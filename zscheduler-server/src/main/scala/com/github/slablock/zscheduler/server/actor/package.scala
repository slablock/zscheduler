package com.github.slablock.zscheduler.server

import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.actor.typed.{ActorRef, ActorRefResolver}
import akka.serialization.Serialization
import com.github.slablock.zscheduler.server.actor.protos.clientActor.ClientMsg
import com.github.slablock.zscheduler.server.actor.protos.brokerActor.{BrokerMsg, ClientActorRef}
import com.github.slablock.zscheduler.server.actor.protos.workerActor.BrokerActorRef
import scalapb.TypeMapper


package object actor {

  val system: ActorSystem = Serialization.getCurrentTransportInformation().system
  val refResolver: ActorRefResolver = ActorRefResolver(system.toTyped)

  implicit val clientActorRefMapper: TypeMapper[ClientActorRef, ActorRef[ClientMsg]] =
    TypeMapper[ClientActorRef, ActorRef[ClientMsg]](
      refData => {
      refResolver.resolveActorRef[ClientMsg](refData.path)
    })(ref => {
      val path = refResolver.toSerializationFormat(ref)
      ClientActorRef(path)
    })

  implicit val brokerActorRefMapper: TypeMapper[BrokerActorRef, ActorRef[BrokerMsg]] =
    TypeMapper[BrokerActorRef, ActorRef[BrokerMsg]](
      refData => {
        refResolver.resolveActorRef[BrokerMsg](refData.path)
      })(ref => {
      val path = refResolver.toSerializationFormat(ref)
      BrokerActorRef(path)
    })

}
