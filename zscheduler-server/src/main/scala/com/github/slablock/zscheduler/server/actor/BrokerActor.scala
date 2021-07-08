package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors, Routers}
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerMsg, BrokerStatus, JobSubmitRequest}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClusterInfo, JobSubmitResp}
import com.github.slablock.zscheduler.server.actor.protos.workerActor.{TaskSubmitRequest, WorkerMsg}
import com.github.slablock.zscheduler.server.broker.db.Job
import com.github.slablock.zscheduler.server.broker.db.job.JobService
import com.github.slablock.zscheduler.server.guice.Injectors
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class BrokerActor(context: ActorContext[BrokerMsg]) extends AbstractBehavior[BrokerMsg](context) {

  private val jobService = Injectors.get().instance[JobService]
  implicit val executionContext: ExecutionContext = context.system.executionContext
  val worker: ActorRef[WorkerMsg] = context.spawn(Routers.group(WorkerActor.serviceKey).withRoundRobinRouting(), "worker-group")

  override def onMessage(msg: BrokerMsg): Behavior[BrokerMsg] = {
    msg match {
      case BrokerStatus(client) => {
        context.log.info("broker receive msg!!!")
        client ! ClusterInfo("hello")
        Behaviors.same
      }
      case JobSubmitRequest(jobName, jobType, contentType, content, user, priority, Seq(), Seq(), sender) => {
        jobService.addJob(Job(0, jobName, jobType, content, user))
          .onComplete({
            case Success(Job(jobId, _, _, _, _)) =>
              worker ! TaskSubmitRequest(s"$jobId", jobName, jobType, content, user, context.self)
              sender ! JobSubmitResp(jobId)
            case Failure(ex) =>
              sender ! JobSubmitResp(-1)
          })
        Behaviors.same
      }
    }
  }
}

object BrokerActor {
  val serviceKey: ServiceKey[BrokerMsg] = ServiceKey[BrokerMsg]("broker")

  def apply(): Behavior[BrokerMsg] = Behaviors.setup[BrokerMsg](context => {
    context.system.receptionist ! Receptionist.Register(serviceKey, context.self)
    new BrokerActor(context)
  })
}
