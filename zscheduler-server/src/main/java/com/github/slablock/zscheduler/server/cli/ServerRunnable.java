
package com.github.slablock.zscheduler.server.cli;

import com.github.slablock.zcheduler.core.lifecycle.Lifecycle;
import com.github.slablock.zscheduler.server.guice.Injectors;
import com.google.inject.Injector;
import org.slf4j.Logger;


public abstract class ServerRunnable extends GuiceRunnable {

    public ServerRunnable(Logger log) {
        super(log);
    }

    @Override
    public void run() {
        final Injector injector = makeInjector();
        Injectors.bind(injector);

        final Lifecycle lifecycle = initLifecycle(injector);

        try {
            lifecycle.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
