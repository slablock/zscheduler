package com.github.slablock.zscheduler.server

import akka.actor.typed.ActorSystem
import com.github.slablock.zcheduler.core.lifecycle.{LifecycleStart, LifecycleStop}
import com.github.slablock.zscheduler.server.actor.BrokerActor
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerMsg
import com.github.slablock.zscheduler.server.common.ClusterRole
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

class BrokerServer {

  private val LOGGER = LoggerFactory.getLogger(classOf[BrokerServer])

  @LifecycleStart
  def start(): Unit = {
    val config = ConfigFactory.load()
      .withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [${ClusterRole.BROKER}]"))
    val systemName = config.getString("zs.system")
    ActorSystem[BrokerMsg](BrokerActor(), systemName, config)
    LOGGER.info("Broker start successfully!!!")
  }

  @LifecycleStop
  def stop(): Unit = {
    LOGGER.info("Broker stopped!!!")
  }
}
