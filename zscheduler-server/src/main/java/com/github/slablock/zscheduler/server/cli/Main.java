
package com.github.slablock.zscheduler.server.cli;

import com.github.slablock.zscheduler.server.guice.GuiceInjectors;
import com.google.inject.Injector;
import io.airlift.airline.Cli;
import io.airlift.airline.Help;
import io.airlift.airline.ParseException;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final Cli.CliBuilder<Runnable> builder = Cli.builder("zscheduler");

        builder.withDescription("Zscheduler command-line runner.")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Version.class);

        List<Class<? extends Runnable>> serverCommands = Arrays.asList(
                CliBroker.class
        );
        builder.withGroup("server")
                .withDescription("Run one of the Druid server types.")
                .withDefaultCommand(Help.class)
                .withCommands(serverCommands);

        List<Class<? extends Runnable>> toolCommands = Arrays.asList(
        );
        builder.withGroup("tools")
                .withDescription("Various tools for working with Druid")
                .withDefaultCommand(Help.class)
                .withCommands(toolCommands);

        final Injector injector = GuiceInjectors.makeStartupInjector();
        final Cli<Runnable> cli = builder.build();
        try {
            final Runnable command = cli.parse(args);
            if (!(command instanceof Help)) { // Hack to work around Help not liking being injected
                injector.injectMembers(command);
            }
            command.run();
        } catch (ParseException e) {
            System.out.println("ERROR!!!!");
            System.out.println(e.getMessage());
            System.out.println("===");
            cli.parse(new String[]{"help"}).run();
            System.exit(1);
        }
    }
}
