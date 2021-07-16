package com.github.slablock.zscheduler.server.service.guice

import com.github.slablock.zscheduler.dao.Tables
import com.github.slablock.zscheduler.server.service._
import com.github.slablock.zscheduler.server.service.job.{JdbcJobStorage, JobService, JobStorage}
import com.google.inject.{AbstractModule, Singleton}
import net.codingwell.scalaguice.ScalaModule

class StorageModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ZSDbComponent].to[ZSMySqlDbComponent].in[Singleton]
    bind[Tables].in[Singleton]
    bind[JobStorage].to[JdbcJobStorage].in[Singleton]
    bind[JobService].in[Singleton]
  }

}