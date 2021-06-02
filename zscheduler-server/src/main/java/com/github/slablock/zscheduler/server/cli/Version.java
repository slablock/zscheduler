
package com.github.slablock.zscheduler.server.cli;

import io.airlift.airline.Command;

@Command(
    name = "version",
    description = "Returns Druid version information"
)
public class Version implements Runnable
{
  @Override
  public void run()
  {
    System.out.println("");
  }
}
