package com.github.slablock.zscheduler.server.service.guice

import com.github.slablock.zscheduler.server.service._
import com.github.slablock.zscheduler.server.service.flow.{FlowService, FlowStorage, JdbcFlowStorage}
import com.github.slablock.zscheduler.server.service.job.{JdbcJobStorage, JobService, JobStorage}
import com.github.slablock.zscheduler.server.service.project.{JdbcProjectStorage, ProjectService, ProjectStorage}
import com.google.inject.{AbstractModule, Singleton}
import net.codingwell.scalaguice.ScalaModule

class StorageModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ZSDbComponent].to[ZSMySqlDbComponent].in[Singleton]

    bind[JobStorage].to[JdbcJobStorage].in[Singleton]
    bind[JobService].in[Singleton]

    bind[ProjectStorage].to[JdbcProjectStorage].in[Singleton]
    bind[ProjectService].in[Singleton]

    bind[FlowStorage].to[JdbcFlowStorage].in[Singleton]
    bind[FlowService].in[Singleton]
  }

}
