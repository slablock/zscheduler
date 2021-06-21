package com.github.slablock.zscheduler.server

import akka.actor.typed.{ActorSystem, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import com.github.slablock.zscheduler.server.actor.BrokerActor
import com.github.slablock.zscheduler.server.broker.BrokerConf
import com.github.slablock.zscheduler.server.broker.BrokerConf.config
import org.slf4j.LoggerFactory

class BrokerServer {

  private val LOGGER = LoggerFactory.getLogger(classOf[BrokerServer])

  @LifecycleStart
  def start(): Unit = {
    val systemName = config.getString(BrokerConf.SYSTEM_NAME)
    ActorSystem[Nothing](Behaviors.setup[Nothing](context => {
      context.spawn(BrokerActor(), classOf[BrokerActor].getSimpleName)
      Behaviors.receiveSignal {
        case (_, Terminated(_)) =>
          Behaviors.stopped
      }
    }), systemName, config)
    LOGGER.info("Broker start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("Broker stopped!!!")
  }
}
