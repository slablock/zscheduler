package com.github.slablock.zscheduler.server.domain;

public class DependencyExpression {

    private Long parentProjectId;
    private Long parentFlowId;
    private Long parentJobId;
    private Integer dependencyType;
    private String rangeExpression;
    private String offsetExpression;


    public Long getParentProjectId() {
        return parentProjectId;
    }

    public void setParentProjectId(Long parentProjectId) {
        this.parentProjectId = parentProjectId;
    }

    public Long getParentFlowId() {
        return parentFlowId;
    }

    public void setParentFlowId(Long parentFlowId) {
        this.parentFlowId = parentFlowId;
    }

    public Long getParentJobId() {
        return parentJobId;
    }

    public void setParentJobId(Long parentJobId) {
        this.parentJobId = parentJobId;
    }

    public Integer getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(Integer dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getRangeExpression() {
        return rangeExpression;
    }

    public void setRangeExpression(String rangeExpression) {
        this.rangeExpression = rangeExpression;
    }

    public String getOffsetExpression() {
        return offsetExpression;
    }

    public void setOffsetExpression(String offsetExpression) {
        this.offsetExpression = offsetExpression;
    }
}