package com.github.slablock.zscheduler.server.cli;

import com.github.slablock.zcheduler.core.guice.LifecycleModule;
import com.github.slablock.zcheduler.core.guice.ManageLifecycle;
import com.github.slablock.zscheduler.server.WorkerServer;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Module;
import io.airlift.airline.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Command(name = "worker", description = "Runs a worker node")
public class CliWorker extends ServerRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CliWorker.class);

    public CliWorker() {
        super(LOGGER);
    }

    @Override
    protected List<? extends Module> getModules() {
        return ImmutableList.of(
                new Module() {
                    @Override
                    public void configure(Binder binder) {
                        binder.bind(WorkerServer.class).in(ManageLifecycle.class);
                        LifecycleModule.register(binder, WorkerServer.class);
                    }
                }
        );
    }

}
