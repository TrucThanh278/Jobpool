package com.ntt.JobPool.repository;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.Skill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

  List<Job> findBySkillsIn(List<Skill> skills);
}
