package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.ExecutionFlow;
import com.github.slablock.zscheduler.dao.dto.generate.ExecutionFlowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExecutionFlowMapper {
    long countByExample(ExecutionFlowExample example);

    int deleteByExample(ExecutionFlowExample example);

    int deleteByPrimaryKey(Long execId);

    int insert(ExecutionFlow record);

    int insertSelective(ExecutionFlow record);

    List<ExecutionFlow> selectByExample(ExecutionFlowExample example);

    ExecutionFlow selectByPrimaryKey(Long execId);

    int updateByExampleSelective(@Param("record") ExecutionFlow record, @Param("example") ExecutionFlowExample example);

    int updateByExample(@Param("record") ExecutionFlow record, @Param("example") ExecutionFlowExample example);

    int updateByPrimaryKeySelective(ExecutionFlow record);

    int updateByPrimaryKey(ExecutionFlow record);
}