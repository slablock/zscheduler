package com.github.slablock.zscheduler.server

import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.actor.typed.{ActorRef, ActorRefResolver}
import akka.serialization.Serialization
import com.github.slablock.zscheduler.server.actor.protos.clientActor.{ClientMsg, ClusterInfo}
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{ActorRefData=>BrokerRef}
import com.github.slablock.zscheduler.server.actor.protos.demoActorProtocol.{ActorRefData, Resp}
import scalapb.TypeMapper

package object actor {

  val system = Serialization.getCurrentTransportInformation().system
  val refResolver = ActorRefResolver(system.toTyped)

  implicit val tm =
    TypeMapper[ActorRefData, ActorRef[Any]](refData => {
      refResolver.resolveActorRef[Any](refData.path)
    })(ref => {
      val path = refResolver.toSerializationFormat(ref)
      ActorRefData(path)
    })

  implicit val client =
    TypeMapper[BrokerRef, ActorRef[ClusterInfo]](refData => {
      refResolver.resolveActorRef[ClusterInfo](refData.path)
    })(ref => {
      val path = refResolver.toSerializationFormat(ref)
      BrokerRef(path)
    })

}
