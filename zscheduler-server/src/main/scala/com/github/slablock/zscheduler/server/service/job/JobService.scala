package com.github.slablock.zscheduler.server.service.job

import com.github.slablock.zscheduler.dao.Tables._
import com.google.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

class JobService @Inject()(jobStorage: JobStorage) {

  def queryJob(jobId: Long): Future[Option[JobRow]] =
    jobStorage.queryJob(jobId)

  def addJob(job: JobRow, jobDependencies: Seq[JobDependencyRow])(implicit executionContext: ExecutionContext): Future[Long] =
    jobStorage.saveJob(job, jobDependencies)

}
