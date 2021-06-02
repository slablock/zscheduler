

package com.github.slablock.zscheduler.server.initialization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * The ClassLoader that gets used when druid.extensions.useExtensionClassloaderFirst = true.
 */
public class ExtensionFirstClassLoader extends URLClassLoader
{
  private final ClassLoader zLoader;

  public ExtensionFirstClassLoader(final URL[] urls, final ClassLoader druidLoader)
  {
    super(urls, null);
    this.zLoader = Preconditions.checkNotNull(druidLoader, "zLoader");
  }

  @Override
  public Class<?> loadClass(final String name) throws ClassNotFoundException
  {
    return loadClass(name, false);
  }

  @Override
  protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException
  {
    synchronized (getClassLoadingLock(name)) {
      Class<?> clazz = findLoadedClass(name);

      if (clazz == null) {
        // Try extension classloader first.
        try {
          clazz = findClass(name);
        }
        catch (ClassNotFoundException e) {
          // Try the Druid classloader. Will throw ClassNotFoundException if the class can't be loaded.
          return zLoader.loadClass(name);
        }
      }

      if (resolve) {
        resolveClass(clazz);
      }

      return clazz;
    }
  }

  @Override
  public URL getResource(final String name)
  {
    final URL resourceFromExtension = super.getResource(name);

    if (resourceFromExtension != null) {
      return resourceFromExtension;
    } else {
      return zLoader.getResource(name);
    }
  }

  @Override
  public Enumeration<URL> getResources(final String name) throws IOException
  {
    final List<URL> urls = new ArrayList<>();
    Iterators.addAll(urls, Iterators.forEnumeration(super.getResources(name)));
    Iterators.addAll(urls, Iterators.forEnumeration(zLoader.getResources(name)));
    return Iterators.asEnumeration(urls.iterator());
  }
}
