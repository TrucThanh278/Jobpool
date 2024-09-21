package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Skill;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO.Meta;
import com.ntt.JobPool.repository.SkillRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

  @Autowired
  private SkillRepository skillRepository;

  public boolean isNameExist(String name) {
    return this.skillRepository.existsByName(name);
  }

  public Skill createSkill(Skill s) {
    return this.skillRepository.save(s);
  }

  public Skill getSkillById(long id) {
    Optional<Skill> s = this.skillRepository.findSkillById(id);
    if (s.isPresent()) {
      return s.get();
    }
    return null;
  }

  public Skill updateSkill(Skill s) {
    return this.skillRepository.save(s);
  }

  public ResultPaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {
    Page<Skill> pageUser = this.skillRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new Meta();

    meta.setPage(pageable.getPageNumber() + 1);
    meta.setPageSize(pageable.getPageSize());

    meta.setTotal(pageUser.getTotalElements());
    meta.setPages(pageUser.getTotalPages());

    rs.setMeta(meta);
    rs.setResult(pageUser.getContent());

    return rs;
  }

  public void deleteSkill(long id) {
    Optional<Skill> skillOption = this.skillRepository.findSkillById(id);
    Skill currentSkill = skillOption.get();
    currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

    this.skillRepository.delete(currentSkill);
  }

  public Skill getSkillByName(String name) {
    return this.skillRepository.findSkillByName(name);
  }
}
