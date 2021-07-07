package com.github.slablock.zscheduler.server

import akka.actor.{ActorSystem, typed}
import akka.actor.typed.scaladsl.adapter.ClassicActorSystemOps
import akka.actor.typed.{ActorRef, ActorRefResolver}
import akka.serialization.Serialization
import org.apache.commons.lang3.StringUtils
import scalapb.TypeMapper

package object actor {

  val system: ActorSystem = Serialization.getCurrentTransportInformation().system
  val typedSystem: typed.ActorSystem[Nothing] = system.toTyped
  val refResolver: ActorRefResolver = ActorRefResolver(typedSystem)

  implicit def actorRefMapper[T]: TypeMapper[String, ActorRef[T]] = {
    TypeMapper[String, ActorRef[T]] { str =>
      if (StringUtils.isBlank(str)) typedSystem.deadLetters[T]
      else refResolver.resolveActorRef[T](str)
    } { ref =>
      ref.path.elements match {
        case List("deadLetters") => "" // resolver.toSerializationFormat(ActorSystemUtils.system.deadLetters[T])
        case _                   => refResolver.toSerializationFormat(ref)
      }
    }
  }
}
