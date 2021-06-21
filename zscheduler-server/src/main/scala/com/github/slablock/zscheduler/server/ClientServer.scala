package com.github.slablock.zscheduler.server

import akka.actor.typed.{ActorSystem, Behavior, Terminated}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import org.slf4j.LoggerFactory
import com.github.slablock.zscheduler.server.actor.ClientActor
import com.github.slablock.zscheduler.server.client.{ClientConf, HttpServer}


class ClientServer {

  private val LOGGER = LoggerFactory.getLogger(classOf[ClientServer])

  def bootStrap(context: ActorContext[Nothing]): Unit = {
    val clientActor = context.spawn(ClientActor(), classOf[ClientActor].getSimpleName)
    HttpServer(clientActor, context.system)
  }

  @LifecycleStart
  def start(): Unit = {
    val systemName = ClientConf.config.getString(ClientConf.SYSTEM_NAME)
    implicit val system: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.setup[Nothing](context => {
      bootStrap(context)
      Behaviors.receiveSignal[Nothing] {
        case (context, Terminated(_)) => {
          context
          Behaviors.stopped[Nothing]
        }
      }
    }), systemName, ClientConf.config)
    LOGGER.info("Client start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("Client stopped!!!")
  }

}
