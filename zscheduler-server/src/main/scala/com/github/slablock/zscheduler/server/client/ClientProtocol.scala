package com.github.slablock.zscheduler.server.client


object ClientProtocol {

  val CODE_SUCCESS: Int = 200
  val CODE_FAIL: Int = 400

  case class ScheduleExpression(scheduleType: Int, expression: String)

  case class DependencyExpression(preProjectId: Long,
                                  preFlowId: Long,
                                  preJobId: Long,
                                  rangeExpression: String,
                                  offsetExpression: String)

  case class ProjectSubmit(projectName: String, user: String)
  case class ProjectUpdate(projectId: Long, projectName: String, user: String, updateUser: String)
  case class ProjectQuery(projectId: Long)
  case class ProjectWriteResult(code: Int, projectId: Long)

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


  case class FlowSubmitResult(code: Int, flowId: Long)

  case class JobSubmit(projectId: Long,
                       flowId: Long,
                       jobName: String,
                       jobType: Int,
                       contentType: Int,
                       content: String,
                       params: String,
                       priority: Int,
                       user: String,
                       dependencies: Seq[DependencyExpression])


  case class JobSubmitResult(code: Int, jobId: Long)

  case class JobUpdate(jobId: Long,
                       projectId: Long,
                       flowId: Long,
                       jobName: String,
                       jobType: Int,
                       contentType: Int,
                       content: String,
                       params: String,
                       priority: Int,
                       user: String,
                       updateUser: String,
                       dependencies: Seq[DependencyExpression])

  case class ErrResult(code: Int, msg: String)

  def errorResult(msg: String): ErrResult = {
    ErrResult(CODE_FAIL, msg)
  }
}
