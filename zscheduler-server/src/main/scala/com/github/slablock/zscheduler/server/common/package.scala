package com.github.slablock.zscheduler.server

import akka.actor.typed.ActorRef

package object common {

  implicit class ZsTypedActorRefOps(val ref: ActorRef[_]) extends AnyVal {
    def cast[T]: ActorRef[T] = ref.asInstanceOf[ActorRef[T]]
  }
}
