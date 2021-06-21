package com.github.slablock.zscheduler.server.broker

import com.github.slablock.zscheduler.server.common.CommonConf
import com.typesafe.config.{Config, ConfigFactory}

object BrokerConf extends CommonConf {

  val FILE = "client.conf"
  val ROLE = "cluster.roles"

  val config: Config = ConfigFactory.load(FILE)

}
