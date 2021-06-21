package com.github.slablock.zscheduler.server.client


object ClientProtocol {


  case class IpInfo(query: String)

  case class JobSubmit(name: String, jobType: Int, content: String, user: String)
  case class JobSubmitted(jobId: Long)

  case class ErrorResult(error: String)

}
