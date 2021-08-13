package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, Routers}
import com.github.slablock.zscheduler.dao.Tables.{JobDependencyRow, JobRow, ProjectRow}
import com.github.slablock.zscheduler.server.actor.BrokerActor.BrokerCommand
import com.github.slablock.zscheduler.server.actor.WorkerActor.WorkerCommand
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerStatus, FlowQueryRequest, FlowSubmitRequest, FlowUpdateRequest, JobQueryRequest, JobSubmitRequest, JobUpdateRequest, ProjectQueryRequest, ProjectSubmitRequest, ProjectUpdateRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, DependencyEntry, FlowInfoEntry, FlowQueryResp, FlowSubmitResp, FlowUpdateResp, JobInfoEntry, JobQueryResp, JobSubmitResp, JobUpdateResp, ProjectInfoEntry, ProjectQueryResp, ProjectSubmitResp, ProjectUpdateResp, ScheduleEntry}
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
      case req: JobQueryRequest => onJobQuery(req)
      case req: JobUpdateRequest => onJobUpdate(req)
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
          req.sender ! JobSubmitResp(success = true, jobId)
        case Failure(ex) =>
          req.sender ! JobSubmitResp(success = false, msg = ex.getLocalizedMessage)
      })
    Behaviors.same
  }

  def onJobQuery(req: JobQueryRequest): Behavior[BrokerCommand] = {
    jobService.queryJob(req.jobId).onComplete({
      case Success((None, _)) => req.sender ! JobQueryResp(success = true, Option.empty, msg = "not found")
      case Success((Some(jobRow), jobDependencies)) => {
        val dependencies = jobDependencies
          .map(d=>DependencyEntry(d.id, d.preProjectId,d.preFlowId, d.preJobId, d.rangeExpression, d.offsetExpression,
            d.createTime.getTime, d.updateTime.getTime))
        req.sender ! JobQueryResp(success = true, Option.apply(JobInfoEntry(jobRow.jobId, jobRow.projectId, jobRow.flowId,
          jobRow.jobName, jobRow.jobType, jobRow.contentType, jobRow.content, jobRow.params, jobRow.priority,
          jobRow.user, jobRow.updateUser, jobRow.createTime.getTime, jobRow.updateTime.getTime, dependencies)))
      }
    })
    Behaviors.same
  }

  def onJobUpdate(req: JobUpdateRequest): Behavior[BrokerCommand] = {
    jobService.modifyJob(req).onComplete({
      case Success(rows) => {
        if (rows > 0) {
          req.sender ! JobUpdateResp(success = true, req.jobId)
        } else {
          req.sender ! JobUpdateResp(success = false, req.jobId, msg = "not found")
        }
      }
      case Failure(ex) => req.sender ! JobUpdateResp(success = false, req.jobId, ex.getLocalizedMessage)
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
    flowService.saveFlow(req).onComplete({
      case Success(flowId) => req.sender ! FlowSubmitResp(success = true, flowId)
      case Failure(ex) => req.sender ! FlowSubmitResp(success = false, -1, ex.getLocalizedMessage)
    })
    Behaviors.same
  }

  def onFlowUpdate(req: FlowUpdateRequest): Behavior[BrokerCommand] = {
    flowService.modifyFlow(req).onComplete({
      case Success(rows) => {
        if (rows > 0) {
          req.sender ! FlowUpdateResp(success = true, req.flowId, "")
        } else {
          req.sender ! FlowUpdateResp(success = false, req.flowId, "not found!")
        }
      }
      case Failure(ex) =>
        req.sender ! FlowUpdateResp(success = false, req.flowId, msg = ex.getLocalizedMessage)
    })
    Behaviors.same
  }


  def onFlowQuery(req: FlowQueryRequest): Behavior[BrokerCommand] = {
    flowService.queryFlow(req.flowId).onComplete(({
      case Success((None, _, _)) => req.sender ! FlowQueryResp(success = true, Option.empty, msg = "not found")
      case Success((Some(flow), flowDependencies, flowSchedules)) => {
        val dependencyEntries = flowDependencies.map(d=>DependencyEntry(d.id,
          d.preProjectId, d.preFlowId, d.preJobId,
          d.rangeExpression, d.offsetExpression, d.createTime.getTime, d.updateTime.getTime))

        val schedules = flowSchedules.map(s=>ScheduleEntry(s.id, s.scheduleType, s.expression,
          s.createTime.getTime, s.updateTime.getTime))

        val re = FlowInfoEntry(flow.flowId, flow.projectId, flow.flowName, flow.user,
          flow.updateUser, flow.createTime.getTime, flow.updateTime.getTime,
          dependencyEntries, schedules)

        req.sender ! FlowQueryResp(success = true, Option.apply(re))
      }
      case Failure(ex) => req.sender ! FlowQueryResp(success = false, msg = ex.getLocalizedMessage)
    }))
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
