package com.github.slablock.zscheduler.server.broker

package object db {

  final case class Job(jobId: Long, name: String, jobType: Int, content: String, user: String) {
    require(jobType > 0, "jobType no greater than 0")
    require(name.nonEmpty, "name.empty")
    require(content.nonEmpty, "content.empty")
    require(user.nonEmpty, "user.empty")
  }

  final case class JobUpdate(name: Option[String] = None, content: Option[String] = None, user: Option[String] = None) {
    def merge(job: Job): Job =
      Job(job.jobId, name.getOrElse(job.name), job.jobType, content.getOrElse(job.content), user.getOrElse(job.user))
  }

}
