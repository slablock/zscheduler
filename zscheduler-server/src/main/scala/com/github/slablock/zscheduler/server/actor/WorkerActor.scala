package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.github.slablock.zscheduler.server.actor.WorkerActor.WorkerCommand
import com.github.slablock.zscheduler.server.actor.protos.workerActor.TaskSubmitRequest

import scala.concurrent.ExecutionContext

class WorkerActor(context: ActorContext[WorkerCommand]) extends AbstractBehavior[WorkerCommand](context) {

  implicit val executionContext: ExecutionContext = context.system.executionContext

  override def onMessage(msg: WorkerCommand): Behavior[WorkerCommand] = {
    msg match {
      case TaskSubmitRequest(fullId, taskName, taskType, content, user, sender) => {
        context.log.info("receive TaskSubmitRequest {}", fullId)
        context.spawn(TaskActor(), fullId)
        Behaviors.same
      }
    }
  }
}


object WorkerActor {

  trait WorkerCommand

  val serviceKey: ServiceKey[WorkerCommand] = ServiceKey[WorkerCommand]("worker")
  def apply(): Behavior[WorkerCommand] = Behaviors.setup[WorkerCommand](context => {
    context.system.receptionist ! Receptionist.Register(serviceKey, context.self)
    new WorkerActor(context)
  })
}
