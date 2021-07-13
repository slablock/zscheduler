package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.FlowDependency;
import com.github.slablock.zscheduler.dao.dto.generate.FlowDependencyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FlowDependencyMapper {
    long countByExample(FlowDependencyExample example);

    int deleteByExample(FlowDependencyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FlowDependency record);

    int insertSelective(FlowDependency record);

    List<FlowDependency> selectByExample(FlowDependencyExample example);

    FlowDependency selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FlowDependency record, @Param("example") FlowDependencyExample example);

    int updateByExample(@Param("record") FlowDependency record, @Param("example") FlowDependencyExample example);

    int updateByPrimaryKeySelective(FlowDependency record);

    int updateByPrimaryKey(FlowDependency record);
}