package com.github.slablock.zscheduler.server.service.job

import com.github.slablock.zscheduler.dao.Tables._
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.JobSubmitRequest
import com.google.inject.Inject
import org.joda.time.DateTime

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

class JobService @Inject()(jobStorage: JobStorage) {

  def queryJob(jobId: Long): Future[Option[JobRow]] =
    jobStorage.queryJob(jobId)


  def addJob(req: JobSubmitRequest)(implicit executionContext: ExecutionContext): Future[Long] = {
    val time = new Timestamp(DateTime.now().getMillis)
    val jobId = 0
    val job = JobRow(jobId, req.projectId, req.flowId, req.jobName, req.jobType, req.contentType, req.content,
      req.params, req.priority, req.user, req.user, time, time)

    val jobDependencies = req.dependencies.map(d=>
      JobDependencyRow(0, req.projectId, req.flowId, jobId,
        d.preProjectId, d.preFlowId, d.preJobId,
        d.rangeExpression, d.offsetExpression, time, time))
    jobStorage.saveJob(job, jobDependencies)
  }

}
