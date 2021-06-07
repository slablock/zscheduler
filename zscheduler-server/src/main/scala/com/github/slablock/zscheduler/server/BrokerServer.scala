package com.github.slablock.zscheduler.server

import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}

class BrokerServer {
  @LifecycleStart
  def start(): Unit = {
    System.out.println("start !!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    System.out.println("stop !!!")
  }
}
