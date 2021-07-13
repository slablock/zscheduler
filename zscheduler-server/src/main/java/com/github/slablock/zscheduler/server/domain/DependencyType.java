package com.github.slablock.zscheduler.server.domain;

public enum DependencyType {

    FLOW(1, "流依赖"),
    JOB(2, "任务依赖"),
    ;

    private final int value;
    private final String description;

    DependencyType(int value, String description) {
        this.value = value;
        this.description = description;
    }


    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static DependencyType parseValue(int value) throws IllegalArgumentException {
        DependencyType[] typeList = DependencyType.values();
        for (DependencyType t : typeList) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new IllegalArgumentException("DependencyType value is invalid. value:" + value);
    }
}
