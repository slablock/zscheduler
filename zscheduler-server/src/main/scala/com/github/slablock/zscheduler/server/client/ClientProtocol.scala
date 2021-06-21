package com.github.slablock.zscheduler.server.client


object ClientProtocol {
  case class IpInfo(query: String)
  case class Job(name: String, jobType: Int, content: String, user: String)
}
