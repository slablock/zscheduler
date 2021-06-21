package com.github.slablock.zscheduler.server.client

import com.github.slablock.zscheduler.server.common.CommonConf
import com.typesafe.config.{Config, ConfigFactory}

object ClientConf extends CommonConf {

  val FILE = "client.conf"
  val PORT = "client.http.port"
  val INTERFACE = "client.http.interface"
  val ROLE = "cluster.roles"

  val config: Config = ConfigFactory.load(FILE)

}
