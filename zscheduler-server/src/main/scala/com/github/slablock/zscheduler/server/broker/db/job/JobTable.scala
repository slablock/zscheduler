package com.github.slablock.zscheduler.server.broker.db.job

import com.github.slablock.zscheduler.server.broker.db.{ZSDbComponent, Job}
import com.google.inject.Inject

private[db] class JobTable @Inject()(val dbComponent: ZSDbComponent) {

  import dbComponent.profile.api._

  class JobSchema(tag: Tag) extends Table[Job](tag, "job") {
    def jobId        = column[Long]("jobId", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def jobType = column[Int]("jobType")
    def content = column[String]("content")
    def user = column[String]("user")
    def * = (jobId, name, jobType, content, user) <> ((Job.apply _).tupled, Job.unapply)
  }

  val jobQuery = TableQuery[JobSchema]
}
