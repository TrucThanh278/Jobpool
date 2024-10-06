package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.Resume;
import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO.Meta;
import com.ntt.JobPool.domain.response.resume.ResCreateResumeDTO;
import com.ntt.JobPool.domain.response.resume.ResResumeDTO;
import com.ntt.JobPool.domain.response.resume.ResResumeDTO.JobResume;
import com.ntt.JobPool.domain.response.resume.ResResumeDTO.UserResume;
import com.ntt.JobPool.domain.response.resume.ResUpdateResumeDTO;
import com.ntt.JobPool.repository.JobRepository;
import com.ntt.JobPool.repository.ResumeRepository;
import com.ntt.JobPool.repository.UserRepository;
import com.ntt.JobPool.utils.SecurityUtil;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

  @Autowired
  private ResumeRepository resumeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  FilterBuilder fb;

  @Autowired
  private FilterParser filterParser;

  @Autowired
  private FilterSpecificationConverter filterSpecificationConverter;

  public Optional<Resume> getResumeById(long id) {
    return this.resumeRepository.findById(id);
  }

  public boolean checkResumeExistByUserAndJob(Resume resume) {
    if (resume.getUser() == null) {
      return false;
    }

    Optional<User> u = this.userRepository.findById(resume.getUser().getId());
    if (u.isEmpty()) {
      return false;
    }

    if (resume.getJob() == null) {
      return false;
    }

    Optional<Job> j = this.jobRepository.findById(resume.getJob().getId());
    if (j.isEmpty()) {
      return false;
    }

    return true;
  }

  public ResCreateResumeDTO createResume(Resume resume) {
    resume = this.resumeRepository.save(resume);

    ResCreateResumeDTO r = new ResCreateResumeDTO();
    r.setId(resume.getId());
    r.setCreatedAt(resume.getCreatedAt());
    r.setCreatedBy(resume.getCreatedBy());

    return r;
  }

  public ResUpdateResumeDTO updateResume(Resume resume) {
    resume = this.resumeRepository.save(resume);
    ResUpdateResumeDTO res = new ResUpdateResumeDTO();
    res.setUpdatedAt(resume.getUpdatedAt());
    res.setUpdatedBy(resume.getUpdatedBy());
    return res;
  }

  public void deleteResume(long id) {
    this.resumeRepository.deleteById(id);
  }

  public ResResumeDTO convertResumeToResResumeDTO(Resume resume) {
    ResResumeDTO res = new ResResumeDTO();
    res.setId(resume.getId());
    res.setEmail(resume.getEmail());
    res.setUrl(resume.getUrl());
    res.setStatus(resume.getStatus());
    res.setCreatedAt(resume.getCreatedAt());
    res.setCreatedBy(resume.getCreatedBy());
    res.setUpdatedAt(resume.getUpdatedAt());
    res.setUpdatedBy(resume.getUpdatedBy());

    if (resume.getJob() != null) {
      res.setCompany(resume.getJob().getName());
    }

    res.setUser(new UserResume(resume.getUser().getId(), resume.getUser().getName()));
    res.setJob(new JobResume(resume.getJob().getId(), resume.getJob().getName()));
    return res;
  }

  public ResultPaginationDTO getAllResumes(Specification<Resume> s, Pageable p) {
    Page<Resume> pageResume = this.resumeRepository.findAll(s, p);

    ResultPaginationDTO res = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new Meta();

    meta.setPage(p.getPageNumber() + 1);
    meta.setPageSize(p.getPageSize());

    meta.setPages(pageResume.getTotalPages());
    meta.setTotal(pageResume.getTotalElements());

    res.setMeta(meta);

    List<ResResumeDTO> listResumes = pageResume.getContent().stream()
        .map(r -> this.convertResumeToResResumeDTO(r)).collect(
            Collectors.toList());

    res.setResult(listResumes);

    return res;
  }

  public ResultPaginationDTO getResumeByUser(Pageable pageable) {
    //decode access token lay email nguoi dung hien tai
    String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
        SecurityUtil.getCurrentUserLogin().get() : "";

    FilterNode node = filterParser.parse("email='" + email + "'");
    FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
    Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

    ResultPaginationDTO res = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new Meta();

    meta.setPage(pageable.getPageNumber() + 1);
    meta.setPageSize(pageable.getPageSize());
    meta.setPages(pageResume.getTotalPages());
    meta.setTotal(pageResume.getTotalElements());

    res.setMeta(meta);
    List<ResResumeDTO> listRes = pageResume.getContent().stream()
        .map(r -> this.convertResumeToResResumeDTO(r)).collect(
            Collectors.toList());

    res.setResult(listRes);

    return res;
  }
}
