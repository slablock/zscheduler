package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, Routers}
import com.github.slablock.zscheduler.dao.Tables.{JobDependencyRow, JobRow, ProjectRow}
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.WorkerActor.WorkerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerStatus, FlowQueryRequest, FlowSubmitRequest, FlowUpdateRequest, JobSubmitRequest, ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, JobSubmitResp, ProjectInfoEntry, ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp}
import com.github.slablock.zscheduler.server.actor.protos.workerActor.{TaskSubmitRequest, WorkerMsg}
import com.github.slablock.zscheduler.server.guice.Injectors
import com.github.slablock.zscheduler.server.service.flow.FlowService
import com.github.slablock.zscheduler.server.service.job.JobService
import com.github.slablock.zscheduler.server.service.project.ProjectService
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class BrokerActor(context: ActorContext[BrokerCommand]) extends AbstractBehavior[BrokerCommand](context) {

  private val jobService = Injectors.get().instance[JobService]
  private val projectService = Injectors.get().instance[ProjectService]
  private val flowService = Injectors.get().instance[FlowService]

  implicit val executionContext: ExecutionContext = context.system.executionContext
  val worker: ActorRef[WorkerCommand] = context.spawn(Routers.group(WorkerActor.serviceKey).withRoundRobinRouting(), "worker-group")

  override def onMessage(msg: BrokerCommand): Behavior[BrokerCommand] = {
    msg match {
      case req: BrokerStatus => onBrokerStatus(req)
      case req: ProjectSubmitRequest => onProjectSubmit(req)
      case req: ProjectUpdateRequest => onProjectUpdate(req)
      case req: ProjectQueryRequest => onProjectQuery(req)
      case req: FlowSubmitRequest => onFlowSubmit(req)
      case req: FlowUpdateRequest => onFlowUpdate(req)
      case req: FlowQueryRequest => onFlowQuery(req)
      case req: JobSubmitRequest => onJobSubmit(req)
    }
  }


  def onBrokerStatus(req: BrokerStatus): Behavior[BrokerCommand] = {
    context.log.info("broker receive msg!!!")
    req.sender ! ClusterInfo("hello")
    Behaviors.same
  }


  def onJobSubmit(req: JobSubmitRequest): Behavior[BrokerCommand] = {
    jobService.addJob(req)
      .onComplete({
        case Success(jobId) =>
          worker ! TaskSubmitRequest(s"$jobId", req.jobName, req.jobType, req.content, req.user, context.self)
          req.sender ! JobSubmitResp(jobId)
        case Failure(ex) =>
          req.sender ! JobSubmitResp(-1)
      })
    Behaviors.same
  }

  def onProjectSubmit(req: ProjectSubmitRequest): Behavior[BrokerCommand] = {
    projectService.addProject(req)
      .onComplete({
        case Success(projectId) =>
          req.sender ! ProjectSubmitResp(success = true, projectId)
        case Failure(ex) =>
          req.sender ! ProjectSubmitResp(success = false, msg = ex.getLocalizedMessage)
      })
    Behaviors.same
  }

  def onProjectUpdate(req: ProjectUpdateRequest): Behavior[BrokerCommand] = {
    projectService.updateProject(req).onComplete({
      case Success(rows) => {
        if (rows > 0) {
          req.sender ! ProjectUpdateResp(success = true, req.projectId, "")
        } else {
          req.sender ! ProjectUpdateResp(success = false, req.projectId, "not found!")
        }
      }
      case Failure(ex) =>
        req.sender ! ProjectUpdateResp(success = false, req.projectId, msg = ex.getLocalizedMessage)
    })
    Behaviors.same
  }

  def onProjectQuery(req: ProjectQueryRequest): Behavior[BrokerCommand] = {
    projectService.queryProject(req.projectId).onComplete({
      case Success(None) => req.sender ! ProjectQueryResp(success = true, Option.empty, "")
      case Success(Some(ProjectRow(projectId, projectName, user, updateUser, createTime, updateTime))) =>
        req.sender ! ProjectQueryResp(success = true,
          Some(ProjectInfoEntry(projectId, projectName, user, updateUser, createTime.getTime, updateTime.getTime)), "")
      case Failure(ex) => req.sender ! ProjectQueryResp(success = false, Option.empty, ex.getLocalizedMessage)
    })
    Behaviors.same
  }

  def onFlowSubmit(req: FlowSubmitRequest): Behavior[BrokerCommand] = {
    flowService.saveFlow(req)
    Behaviors.same
  }

  def onFlowUpdate(req: FlowUpdateRequest): Behavior[BrokerCommand] = {
    Behaviors.same
  }

  def onFlowQuery(req: FlowQueryRequest): Behavior[BrokerCommand] = {
    Behaviors.same
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
