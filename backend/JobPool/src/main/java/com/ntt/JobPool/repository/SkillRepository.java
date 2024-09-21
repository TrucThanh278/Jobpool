package com.ntt.JobPool.repository;

import com.ntt.JobPool.domain.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>,
    JpaSpecificationExecutor<Skill> {

  boolean existsByName(String name);

  Optional<Skill> findSkillById(long id);

  Skill findSkillByName(String name);

  List<Skill> findSkillByIdIn(List<Long> ids);
}
