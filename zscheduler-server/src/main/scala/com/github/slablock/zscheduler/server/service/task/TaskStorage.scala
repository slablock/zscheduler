package com.github.slablock.zscheduler.server.service.task

import com.github.slablock.zscheduler.server.service.ZSDbComponent
import com.google.inject.Inject

trait TaskStorage {

}

private[service] class JdbcTaskStorage @Inject()(val dbComponent: ZSDbComponent) extends TaskStorage {

}
