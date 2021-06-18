package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.{ActorRef, Behavior, Scheduler}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.util.Timeout
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.BrokerStatus
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo, Query}

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContextExecutor

class ClientActor(context: ActorContext[ClientMsg]) extends AbstractBehavior[ClientMsg](context) {

  implicit val timeout: Timeout = Timeout(10, TimeUnit.SECONDS)
  implicit val scheduler: Scheduler = context.system.scheduler
  implicit val executionContext: ExecutionContextExecutor = context.system.executionContext

  override def onMessage(msg: ClientMsg): Behavior[ClientMsg] = {
    msg match {
      case msg: Query =>
        context.log.info("client receive msg: {}", msg.id)
        context.system.receptionist.ask[Receptionist.Listing](Receptionist.Find(BrokerActor.serviceKey)).map {
          listing =>
            if (listing.isForKey(BrokerActor.serviceKey)) {
              listing.serviceInstances(BrokerActor.serviceKey).head !BrokerStatus(context.self)
            }
        }
        Behaviors.same
      case msg: ClusterInfo =>
        context.log.info("ClusterInfo {}", msg.data)
        Behaviors.same
    }
  }
}

object ClientActor {

  sealed trait Command

  def apply(): Behavior[ClientMsg] = Behaviors.setup[ClientMsg](context => {
    context.spawn(ClusterListenerActor(), "lister")
    new ClientActor(context)
  })

}
