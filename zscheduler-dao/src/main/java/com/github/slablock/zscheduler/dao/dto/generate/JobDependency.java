package com.github.slablock.zscheduler.dao.dto.generate;

import java.util.Date;

public class JobDependency {
    private Long id;

    private Long projectId;

    private Long flowId;

    private Long jobId;

    private Long preProjectId;

    private Long preFlowId;

    private Long preJobId;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getPreProjectId() {
        return preProjectId;
    }

    public void setPreProjectId(Long preProjectId) {
        this.preProjectId = preProjectId;
    }

    public Long getPreFlowId() {
        return preFlowId;
    }

    public void setPreFlowId(Long preFlowId) {
        this.preFlowId = preFlowId;
    }

    public Long getPreJobId() {
        return preJobId;
    }

    public void setPreJobId(Long preJobId) {
        this.preJobId = preJobId;
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