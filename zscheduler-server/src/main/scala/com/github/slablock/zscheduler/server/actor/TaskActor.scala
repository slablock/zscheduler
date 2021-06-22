package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.github.slablock.zscheduler.server.actor.TaskActor.{Command, TaskSuccess}
import org.slf4j.{Logger, LoggerFactory}

class TaskActor(context: ActorContext[Command]) extends AbstractBehavior[Command](context) {

  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[TaskActor])

  def run(): Unit = {
    LOGGER.info("task run start ")
    Thread.sleep(1000)
    LOGGER.info("task run stop ")
    context.self ! TaskSuccess()
  }

  run()

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case TaskSuccess() => {
        context.log.info("TaskActor stopped ")
        Behaviors.stopped
      }
    }
  }

}

object TaskActor {

  trait Command

  case class TaskSuccess() extends Command

  def apply(): Behavior[Command] = Behaviors.setup[Command](context => {
    new TaskActor(context)
  })
}
