package com.github.slablock.zscheduler.server.service.job

import com.github.slablock.zscheduler.dao.Tables._
import com.github.slablock.zscheduler.server.domain.OperationType
import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject
import slick.jdbc.TransactionIsolation

import scala.concurrent.{ExecutionContext, Future}

trait JobStorage {
  def queryJob(jobId: Long): Future[(Option[JobRow], Seq[JobDependencyRow])]

  def saveJob(jobRow: JobRow, jobDependencyRows: Seq[JobDependencyRow])(implicit executionContext: ExecutionContext): Future[Long]

  def updateJob(jobRow: JobRow, jobDependencyRows: Map[Int, Seq[JobDependencyRow]])
               (implicit executionContext: ExecutionContext): Future[Int]
}


private[service] class JdbcJobStorage @Inject()(val dbComponent: ZSDbComponent) extends JobStorage {

  import dbComponent._
  import profile.api._

  override def queryJob(jobId: Long): Future[(Option[JobRow], Seq[JobDependencyRow])] = {
    val jobQ: DBIO[Option[JobRow]] = job.filter(d => d.jobId === jobId).result.headOption
    val jobDependencyQ: DBIO[Seq[JobDependencyRow]] = jobDependency.filter(d => d.jobId === jobId).result
    val q: DBIO[(Option[JobRow], Seq[JobDependencyRow])] = jobQ zip  jobDependencyQ
    db.run(q)
  }


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

  override def updateJob(jobRow: JobRow, jobDependencyRows: Map[Int, Seq[JobDependencyRow]])
                        (implicit executionContext: ExecutionContext): Future[Int] = {
    val jobQ : DBIO[Int] = job.filter(j => j.jobId === jobRow.jobId)
      .map(j=>(j.jobName, j.content, j.params, j.priority, j.user, j.updateUser, j.updateTime))
      .update((jobRow.jobName, jobRow.content, jobRow.params, jobRow.priority, jobRow.user, jobRow.updateUser, jobRow.updateTime))

    val jobDependencyUpdate : DBIO[Int] = DBIO.sequence(jobDependencyRows.get(OperationType.EDIT.getValue)
      .map(_.map(jd=>{
        jobDependency.filter(d=>d.id === jd.id)
          .map(d=>(d.preProjectId, d.preFlowId, d.preJobId, d.updateTime))
          .update((jd.preProjectId, jd.preFlowId, jd.preJobId, jd.updateTime))
      })).getOrElse(Seq())).collect(r=>r.sum)

    val jobDependencyAdd : DBIO[Int] = (jobDependency ++= jobDependencyRows
      .getOrElse(OperationType.ADD.getValue, Seq())).collect(r=>r.sum)

    val toDelete = jobDependencyRows.get(OperationType.DELETE.getValue).map(_.map(d=>d.id)).getOrElse(Seq())

    val jobDependencyDelete : DBIO[Int] = jobDependency.filter(d=>d.id.inSet(toDelete)).delete

    val q: DBIO[Int] = DBIO.sequence(Vector(jobQ, jobDependencyUpdate, jobDependencyAdd, jobDependencyDelete)).collect(r=>r.sum)
    db.run(q.withTransactionIsolation(TransactionIsolation.ReadCommitted).transactionally)
  }

}

