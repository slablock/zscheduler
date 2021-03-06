package com.github.slablock.zcheduler.core.guice;

import com.github.slablock.zcheduler.core.lifecycle.Lifecycle;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import java.lang.annotation.Annotation;
import java.util.Set;

public class LifecycleModule implements Module {
    /**
     * This scope includes final logging shutdown, so all other handlers in this lifecycle scope should avoid logging in
     * their stop() method, either failing silently or failing violently and throwing an exception causing an ungraceful
     * exit.
     */
    private final LifecycleScope initScope = new LifecycleScope(Lifecycle.Stage.INIT);
    private final LifecycleScope normalScope = new LifecycleScope(Lifecycle.Stage.NORMAL);
    private final LifecycleScope serverScope = new LifecycleScope(Lifecycle.Stage.SERVER);
    private final LifecycleScope annoucementsScope = new LifecycleScope(Lifecycle.Stage.ANNOUNCEMENTS);

    /**
     * Registers a class to instantiate eagerly.  Classes mentioned here will be pulled out of
     * the injector with an injector.getInstance() call when the lifecycle is created.
     * <p>
     * Eagerly loaded classes will *not* be automatically added to the Lifecycle unless they are bound to the proper
     * scope.  That is, they are generally eagerly loaded because the loading operation will produce some beneficial
     * side-effect even if nothing actually directly depends on the instance.
     * <p>
     * This mechanism exists to allow the {@link Lifecycle} to be the primary entry point from the injector, not to
     * auto-register things with the {@link Lifecycle}.  It is also possible to just bind things eagerly with Guice,
     * it is not clear which is actually the best approach.  This is more explicit, but eager bindings inside of modules
     * is less error-prone.
     *
     * @param clazz the class to instantiate
     * @return this, for chaining.
     */
    public static void register(Binder binder, Class<?> clazz) {
        registerKey(binder, Key.get(clazz));
    }

    /**
     * Registers a class/annotation combination to instantiate eagerly.  Classes mentioned here will be pulled out of
     * the injector with an injector.getInstance() call when the lifecycle is created.
     * <p>
     * Eagerly loaded classes will *not* be automatically added to the Lifecycle unless they are bound to the proper
     * scope.  That is, they are generally eagerly loaded because the loading operation will produce some beneficial
     * side-effect even if nothing actually directly depends on the instance.
     * <p>
     * This mechanism exists to allow the {@link Lifecycle} to be the primary entry point from the injector, not to
     * auto-register things with the {@link Lifecycle}.  It is also possible to just bind things eagerly with Guice,
     * it is not clear which is actually the best approach.  This is more explicit, but eager bindings inside of modules
     * is less error-prone.
     *
     * @param clazz      the class to instantiate
     * @param annotation The annotation class to register with Guice
     * @return this, for chaining
     */
    public static void register(Binder binder, Class<?> clazz, Class<? extends Annotation> annotation) {
        registerKey(binder, Key.get(clazz, annotation));
    }

    /**
     * Registers a key to instantiate eagerly.  {@link Key}s mentioned here will be pulled out of
     * the injector with an injector.getInstance() call when the lifecycle is created.
     * <p>
     * Eagerly loaded classes will *not* be automatically added to the Lifecycle unless they are bound to the proper
     * scope.  That is, they are generally eagerly loaded because the loading operation will produce some beneficial
     * side-effect even if nothing actually directly depends on the instance.
     * <p>
     * This mechanism exists to allow the {@link Lifecycle} to be the primary entry point
     * from the injector, not to auto-register things with the {@link Lifecycle}.  It is
     * also possible to just bind things eagerly with Guice, it is not clear which is actually the best approach.
     * This is more explicit, but eager bindings inside of modules is less error-prone.
     *
     * @param key The key to use in finding the DruidNode instance
     */
    public static void registerKey(Binder binder, Key<?> key) {
        getEagerBinder(binder).addBinding().toInstance(new KeyHolder<Object>(key));
    }

    private static Multibinder<KeyHolder> getEagerBinder(Binder binder) {
        return Multibinder.newSetBinder(binder, KeyHolder.class, Names.named("lifecycle"));
    }

    @Override
    public void configure(Binder binder) {
        getEagerBinder(binder); // Load up the eager binder so that it will inject the empty set at a minimum.

        binder.bindScope(ManageLifecycleInit.class, initScope);
        binder.bindScope(ManageLifecycle.class, normalScope);
        binder.bindScope(ManageLifecycleServer.class, serverScope);
        binder.bindScope(ManageLifecycleAnnouncements.class, annoucementsScope);
    }

    @Provides
    @LazySingleton
    public Lifecycle getLifecycle(final Injector injector) {
        final Key<Set<KeyHolder>> keyHolderKey = Key.get(new TypeLiteral<Set<KeyHolder>>() {
        }, Names.named("lifecycle"));
        final Set<KeyHolder> eagerClasses = injector.getInstance(keyHolderKey);

        Lifecycle lifecycle = new Lifecycle("module") {
            @Override
            public void start() throws Exception {
                for (KeyHolder<?> holder : eagerClasses) {
                    injector.getInstance(holder.getKey()); // Pull the key so as to "eagerly" load up the class.
                }
                super.start();
            }
        };

        initScope.setLifecycle(lifecycle);
        normalScope.setLifecycle(lifecycle);
        serverScope.setLifecycle(lifecycle);
        annoucementsScope.setLifecycle(lifecycle);

        return lifecycle;
    }
}
