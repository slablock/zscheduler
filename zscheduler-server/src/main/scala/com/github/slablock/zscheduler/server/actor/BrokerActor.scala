package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, Routers}
import com.github.slablock.zscheduler.dao.Tables.{JobDependencyRow, JobRow, ProjectRow}
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.WorkerActor.WorkerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerStatus, JobSubmitRequest, ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, JobSubmitResp, ProjectInfoEntry, ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp}
import com.github.slablock.zscheduler.server.actor.protos.workerActor.{TaskSubmitRequest, WorkerMsg}
import com.github.slablock.zscheduler.server.guice.Injectors
import com.github.slablock.zscheduler.server.service.job.JobService
import com.github.slablock.zscheduler.server.service.project.ProjectService
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import org.joda.time.DateTime

import java.sql.Timestamp
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class BrokerActor(context: ActorContext[BrokerCommand]) extends AbstractBehavior[BrokerCommand](context) {

  private val jobService = Injectors.get().instance[JobService]
  private val projectService = Injectors.get().instance[ProjectService]

  implicit val executionContext: ExecutionContext = context.system.executionContext
  val worker: ActorRef[WorkerCommand] = context.spawn(Routers.group(WorkerActor.serviceKey).withRoundRobinRouting(), "worker-group")

  override def onMessage(msg: BrokerCommand): Behavior[BrokerCommand] = {
    msg match {
      case BrokerStatus(client) => {
        context.log.info("broker receive msg!!!")
        client ! ClusterInfo("hello")
        Behaviors.same
      }
      case JobSubmitRequest(projectId, flowId, jobName, jobType, contentType, content, params, priority, user, dependencies, sender) => {
        val time = new Timestamp(DateTime.now().getMillis)
        val jobId = 0
        val job = JobRow(jobId, projectId, flowId, jobName, jobType, contentType, content, params, priority, user, user, time, time)
        val jobDependencies = dependencies.map(d=>
          JobDependencyRow(0, projectId, flowId, jobId,
            d.preProjectId, d.preFlowId, d.preJobId,
            d.rangeExpression, d.offsetExpression, time, time))
        jobService.addJob(job, jobDependencies)
          .onComplete({
            case Success(jobId) =>
              worker ! TaskSubmitRequest(s"$jobId", jobName, jobType, content, user, context.self)
              sender ! JobSubmitResp(jobId)
            case Failure(ex) =>
              sender ! JobSubmitResp(-1)
          })
        Behaviors.same
      }
      case ProjectSubmitRequest(projectName, user, sender)  => {
        val time = new Timestamp(DateTime.now().getMillis)
        projectService.addProject(ProjectRow(0, projectName, user, user, time, time))
          .onComplete({
            case Success(projectId) =>
              sender ! ProjectSubmitResp(success = true, projectId)
            case Failure(ex) =>
              sender ! ProjectSubmitResp(success = false, msg = ex.getLocalizedMessage)
          })
        Behaviors.same
      }
      case ProjectUpdateRequest(projectId, projectName, user, updateUser, sender)  => {
        val time = new Timestamp(DateTime.now().getMillis)
        projectService.updateProject(ProjectRow(projectId, projectName, user, updateUser, time, time)).onComplete({
          case Success(rows) => {
            if (rows > 0) {
              sender ! ProjectUpdateResp(success = true, projectId, "")
            } else {
              sender ! ProjectUpdateResp(success = false, projectId, "not found!")
            }
          }
          case Failure(ex) =>
            sender ! ProjectUpdateResp(success = false, projectId, msg = ex.getLocalizedMessage)
        })
        Behaviors.same
      }
      case ProjectQueryRequest(projectId, sender) => {
        projectService.queryProject(projectId).onComplete({
        case Success(None) => sender ! ProjectQueryResp(success = true, Option.empty, "")
          case Success(Some(ProjectRow(projectId, projectName, user, updateUser, createTime, updateTime))) =>
            sender ! ProjectQueryResp(success = true,
              Some(ProjectInfoEntry(projectId, projectName, user, updateUser, createTime.getTime, updateTime.getTime)),"")
          case Failure(ex) => sender ! ProjectQueryResp(success = false, Option.empty, ex.getLocalizedMessage)
        })
        Behaviors.same
      }
    }
  }
}

object BrokerActor {

  trait BrokerCommand

  val serviceKey: ServiceKey[BrokerCommand] = ServiceKey[BrokerCommand]("broker")

  def apply(): Behavior[BrokerCommand] = Behaviors.setup[BrokerCommand](context => {
    context.system.receptionist ! Receptionist.Register(serviceKey, context.self)
    new BrokerActor(context)
  })
}
