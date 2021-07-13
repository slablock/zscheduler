package com.github.slablock.zscheduler.dao.dto.generate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExecutionFlowExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExecutionFlowExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andExecIdIsNull() {
            addCriterion("execId is null");
            return (Criteria) this;
        }

        public Criteria andExecIdIsNotNull() {
            addCriterion("execId is not null");
            return (Criteria) this;
        }

        public Criteria andExecIdEqualTo(Long value) {
            addCriterion("execId =", value, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdNotEqualTo(Long value) {
            addCriterion("execId <>", value, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdGreaterThan(Long value) {
            addCriterion("execId >", value, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdGreaterThanOrEqualTo(Long value) {
            addCriterion("execId >=", value, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdLessThan(Long value) {
            addCriterion("execId <", value, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdLessThanOrEqualTo(Long value) {
            addCriterion("execId <=", value, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdIn(List<Long> values) {
            addCriterion("execId in", values, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdNotIn(List<Long> values) {
            addCriterion("execId not in", values, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdBetween(Long value1, Long value2) {
            addCriterion("execId between", value1, value2, "execId");
            return (Criteria) this;
        }

        public Criteria andExecIdNotBetween(Long value1, Long value2) {
            addCriterion("execId not between", value1, value2, "execId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdIsNull() {
            addCriterion("attemptId is null");
            return (Criteria) this;
        }

        public Criteria andAttemptIdIsNotNull() {
            addCriterion("attemptId is not null");
            return (Criteria) this;
        }

        public Criteria andAttemptIdEqualTo(Integer value) {
            addCriterion("attemptId =", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdNotEqualTo(Integer value) {
            addCriterion("attemptId <>", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdGreaterThan(Integer value) {
            addCriterion("attemptId >", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("attemptId >=", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdLessThan(Integer value) {
            addCriterion("attemptId <", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdLessThanOrEqualTo(Integer value) {
            addCriterion("attemptId <=", value, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdIn(List<Integer> values) {
            addCriterion("attemptId in", values, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdNotIn(List<Integer> values) {
            addCriterion("attemptId not in", values, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdBetween(Integer value1, Integer value2) {
            addCriterion("attemptId between", value1, value2, "attemptId");
            return (Criteria) this;
        }

        public Criteria andAttemptIdNotBetween(Integer value1, Integer value2) {
            addCriterion("attemptId not between", value1, value2, "attemptId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNull() {
            addCriterion("projectId is null");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNotNull() {
            addCriterion("projectId is not null");
            return (Criteria) this;
        }

        public Criteria andProjectIdEqualTo(Long value) {
            addCriterion("projectId =", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotEqualTo(Long value) {
            addCriterion("projectId <>", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThan(Long value) {
            addCriterion("projectId >", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThanOrEqualTo(Long value) {
            addCriterion("projectId >=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThan(Long value) {
            addCriterion("projectId <", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThanOrEqualTo(Long value) {
            addCriterion("projectId <=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIn(List<Long> values) {
            addCriterion("projectId in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotIn(List<Long> values) {
            addCriterion("projectId not in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdBetween(Long value1, Long value2) {
            addCriterion("projectId between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotBetween(Long value1, Long value2) {
            addCriterion("projectId not between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andFlowIdIsNull() {
            addCriterion("flowId is null");
            return (Criteria) this;
        }

        public Criteria andFlowIdIsNotNull() {
            addCriterion("flowId is not null");
            return (Criteria) this;
        }

        public Criteria andFlowIdEqualTo(Long value) {
            addCriterion("flowId =", value, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdNotEqualTo(Long value) {
            addCriterion("flowId <>", value, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdGreaterThan(Long value) {
            addCriterion("flowId >", value, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdGreaterThanOrEqualTo(Long value) {
            addCriterion("flowId >=", value, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdLessThan(Long value) {
            addCriterion("flowId <", value, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdLessThanOrEqualTo(Long value) {
            addCriterion("flowId <=", value, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdIn(List<Long> values) {
            addCriterion("flowId in", values, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdNotIn(List<Long> values) {
            addCriterion("flowId not in", values, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdBetween(Long value1, Long value2) {
            addCriterion("flowId between", value1, value2, "flowId");
            return (Criteria) this;
        }

        public Criteria andFlowIdNotBetween(Long value1, Long value2) {
            addCriterion("flowId not between", value1, value2, "flowId");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeIsNull() {
            addCriterion("scheduleTime is null");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeIsNotNull() {
            addCriterion("scheduleTime is not null");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeEqualTo(Date value) {
            addCriterion("scheduleTime =", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeNotEqualTo(Date value) {
            addCriterion("scheduleTime <>", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeGreaterThan(Date value) {
            addCriterion("scheduleTime >", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("scheduleTime >=", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeLessThan(Date value) {
            addCriterion("scheduleTime <", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeLessThanOrEqualTo(Date value) {
            addCriterion("scheduleTime <=", value, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeIn(List<Date> values) {
            addCriterion("scheduleTime in", values, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeNotIn(List<Date> values) {
            addCriterion("scheduleTime not in", values, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeBetween(Date value1, Date value2) {
            addCriterion("scheduleTime between", value1, value2, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andScheduleTimeNotBetween(Date value1, Date value2) {
            addCriterion("scheduleTime not between", value1, value2, "scheduleTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeIsNull() {
            addCriterion("dataTime is null");
            return (Criteria) this;
        }

        public Criteria andDataTimeIsNotNull() {
            addCriterion("dataTime is not null");
            return (Criteria) this;
        }

        public Criteria andDataTimeEqualTo(Date value) {
            addCriterion("dataTime =", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeNotEqualTo(Date value) {
            addCriterion("dataTime <>", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeGreaterThan(Date value) {
            addCriterion("dataTime >", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("dataTime >=", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeLessThan(Date value) {
            addCriterion("dataTime <", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeLessThanOrEqualTo(Date value) {
            addCriterion("dataTime <=", value, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeIn(List<Date> values) {
            addCriterion("dataTime in", values, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeNotIn(List<Date> values) {
            addCriterion("dataTime not in", values, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeBetween(Date value1, Date value2) {
            addCriterion("dataTime between", value1, value2, "dataTime");
            return (Criteria) this;
        }

        public Criteria andDataTimeNotBetween(Date value1, Date value2) {
            addCriterion("dataTime not between", value1, value2, "dataTime");
            return (Criteria) this;
        }

        public Criteria andProgressIsNull() {
            addCriterion("progress is null");
            return (Criteria) this;
        }

        public Criteria andProgressIsNotNull() {
            addCriterion("progress is not null");
            return (Criteria) this;
        }

        public Criteria andProgressEqualTo(Float value) {
            addCriterion("progress =", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotEqualTo(Float value) {
            addCriterion("progress <>", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThan(Float value) {
            addCriterion("progress >", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThanOrEqualTo(Float value) {
            addCriterion("progress >=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThan(Float value) {
            addCriterion("progress <", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThanOrEqualTo(Float value) {
            addCriterion("progress <=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressIn(List<Float> values) {
            addCriterion("progress in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotIn(List<Float> values) {
            addCriterion("progress not in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressBetween(Float value1, Float value2) {
            addCriterion("progress between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotBetween(Float value1, Float value2) {
            addCriterion("progress not between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andExecuteUserIsNull() {
            addCriterion("executeUser is null");
            return (Criteria) this;
        }

        public Criteria andExecuteUserIsNotNull() {
            addCriterion("executeUser is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteUserEqualTo(String value) {
            addCriterion("executeUser =", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotEqualTo(String value) {
            addCriterion("executeUser <>", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserGreaterThan(String value) {
            addCriterion("executeUser >", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserGreaterThanOrEqualTo(String value) {
            addCriterion("executeUser >=", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserLessThan(String value) {
            addCriterion("executeUser <", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserLessThanOrEqualTo(String value) {
            addCriterion("executeUser <=", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserLike(String value) {
            addCriterion("executeUser like", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotLike(String value) {
            addCriterion("executeUser not like", value, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserIn(List<String> values) {
            addCriterion("executeUser in", values, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotIn(List<String> values) {
            addCriterion("executeUser not in", values, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserBetween(String value1, String value2) {
            addCriterion("executeUser between", value1, value2, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteUserNotBetween(String value1, String value2) {
            addCriterion("executeUser not between", value1, value2, "executeUser");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIsNull() {
            addCriterion("executeStartTime is null");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIsNotNull() {
            addCriterion("executeStartTime is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeEqualTo(Date value) {
            addCriterion("executeStartTime =", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotEqualTo(Date value) {
            addCriterion("executeStartTime <>", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeGreaterThan(Date value) {
            addCriterion("executeStartTime >", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("executeStartTime >=", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeLessThan(Date value) {
            addCriterion("executeStartTime <", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("executeStartTime <=", value, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeIn(List<Date> values) {
            addCriterion("executeStartTime in", values, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotIn(List<Date> values) {
            addCriterion("executeStartTime not in", values, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeBetween(Date value1, Date value2) {
            addCriterion("executeStartTime between", value1, value2, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("executeStartTime not between", value1, value2, "executeStartTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeIsNull() {
            addCriterion("executeEndTime is null");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeIsNotNull() {
            addCriterion("executeEndTime is not null");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeEqualTo(Date value) {
            addCriterion("executeEndTime =", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeNotEqualTo(Date value) {
            addCriterion("executeEndTime <>", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeGreaterThan(Date value) {
            addCriterion("executeEndTime >", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("executeEndTime >=", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeLessThan(Date value) {
            addCriterion("executeEndTime <", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("executeEndTime <=", value, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeIn(List<Date> values) {
            addCriterion("executeEndTime in", values, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeNotIn(List<Date> values) {
            addCriterion("executeEndTime not in", values, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeBetween(Date value1, Date value2) {
            addCriterion("executeEndTime between", value1, value2, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andExecuteEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("executeEndTime not between", value1, value2, "executeEndTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("updateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("updateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("updateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("updateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("updateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("updateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("updateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("updateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("updateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}