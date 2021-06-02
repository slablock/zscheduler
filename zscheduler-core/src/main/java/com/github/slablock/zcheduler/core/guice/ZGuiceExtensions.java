package com.github.slablock.zcheduler.core.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

public class ZGuiceExtensions implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bindScope(LazySingleton.class, ZScopes.SINGLETON);
    }
}
