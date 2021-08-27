package com.github.slablock.zscheduler.server.domain

import com.github.slablock.zcheduler.core.domain.ScheduleExpression
import com.github.slablock.zscheduler.dao.Tables.FlowRow

class FlowEntry(val flow: FlowRow, val schedules: Map[Long, ScheduleExpression]) {

}