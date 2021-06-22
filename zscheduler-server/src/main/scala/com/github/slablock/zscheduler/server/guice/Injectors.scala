package com.github.slablock.zscheduler.server.guice

import com.google.inject.Injector

object Injectors {

  private var self: Option[Injector] = None

  def bind(baseInjector: Injector): Unit = {
    self = Option.apply(baseInjector)
  }

  def get(): Injector = {
    self match {
      case Some(value) => value
      case None => throw new IllegalStateException()
    }
  }
}
