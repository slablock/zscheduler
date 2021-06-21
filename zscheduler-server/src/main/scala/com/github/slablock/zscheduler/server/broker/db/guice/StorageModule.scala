package com.github.slablock.zscheduler.server.broker.db.guice

import com.github.slablock.zscheduler.server.broker.db.job.{JdbcJobStorage, JobService, JobStorage, JobTable}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import com.github.slablock.zscheduler.server.broker.db.{ZSDbComponent, ZSMySqlDbComponent}
import com.google.inject.Singleton

class StorageModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ZSDbComponent].to[ZSMySqlDbComponent].in[Singleton]
    bind[JobTable].in[Singleton]
    bind[JobStorage].to[JdbcJobStorage].in[Singleton]
    bind[JobService].in[Singleton]
  }

}
