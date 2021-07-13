package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.JobDependency;
import com.github.slablock.zscheduler.dao.dto.generate.JobDependencyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JobDependencyMapper {
    long countByExample(JobDependencyExample example);

    int deleteByExample(JobDependencyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(JobDependency record);

    int insertSelective(JobDependency record);

    List<JobDependency> selectByExample(JobDependencyExample example);

    JobDependency selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") JobDependency record, @Param("example") JobDependencyExample example);

    int updateByExample(@Param("record") JobDependency record, @Param("example") JobDependencyExample example);

    int updateByPrimaryKeySelective(JobDependency record);

    int updateByPrimaryKey(JobDependency record);
}