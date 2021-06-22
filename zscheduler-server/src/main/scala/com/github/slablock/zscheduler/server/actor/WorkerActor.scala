package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.github.slablock.zscheduler.server.actor.protos.workerActor.{TaskSubmitRequest, WorkerMsg}

import scala.concurrent.ExecutionContext

class WorkerActor(context: ActorContext[WorkerMsg]) extends AbstractBehavior[WorkerMsg](context) {

  implicit val executionContext: ExecutionContext = context.system.executionContext

  override def onMessage(msg: WorkerMsg): Behavior[WorkerMsg] = {
    msg match {
      case TaskSubmitRequest(fullId, taskName, taskType, content, user, sender) => {
        context.log.info("receive TaskSubmitRequest {}", fullId)
        
        Behaviors.same
      }
    }
  }
}


object WorkerActor {
  val serviceKey: ServiceKey[WorkerMsg] = ServiceKey[WorkerMsg]("worker")
  def apply(): Behavior[WorkerMsg] = Behaviors.setup[WorkerMsg](context => {
    context.system.receptionist ! Receptionist.Register(serviceKey, context.self)
    new WorkerActor(context)
  })
}
