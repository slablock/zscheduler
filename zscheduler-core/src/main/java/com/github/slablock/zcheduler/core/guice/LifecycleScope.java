package com.github.slablock.zcheduler.core.guice;

import com.github.slablock.zcheduler.core.lifecycle.Lifecycle;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LifecycleScope implements Scope {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleScope.class);

    private final Lifecycle.Stage stage;

    private Lifecycle lifecycle;
    private final List<Object> instances = new ArrayList<>();


    public LifecycleScope(Lifecycle.Stage stage) {
        this.stage = stage;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        synchronized (instances) {
            this.lifecycle = lifecycle;
            for (Object instance : instances) {
                lifecycle.addManagedInstance(instance, stage);
            }
        }
    }


    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return new Provider<T>() {

            private volatile T value = null;

            @Override
            public synchronized T get() {
                if (value == null) {
                    final T retVal = unscoped.get();
                    synchronized (instances) {
                        if (lifecycle == null) {
                            instances.add(retVal);
                        } else {
                            try {
                                lifecycle.addMaybeStartManagedInstance(retVal, stage);
                            } catch (Exception e) {
                                LOGGER.warn("Caught exception when trying to create a[{}]", key, e);
                                return null;
                            }
                        }
                    }
                    value = retVal;
                }
                return value;
            }
        };
    }
}
