package com.github.slablock.zscheduler.server

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorSystem, Terminated}
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import com.github.slablock.zscheduler.server.actor.WorkerActor
import com.github.slablock.zscheduler.server.worker.WorkerConf
import com.github.slablock.zscheduler.server.worker.WorkerConf.config
import org.slf4j.LoggerFactory

class WorkerServer {

  private val LOGGER = LoggerFactory.getLogger(classOf[WorkerServer])

  def bootStrap(context: ActorContext[Nothing]): Unit = {
    context.spawn(WorkerActor(), classOf[WorkerActor].getSimpleName)
  }

  @LifecycleStart
  def start(): Unit = {
    val systemName = config.getString(WorkerConf.SYSTEM_NAME)
    ActorSystem[Nothing](Behaviors.setup[Nothing](context => {
      bootStrap(context)
      Behaviors.receiveSignal[Nothing] {
        case (_, Terminated(_)) =>
          Behaviors.stopped
      }
    }), systemName, config)
    LOGGER.info("Worker start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("Worker stopped!!!")
  }
}
