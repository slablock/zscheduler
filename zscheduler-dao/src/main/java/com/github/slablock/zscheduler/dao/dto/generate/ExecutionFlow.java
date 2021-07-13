package com.github.slablock.zscheduler.dao.dto.generate;

import java.util.Date;

public class ExecutionFlow {
    private Long execId;

    private Integer attemptId;

    private Long projectId;

    private Long flowId;

    private Date scheduleTime;

    private Date dataTime;

    private Float progress;

    private Integer status;

    private String executeUser;

    private Date executeStartTime;

    private Date executeEndTime;

    private Date createTime;

    private Date updateTime;

    public Long getExecId() {
        return execId;
    }

    public void setExecId(Long execId) {
        this.execId = execId;
    }

    public Integer getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExecuteUser() {
        return executeUser;
    }

    public void setExecuteUser(String executeUser) {
        this.executeUser = executeUser;
    }

    public Date getExecuteStartTime() {
        return executeStartTime;
    }

    public void setExecuteStartTime(Date executeStartTime) {
        this.executeStartTime = executeStartTime;
    }

    public Date getExecuteEndTime() {
        return executeEndTime;
    }

    public void setExecuteEndTime(Date executeEndTime) {
        this.executeEndTime = executeEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}