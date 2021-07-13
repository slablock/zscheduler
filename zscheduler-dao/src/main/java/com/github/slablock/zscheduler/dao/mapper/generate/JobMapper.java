package com.github.slablock.zscheduler.dao.mapper.generate;

import com.github.slablock.zscheduler.dao.dto.generate.Job;
import com.github.slablock.zscheduler.dao.dto.generate.JobExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JobMapper {
    long countByExample(JobExample example);

    int deleteByExample(JobExample example);

    int deleteByPrimaryKey(Long jobId);

    int insert(Job record);

    int insertSelective(Job record);

    List<Job> selectByExampleWithBLOBs(JobExample example);

    List<Job> selectByExample(JobExample example);

    Job selectByPrimaryKey(Long jobId);

    int updateByExampleSelective(@Param("record") Job record, @Param("example") JobExample example);

    int updateByExampleWithBLOBs(@Param("record") Job record, @Param("example") JobExample example);

    int updateByExample(@Param("record") Job record, @Param("example") JobExample example);

    int updateByPrimaryKeySelective(Job record);

    int updateByPrimaryKeyWithBLOBs(Job record);

    int updateByPrimaryKey(Job record);
}