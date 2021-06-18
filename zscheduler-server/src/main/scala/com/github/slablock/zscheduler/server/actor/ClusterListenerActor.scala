package com.github.slablock.zscheduler.server.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.cluster.ClusterEvent.ClusterDomainEvent
import akka.cluster.typed.Cluster
import akka.cluster.{ClusterEvent, Member, MemberStatus}
import akka.cluster.typed.Subscribe
import com.github.slablock.zscheduler.server.common.ClusterRole

import java.util.stream.StreamSupport

class ClusterListenerActor(context: ActorContext[ClusterDomainEvent]) extends AbstractBehavior[ClusterDomainEvent](context) {

  val cluster: Cluster = Cluster.get(context.system)
  cluster.subscriptions ! Subscribe.create(context.self, classOf[ClusterEvent.ClusterDomainEvent])


  override def onMessage(msg: ClusterDomainEvent): Behavior[ClusterDomainEvent] = {
    msg match {
      case _ =>
        context.log.info("{} sent to {}", msg, cluster.selfMember)
        val clusterState = cluster.state
        val unreachable = clusterState.getUnreachable
        val broker = StreamSupport
          .stream(clusterState.getMembers.spliterator, false)
          .filter((member: Member) => member.status == MemberStatus.up)
          .filter((member: Member) => !unreachable.contains(member))
          .filter((member: Member) => member.hasRole(ClusterRole.BROKER)).findAny()
        if (broker.isPresent) {
          context.log.info("broker find {}", broker.get())
        }
        Behaviors.same
    }
  }

}


object ClusterListenerActor {
  def apply(): Behavior[ClusterDomainEvent] = Behaviors.setup[ClusterDomainEvent](context => {
    new ClusterListenerActor(context)
  })
}
