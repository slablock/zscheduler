package com.github.slablock.zscheduler.server.client


object ClientProtocol {


  case class IpInfo(query: String)

  case class JobSubmit(jobName: String, jobType: Int, contentType: Int, content: String, user: String, priority: Int)

  case class JobSubmitted(jobId: Long)

  case class ErrorResult(error: String)

}
