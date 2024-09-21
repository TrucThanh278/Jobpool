package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.Skill;
import com.ntt.JobPool.domain.response.job.ResCreateJobDTO;
import com.ntt.JobPool.domain.response.job.ResUpdateJobDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO.Meta;
import com.ntt.JobPool.repository.JobRespository;
import com.ntt.JobPool.repository.SkillRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class JobService {

  @Autowired
  private JobRespository jobRespository;

  @Autowired
  private SkillRepository skillRepository;

  public ResCreateJobDTO createJob(Job job) {
    if (job.getSkills() != null) {
      List<Long> reqSkills = job.getSkills().stream().map(x -> x.getId())
          .collect(Collectors.toList());

      List<Skill> dbSkills = skillRepository.findSkillByIdIn(reqSkills);
      job.setSkills(dbSkills);
    }

    Job currentJob = this.jobRespository.save(job);

    ResCreateJobDTO resJob = new ResCreateJobDTO();
    resJob.setId(currentJob.getId());
    resJob.setName(currentJob.getName());
    resJob.setSalary(currentJob.getSalary());
    resJob.setQuantity(currentJob.getQuantity());
    resJob.setLocation(currentJob.getLocation());
    resJob.setLevel(currentJob.getLevel());
    resJob.setStartDate(currentJob.getStartDate());
    resJob.setEndDate(currentJob.getEndDate());
    resJob.setActive(currentJob.isActive());
    resJob.setCreatedAt(currentJob.getCreatedAt());
    resJob.setCreatedBy(currentJob.getCreatedBy());

    if (currentJob.getSkills() != null) {
      List<String> skills = currentJob.getSkills().stream().map(skill -> skill.getName()).collect(
          Collectors.toList());
      resJob.setSkills(skills);
    }

    return resJob;
  }

  public Optional<Job> getJobById(long id) {
    return this.jobRespository.findById(id);
  }

  public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
    Page<Job> pageJob = this.jobRespository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new Meta();

    meta.setPage(pageable.getPageNumber() + 1);
    meta.setPageSize(pageable.getPageSize());

    meta.setPages(pageJob.getTotalPages());
    meta.setTotal(pageJob.getTotalElements());

    rs.setMeta(meta);
    rs.setResult(pageJob.getContent());
    return rs;
  }

  public ResUpdateJobDTO updateJob(Job job) {
    if (job.getSkills() != null) {
      List<Long> reqSkills = job.getSkills().stream().map(j -> j.getId())
          .collect(Collectors.toList());
      List<Skill> s = this.skillRepository.findSkillByIdIn(reqSkills);
      job.setSkills(s);
    }

    Job currentJob = this.jobRespository.save(job);

    ResUpdateJobDTO res = new ResUpdateJobDTO();
    res.setId(currentJob.getId());
    res.setName(currentJob.getName());
    res.setSalary(currentJob.getSalary());
    res.setQuantity(currentJob.getQuantity());
    res.setLocation(currentJob.getLocation());
    res.setLevel(currentJob.getLevel());
    res.setStartDate(currentJob.getStartDate());
    res.setEndDate(currentJob.getEndDate());
    res.setActive(currentJob.isActive());
    res.setUpdatedAt(currentJob.getUpdatedAt());
    res.setUpdatedBy(currentJob.getUpdatedBy());

    if (currentJob.getSkills() != null) {
      List<String> skills = currentJob.getSkills().stream().map(s -> s.getName())
          .collect(Collectors.toList());
      res.setSkills(skills);
    }

    return res;
  }

  public void deleteJob(long id) {
    this.jobRespository.deleteById(id);
  }
}
