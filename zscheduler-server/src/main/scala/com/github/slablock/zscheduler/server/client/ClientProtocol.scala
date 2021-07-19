package com.github.slablock.zscheduler.server.client


object ClientProtocol {

  case class ScheduleExpression(scheduleType: Int, expression: String)

  case class DependencyExpression(preProjectId: Long,
                                  preFlowId: Long,
                                  preJobId: Long,
                                  rangeExpression: String,
                                  offsetExpression: String)

  case class ProjectSubmit(projectName: String, user: String)
  case class ProjectUpdate(projectId: Long, projectName: String, user: String, updateUser: String)

  case class FlowSubmit(projectId: Long,
                        flowName: String,
                        user: String,
                        dependencies: Seq[DependencyExpression],
                        schedules: Seq[ScheduleExpression])


  case class FlowUpdate(flowId: Long,
                        projectId: Long,
                        flowName: String,
                        user: String,
                        updateUser: String,
                        dependencies: Seq[DependencyExpression],
                        schedules: Seq[ScheduleExpression])

  case class JobSubmit(projectId: Long,
                       jobName: String,
                       jobType: Int,
                       contentType: Int,
                       content: String,
                       params: String,
                       priority: Int,
                       user: String,
                       dependency: Seq[DependencyExpression])


  case class JobUpdate(jobId: Long,
                       projectId: Long,
                       jobName: String,
                       jobType: Int,
                       contentType: Int,
                       content: String,
                       params: String,
                       priority: Int,
                       user: String,
                       updateUser: String,
                       dependency: Seq[DependencyExpression])

  case class ClientResult(code: Int, msg: String, data: Any)

  case class JobVo()


  def successResult(data: Any): ClientResult = ClientResult(200, "success", data)
  def successResult(): ClientResult = ClientResult(200, "success", null)

  def errorResult(msg: String): ClientResult = ClientResult(500, msg, null)
  def errorResult(): ClientResult = ClientResult(500, "error", null)

}
