package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.scaladsl.adapter.TypedActorRefOps
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.github.slablock.zscheduler.server.actor.protos.demoActorProtocol.{Command, Read, Resp}
import com.github.slablock.zscheduler.server.common.ZsTypedActorRefOps

class DemoActor(context: ActorContext[Command]) extends AbstractBehavior[Command](context){
  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case Read(id, sender) => {
        sender.cast[Resp] ! Resp(id)
        Behaviors.same
      }
    }
  }
}

object DemoActor {

  final case class Hello(data: String)

  def apply():Behavior[Command] = Behaviors.setup[Command](context => new DemoActor(context))
}