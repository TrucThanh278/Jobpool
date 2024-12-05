package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Skill;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SkillService {

    public boolean isNameExist(String name);

    public Skill createSkill(Skill s);

    public Skill getSkillById(long id);

    public Skill updateSkill(Skill s);

    public ResultPaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable);

    public void deleteSkill(long id);

    public Skill getSkillByName(String name);
}
