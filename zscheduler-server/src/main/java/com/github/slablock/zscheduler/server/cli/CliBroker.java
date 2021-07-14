package com.github.slablock.zscheduler.server.cli;

import com.github.slablock.zcheduler.core.guice.LifecycleModule;
import com.github.slablock.zcheduler.core.guice.ManageLifecycle;
import com.github.slablock.zscheduler.server.BrokerServer;
import com.github.slablock.zscheduler.server.broker.BrokerConf;
import com.github.slablock.zscheduler.server.broker.db.guice.StorageModule;
import com.github.slablock.zscheduler.server.guice.ZSMyBatisModule;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binder;
import com.google.inject.Module;
import io.airlift.airline.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Command(name = "broker", description = "Runs a broker node")
public class CliBroker extends ServerRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CliBroker.class);

    public CliBroker() {
        super(LOGGER);
    }

    @Override
    protected List<? extends Module> getModules() {
        return ImmutableList.of(
                new ZSMyBatisModule(BrokerConf.config()),
                new StorageModule(),
                new Module() {
                    @Override
                    public void configure(Binder binder) {
                        binder.bind(BrokerServer.class).in(ManageLifecycle.class);
                        LifecycleModule.register(binder, BrokerServer.class);
                    }
                }
        );
    }
}
