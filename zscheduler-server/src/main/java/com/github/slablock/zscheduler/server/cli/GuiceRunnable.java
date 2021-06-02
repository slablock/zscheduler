

package com.github.slablock.zscheduler.server.cli;

import com.github.slablock.zcheduler.core.lifecycle.Lifecycle;
import com.github.slablock.zscheduler.server.initialization.Initialization;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.slf4j.Logger;

import java.util.List;

public abstract class GuiceRunnable implements Runnable
{
  private final Logger log;

  private Injector baseInjector;

  public GuiceRunnable(Logger log)
  {
    this.log = log;
  }

  /**
   * This method overrides {@link Runnable} just in order to be able to define it as "entry point" for
   * "Unused declarations" inspection in IntelliJ.
   */
  @Override
  public abstract void run();

  @Inject
  public void configure(Injector injector)
  {
    this.baseInjector = injector;
  }

  protected abstract List<? extends Module> getModules();

  public Injector makeInjector()
  {
    try {
      return Initialization.makeInjectorWithModules(baseInjector, getModules());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Lifecycle initLifecycle(Injector injector)
  {
    try {
      final Lifecycle lifecycle = injector.getInstance(Lifecycle.class);
      try {
        lifecycle.start();
      }
      catch (Throwable t) {
        log.error("Error when starting up.  Failing.", t);
        System.exit(1);
      }
      return lifecycle;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
