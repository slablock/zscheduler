package com.github.slablock.zscheduler.server.domain;

public enum OperationType {

    ADD(1),         //追加
    EDIT(2),        //修改
    DELETE(3);      //删除

    private final int value;

    OperationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
