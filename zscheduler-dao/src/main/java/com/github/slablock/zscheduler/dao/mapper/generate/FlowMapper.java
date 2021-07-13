package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.Flow;
import com.github.slablock.zscheduler.dao.dto.generate.FlowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FlowMapper {
    long countByExample(FlowExample example);

    int deleteByExample(FlowExample example);

    int deleteByPrimaryKey(Long flowId);

    int insert(Flow record);

    int insertSelective(Flow record);

    List<Flow> selectByExample(FlowExample example);

    Flow selectByPrimaryKey(Long flowId);

    int updateByExampleSelective(@Param("record") Flow record, @Param("example") FlowExample example);

    int updateByExample(@Param("record") Flow record, @Param("example") FlowExample example);

    int updateByPrimaryKeySelective(Flow record);

    int updateByPrimaryKey(Flow record);
}