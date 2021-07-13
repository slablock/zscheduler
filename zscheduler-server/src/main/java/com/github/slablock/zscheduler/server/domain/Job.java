package com.github.slablock.zscheduler.server.domain;

import java.util.List;

public class Job implements Node {

    private Long projectId;
    private Long jobId;
    private String jobName;
    private Integer jobType;
    private Integer contentType;
    private String content;
    private String user;
    private Integer priority;

    private List<DependencyExpression> dependencies;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<DependencyExpression> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyExpression> dependencies) {
        this.dependencies = dependencies;
    }
}