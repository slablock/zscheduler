package com.github.slablock.zscheduler.server.service.job

import com.github.slablock.zscheduler.dao.Tables._
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

trait JobStorage {
  def queryJob(jobId: Long): Future[Option[JobRow]]

  def saveJob(jobRow: JobRow, jobDependencyRows: Seq[JobDependencyRow])(implicit executionContext: ExecutionContext): Future[Long]
}


private[service] class JdbcJobStorage @Inject()(val dbComponent: ZSDbComponent) extends JobStorage {

  import dbComponent._
  import profile.api._

  override def queryJob(jobId: Long): Future[Option[JobRow]] =
    db.run(job.filter(d => d.jobId === jobId).result.headOption)


  override def saveJob(jobRow: JobRow, jobDependencyRows: Seq[JobDependencyRow])
                      (implicit executionContext: ExecutionContext): Future[Long] = {

    val jobQ: DBIO[Long] = job returning job.map(_.jobId) += jobRow

    def addDependencies(jobId: Long): DBIO[Option[Int]] = jobDependency ++= jobDependencyRows.map(jd => {
      JobDependencyRow(jd.id, jd.preProjectId, jd.flowId,
        jobId, jd.preProjectId, jd.preFlowId, jd.preJobId, jd.rangeExpression,
        jd.offsetExpression, jd.createTime, jd.updateTime)
    })

    val q: DBIO[Long] = jobQ.flatMap(jobId => {
      addDependencies(jobId).andThen(DBIO.successful(jobId))
    })

    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}

