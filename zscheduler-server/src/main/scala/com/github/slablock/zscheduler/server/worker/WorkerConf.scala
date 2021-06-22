package com.github.slablock.zscheduler.server.worker

import com.github.slablock.zscheduler.server.common.CommonConf
import com.typesafe.config.{Config, ConfigFactory}

object WorkerConf extends CommonConf {

  val FILE = "worker.conf"
  val ROLE = "cluster.roles"

  val config: Config = ConfigFactory.load(FILE)

}
