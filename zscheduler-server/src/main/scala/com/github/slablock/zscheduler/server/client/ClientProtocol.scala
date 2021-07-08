package com.github.slablock.zscheduler.server.client


object ClientProtocol {


  case class IpInfo(query: String)

  case class ScheduleExpression(scheduleType: Int, expression: String)
  case class DependencyExpression(jobId: Long, rangeExpression: String, offsetExpression: String)

  case class JobSubmit(jobName: String, jobType: Int, contentType: Int, content: String, user: String, priority: Int,
                       schedule: Seq[ScheduleExpression],
                       dependency: Seq[DependencyExpression])

  case class JobSubmitted(jobId: Long)

  case class ErrorResult(error: String)

}
