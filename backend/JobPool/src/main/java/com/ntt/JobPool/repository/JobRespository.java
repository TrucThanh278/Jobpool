package com.ntt.JobPool.repository;

import com.ntt.JobPool.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRespository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

}
