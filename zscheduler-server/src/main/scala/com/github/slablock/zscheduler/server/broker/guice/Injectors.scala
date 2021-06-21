package com.github.slablock.zscheduler.server.broker.guice

import com.github.slablock.zscheduler.server.broker.db.guice.StorageModule
import com.google.inject.{Guice, Injector}

object Injectors {
  val injector: Injector = Guice.createInjector(new StorageModule)
}
