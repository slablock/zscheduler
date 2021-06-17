package com.github.slablock.zscheduler.server.cli;

import com.github.slablock.zcheduler.core.guice.LifecycleModule;
import com.github.slablock.zcheduler.core.guice.ManageLifecycle;
import com.github.slablock.zscheduler.server.ClientServer;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Module;
import io.airlift.airline.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Command(name = "client", description = "Runs a client node")
public class CliClient extends ServerRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CliClient.class);

    public CliClient() {
        super(LOGGER);
    }

    @Override
    protected List<? extends Module> getModules() {
        return ImmutableList.of(
                new Module() {
                    @Override
                    public void configure(Binder binder) {
                        binder.bind(ClientServer.class).in(ManageLifecycle.class);
                        LifecycleModule.register(binder, ClientServer.class);
                    }
                }
        );
    }
}
