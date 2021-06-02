

package com.github.slablock.zscheduler.server.initialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slablock.zcheduler.core.guice.LifecycleModule;
import com.github.slablock.zcheduler.core.guice.ZSecondaryModule;
import com.github.slablock.zcheduler.core.guice.annotations.Json;
import com.github.slablock.zcheduler.core.guice.annotations.Smile;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 */
public class Initialization
{
  private static final Logger log = LoggerFactory.getLogger(Initialization.class);
  private static final ConcurrentHashMap<File, URLClassLoader> LOADERS_MAP = new ConcurrentHashMap<>();

  private static final ConcurrentHashMap<Class<?>, Collection<?>> EXTENSIONS_MAP = new ConcurrentHashMap<>();

  /**
   * @param clazz service class
   * @param <T>   the service type
   *
   * @return Returns a collection of implementations loaded.
   */
  public static <T> Collection<T> getLoadedImplementations(Class<T> clazz)
  {
    @SuppressWarnings("unchecked")
    Collection<T> retVal = (Collection<T>) EXTENSIONS_MAP.get(clazz);
    if (retVal == null) {
      return new HashSet<>();
    }
    return retVal;
  }

  @VisibleForTesting
  static void clearLoadedImplementations()
  {
    EXTENSIONS_MAP.clear();
  }

  @VisibleForTesting
  static Map<File, URLClassLoader> getLoadersMap()
  {
    return LOADERS_MAP;
  }

  /**
   * Look for implementations for the given class from both classpath and extensions directory, using {@link
   * ServiceLoader}. A user should never put the same two extensions in classpath and extensions directory, if he/she
   * does that, the one that is in the classpath will be loaded, the other will be ignored.
   *
   * @param config       Extensions configuration
   * @param serviceClass The class to look the implementations of (e.g., DruidModule)
   *
   * @return A collection that contains implementations (of distinct concrete classes) of the given class. The order of
   * elements in the returned collection is not specified and not guaranteed to be the same for different calls to
   * getFromExtensions().
   */
  public static <T> Collection<T> getFromExtensions(ExtensionsConfig config, Class<T> serviceClass)
  {
    // It's not clear whether we should recompute modules even if they have been computed already for the serviceClass,
    // but that's how it used to be an preserving the old behaviour here.
    Collection<?> modules = EXTENSIONS_MAP.compute(
        serviceClass,
        (serviceC, ignored) -> new ServiceLoadingFromExtensions<>(config, serviceC).implsToLoad
    );
    //noinspection unchecked
    return (Collection<T>) modules;
  }

  private static class ServiceLoadingFromExtensions<T>
  {
    private final ExtensionsConfig extensionsConfig;
    private final Class<T> serviceClass;
    private final List<T> implsToLoad = new ArrayList<>();
    private final Set<String> implClassNamesToLoad = new HashSet<>();

    private ServiceLoadingFromExtensions(ExtensionsConfig extensionsConfig, Class<T> serviceClass)
    {
      this.extensionsConfig = extensionsConfig;
      this.serviceClass = serviceClass;
      if (extensionsConfig.searchCurrentClassloader()) {
        addAllFromCurrentClassLoader();
      }
      addAllFromFileSystem();
    }

    private void addAllFromCurrentClassLoader()
    {
      ServiceLoader
          .load(serviceClass, Thread.currentThread().getContextClassLoader())
          .forEach(impl -> tryAdd(impl, "classpath"));
    }

    private void addAllFromFileSystem()
    {
      for (File extension : getExtensionFilesToLoad(extensionsConfig)) {
        log.debug("Loading extension [{}] for class [{}]", extension.getName(), serviceClass);
        try {
          final URLClassLoader loader = getClassLoaderForExtension(
              extension,
              extensionsConfig.isUseExtensionClassloaderFirst()
          );

          log.info(
              "Loading extension [{}], jars: {}",
              extension.getName(),
              Arrays.stream(loader.getURLs())
                    .map(u -> new File(u.getPath()).getName())
                    .collect(Collectors.joining(", "))
          );

          ServiceLoader.load(serviceClass, loader).forEach(impl -> tryAdd(impl, "local file system"));
        }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    private void tryAdd(T serviceImpl, String extensionType)
    {
      final String serviceImplName = serviceImpl.getClass().getName();
      if (serviceImplName == null) {
        log.warn(
            "Implementation [{}] was ignored because it doesn't have a canonical name, "
            + "is it a local or anonymous class?",
            serviceImpl.getClass().getName()
        );
      } else if (!implClassNamesToLoad.contains(serviceImplName)) {
        log.debug(
            "Adding implementation [{}] for class [{}] from {} extension",
            serviceImplName,
            serviceClass,
            extensionType
        );
        implClassNamesToLoad.add(serviceImplName);
        implsToLoad.add(serviceImpl);
      }
    }
  }

  /**
   * Find all the extension files that should be loaded by druid.
   * <p/>
   * If user explicitly specifies druid.extensions.loadList, then it will look for those extensions under root
   * extensions directory. If one of them is not found, druid will fail loudly.
   * <p/>
   * If user doesn't specify druid.extension.toLoad (or its value is empty), druid will load all the extensions
   * under the root extensions directory.
   *
   * @param config ExtensionsConfig configured by druid.extensions.xxx
   *
   * @return an array of druid extension files that will be loaded by druid process
   */
  public static File[] getExtensionFilesToLoad(ExtensionsConfig config)
  {
    final File rootExtensionsDir = new File(config.getDirectory());
    if (rootExtensionsDir.exists() && !rootExtensionsDir.isDirectory()) {
      throw new IllegalStateException(String.format("Root extensions directory [%s] is not a directory!?", rootExtensionsDir));
    }
    File[] extensionsToLoad;
    final LinkedHashSet<String> toLoad = config.getLoadList();
    if (toLoad == null) {
      extensionsToLoad = rootExtensionsDir.listFiles();
    } else {
      int i = 0;
      extensionsToLoad = new File[toLoad.size()];
      for (final String extensionName : toLoad) {
        File extensionDir = new File(extensionName);
        if (!extensionDir.isAbsolute()) {
          extensionDir = new File(rootExtensionsDir, extensionName);
        }

        if (!extensionDir.isDirectory()) {
          throw new IllegalStateException(String.format("Extension [%s] specified in \"druid.extensions.loadList\" didn't exist!?",
                  extensionDir.getAbsolutePath()));
        }
        extensionsToLoad[i++] = extensionDir;
      }
    }
    return extensionsToLoad == null ? new File[]{} : extensionsToLoad;
  }


  /**
   * @param extension The File instance of the extension we want to load
   *
   * @return a URLClassLoader that loads all the jars on which the extension is dependent
   */
  public static URLClassLoader getClassLoaderForExtension(File extension, boolean useExtensionClassloaderFirst)
  {
    return LOADERS_MAP.computeIfAbsent(
        extension,
        theExtension -> makeClassLoaderForExtension(theExtension, useExtensionClassloaderFirst)
    );
  }

  private static URLClassLoader makeClassLoaderForExtension(
      final File extension,
      final boolean useExtensionClassloaderFirst
  )
  {
    final Collection<File> jars = FileUtils.listFiles(extension, new String[]{"jar"}, false);
    final URL[] urls = new URL[jars.size()];

    try {
      int i = 0;
      for (File jar : jars) {
        final URL url = jar.toURI().toURL();
        log.debug("added URL[%s] for extension[%s]", url, extension.getName());
        urls[i++] = url;
      }
    }
    catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }

    if (useExtensionClassloaderFirst) {
      return new ExtensionFirstClassLoader(urls, Initialization.class.getClassLoader());
    } else {
      return new URLClassLoader(urls, Initialization.class.getClassLoader());
    }
  }

  public static List<URL> getURLsForClasspath(String cp)
  {
    try {
      String[] paths = cp.split(File.pathSeparator);

      List<URL> urls = new ArrayList<>();
      for (String path : paths) {
        File f = new File(path);
        if ("*".equals(f.getName())) {
          File parentDir = f.getParentFile();
          if (parentDir.isDirectory()) {
            File[] jars = parentDir.listFiles(
                new FilenameFilter()
                {
                  @Override
                  public boolean accept(File dir, String name)
                  {
                    return name != null && (name.endsWith(".jar") || name.endsWith(".JAR"));
                  }
                }
            );
            for (File jar : jars) {
              urls.add(jar.toURI().toURL());
            }
          }
        } else {
          urls.add(new File(path).toURI().toURL());
        }
      }
      return urls;
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Injector makeInjectorWithModules(final Injector baseInjector, Iterable<? extends Module> modules)
  {
    final ModuleList defaultModules = new ModuleList(baseInjector);
    defaultModules.addModules(
        // New modules should be added after Log4jShutterDownerModule
        new Log4jShutterDownerModule(),
        new LifecycleModule()
    );

    ModuleList actualModules = new ModuleList(baseInjector);
    actualModules.addModule(ZSecondaryModule.class);
    for (Object module : modules) {
      actualModules.addModule(module);
    }

    Module intermediateModules = Modules.override(defaultModules.getModules()).with(actualModules.getModules());

    ModuleList extensionModules = new ModuleList(baseInjector);
    final ExtensionsConfig config = baseInjector.getInstance(ExtensionsConfig.class);
    for (ZModule module : Initialization.getFromExtensions(config, ZModule.class)) {
      extensionModules.addModule(module);
    }

    return Guice.createInjector(Modules.override(intermediateModules).with(extensionModules.getModules()));
  }

  private static class ModuleList
  {
    private final Injector baseInjector;
    private final ModulesConfig modulesConfig;
    private final ObjectMapper jsonMapper;
    private final ObjectMapper smileMapper;
    private final List<Module> modules;

    public ModuleList(Injector baseInjector)
    {
      this.baseInjector = baseInjector;
      this.modulesConfig = baseInjector.getInstance(ModulesConfig.class);
      this.jsonMapper = baseInjector.getInstance(Key.get(ObjectMapper.class, Json.class));
      this.smileMapper = baseInjector.getInstance(Key.get(ObjectMapper.class, Smile.class));
      this.modules = new ArrayList<>();
    }

    private List<Module> getModules()
    {
      return Collections.unmodifiableList(modules);
    }

    public void addModule(Object input)
    {
      if (input instanceof ZModule) {
        if (!checkModuleClass(input.getClass())) {
          return;
        }
        baseInjector.injectMembers(input);
        modules.add(registerJacksonModules(((ZModule) input)));
      } else if (input instanceof Module) {
        if (!checkModuleClass(input.getClass())) {
          return;
        }
        baseInjector.injectMembers(input);
        modules.add((Module) input);
      } else if (input instanceof Class) {
        if (!checkModuleClass((Class<?>) input)) {
          return;
        }
        if (ZModule.class.isAssignableFrom((Class) input)) {
          modules.add(registerJacksonModules(baseInjector.getInstance((Class<? extends ZModule>) input)));
        } else if (Module.class.isAssignableFrom((Class) input)) {
          modules.add(baseInjector.getInstance((Class<? extends Module>) input));
          return;
        } else {
          throw new IllegalStateException(String.format("Class[%s] does not implement %s", input.getClass(), Module.class));
        }
      } else {
        throw new IllegalStateException(String.format("Unknown module type[%s]", input.getClass()));
      }
    }

    private boolean checkModuleClass(Class<?> moduleClass)
    {
      String moduleClassName = moduleClass.getName();
      if (moduleClassName != null && modulesConfig.getExcludeList().contains(moduleClassName)) {
        log.info("Not loading module [{}] because it is present in excludeList", moduleClassName);
        return false;
      }
      return true;
    }

    public void addModules(Object... object)
    {
      for (Object o : object) {
        addModule(o);
      }
    }

    private ZModule registerJacksonModules(ZModule module)
    {
      for (com.fasterxml.jackson.databind.Module jacksonModule : module.getJacksonModules()) {
        jsonMapper.registerModule(jacksonModule);
        smileMapper.registerModule(jacksonModule);
      }
      return module;
    }
  }
}
