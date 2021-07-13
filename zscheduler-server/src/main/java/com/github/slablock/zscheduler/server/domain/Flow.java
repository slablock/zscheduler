package com.github.slablock.zscheduler.server.domain;

import java.util.List;

public class Flow implements Node {

    private Long projectId;
    private Long flowId;
    private String flowName;
    private List<Node> nodes;

    private List<DependencyExpression> dependencies;
    private List<ScheduleExpression> schedules;

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

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<DependencyExpression> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyExpression> dependencies) {
        this.dependencies = dependencies;
    }

    public List<ScheduleExpression> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleExpression> schedules) {
        this.schedules = schedules;
    }
}
