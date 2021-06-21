package com.github.slablock.zscheduler.server

import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import org.slf4j.LoggerFactory
import com.github.slablock.zscheduler.server.actor.ClientActor
import com.github.slablock.zscheduler.server.client.ClientConf.config
import com.github.slablock.zscheduler.server.client.ClientConf


class ClientServer {

  private val LOGGER = LoggerFactory.getLogger(classOf[ClientServer])

  def bootStrap(context: ActorContext[Nothing]): Unit = {
    context.spawn(ClientActor(), classOf[ClientActor].getSimpleName)
  }

  @LifecycleStart
  def start(): Unit = {
    val systemName = config.getString(ClientConf.SYSTEM_NAME)
    ActorSystem[Nothing](Behaviors.setup[Nothing](context => {
      bootStrap(context)
      Behaviors.receiveSignal[Nothing] {
        case (_, Terminated(_)) =>
          Behaviors.stopped
      }
    }), systemName, config)
    LOGGER.info("Client start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("Client stopped!!!")
  }

}
