package com.github.slablock.zscheduler.server.domain;

public enum ScheduleType {

    CRON(1, "Cron表达式"),
    FIXED_RATE(2, "固定频率"),
    FIXED_DELAY(3, "固定时延"),
    ISO8601(4, "ISO8601"),
    ;

    private final int value;
    private final String description;

    ScheduleType(int value, String description) {
        this.value = value;
        this.description = description;
    }


    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ScheduleType parseValue(int value) throws IllegalArgumentException {
        ScheduleType[] typeList = ScheduleType.values();
        for (ScheduleType t : typeList) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new IllegalArgumentException("ScheduleType value is invalid. value:" + value);
    }
}
