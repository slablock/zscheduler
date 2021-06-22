package com.github.slablock.zscheduler.server

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Terminated}
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import com.github.slablock.zscheduler.server.actor.WorkerActor
import com.github.slablock.zscheduler.server.worker.WorkerConf
import com.github.slablock.zscheduler.server.worker.WorkerConf.config
import org.slf4j.LoggerFactory

class LogStorageServer {

  private val LOGGER = LoggerFactory.getLogger(classOf[LogStorageServer])

  @LifecycleStart
  def start(): Unit = {
    LOGGER.info("LogStorage start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("LogStorage stopped!!!")
  }
}
