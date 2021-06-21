package com.github.slablock.zscheduler.server.broker.db.job

import com.github.slablock.zscheduler.server.broker.db.{Job, JobUpdate}
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class JobService @Inject()(jobStorage: JobStorage) {

  def queryJob(jobId: Long): Future[Option[Job]] =
    jobStorage.queryJob(jobId)

  def addJob(job: Job)(implicit executionContext: ExecutionContext): Future[Job] =
    jobStorage.saveJob(job)

}
