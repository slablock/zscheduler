package com.github.slablock.zscheduler.server.broker.db.job


import com.github.slablock.zscheduler.server.broker.db.Job
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

sealed trait JobStorage {
  def queryJob(jobId: Long): Future[Option[Job]]
  def saveJob(job: Job)(implicit executionContext: ExecutionContext): Future[Job]
}

private [db] class JdbcJobStorage @Inject()(val table: JobTable) extends JobStorage {
  import table._
  import dbComponent._
  import profile.api._

  override def queryJob(jobId: Long): Future[Option[Job]] =
    db.run(jobQuery.filter(d => d.jobId === jobId).result.headOption)

  private val insertQuery = jobQuery returning jobQuery.map(_.jobId) into ((item, jobId) => item.copy(jobId = jobId))

  override def saveJob(job: Job)(implicit executionContext: ExecutionContext): Future[Job] = {
    val action = insertQuery += job
    db.run(action.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}

