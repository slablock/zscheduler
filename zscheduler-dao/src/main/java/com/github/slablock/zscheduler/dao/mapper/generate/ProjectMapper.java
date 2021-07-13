package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.Project;
import com.github.slablock.zscheduler.dao.dto.generate.ProjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProjectMapper {
    long countByExample(ProjectExample example);

    int deleteByExample(ProjectExample example);

    int deleteByPrimaryKey(Long projectId);

    int insert(Project record);

    int insertSelective(Project record);

    List<Project> selectByExample(ProjectExample example);

    Project selectByPrimaryKey(Long projectId);

    int updateByExampleSelective(@Param("record") Project record, @Param("example") ProjectExample example);

    int updateByExample(@Param("record") Project record, @Param("example") ProjectExample example);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);
}