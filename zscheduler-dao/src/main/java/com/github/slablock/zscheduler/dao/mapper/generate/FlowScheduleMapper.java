package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.FlowSchedule;
import com.github.slablock.zscheduler.dao.dto.generate.FlowScheduleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FlowScheduleMapper {
    long countByExample(FlowScheduleExample example);

    int deleteByExample(FlowScheduleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FlowSchedule record);

    int insertSelective(FlowSchedule record);

    List<FlowSchedule> selectByExample(FlowScheduleExample example);

    FlowSchedule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FlowSchedule record, @Param("example") FlowScheduleExample example);

    int updateByExample(@Param("record") FlowSchedule record, @Param("example") FlowScheduleExample example);

    int updateByPrimaryKeySelective(FlowSchedule record);

    int updateByPrimaryKey(FlowSchedule record);
}