package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerMsg, BrokerStatus}
import com.github.slablock.zscheduler.server.actor.protos.clientActor.ClusterInfo

class BrokerActor(context: ActorContext[BrokerMsg]) extends AbstractBehavior[BrokerMsg](context) {
  override def onMessage(msg: BrokerMsg): Behavior[BrokerMsg] = {
    msg match {
      case BrokerStatus(sender) => {
        context.log.info("broker receive msg!!!")
        sender ! ClusterInfo("hello")
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
