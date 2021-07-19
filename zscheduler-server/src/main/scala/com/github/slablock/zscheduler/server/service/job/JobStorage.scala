package com.github.slablock.zscheduler.server.service.job

import com.github.slablock.zscheduler.dao.Tables._
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

trait JobStorage {
  def queryJob(jobId: Long): Future[Option[JobRow]]
  def saveJob(job: JobRow)(implicit executionContext: ExecutionContext): Future[JobRow]
}


private [service] class JdbcJobStorage @Inject()(val dbComponent: ZSDbComponent) extends JobStorage {
  import dbComponent._
  import profile.api._

  override def queryJob(jobId: Long): Future[Option[JobRow]] =
    db.run(job.filter(d => d.jobId === jobId).result.headOption)


  private val insertQuery = job returning job.map(_.jobId) into ((item, jobId) => item.copy(jobId = jobId))

  override def saveJob(job: JobRow)(implicit executionContext: ExecutionContext): Future[JobRow] = {
    val action = insertQuery += job
    db.run(action.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}

