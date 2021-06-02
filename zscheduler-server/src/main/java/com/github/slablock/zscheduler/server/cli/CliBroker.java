package com.github.slablock.zscheduler.server.cli;

import com.google.common.collect.ImmutableList;
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
        return ImmutableList.of();
    }
}
