package com.github.slablock.zscheduler.server.domain;

public class ScheduleExpression {

    private Integer scheduleType;
    private String expression;

    public Integer getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(Integer scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
