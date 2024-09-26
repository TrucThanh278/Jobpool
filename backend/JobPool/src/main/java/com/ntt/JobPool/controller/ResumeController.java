package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Company;
import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.Resume;
import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.resume.ResCreateResumeDTO;
import com.ntt.JobPool.domain.response.resume.ResResumeDTO;
import com.ntt.JobPool.domain.response.resume.ResUpdateResumeDTO;
import com.ntt.JobPool.service.ResumeService;
import com.ntt.JobPool.service.UserService;
import com.ntt.JobPool.utils.SecurityUtil;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

  @Autowired
  private ResumeService resumeService;

  @Autowired
  private FilterBuilder filterBuilder;

  @Autowired
  private FilterSpecificationConverter filterSpecificationConverter;

  @Autowired
  private UserService userService;

  @PostMapping("/resumes")
  @ApiMessage("Create a resume")
  public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume)
      throws IdInvalidException {
    boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
    if (!isIdExist) {
      throw new IdInvalidException("User hoac job khong ton tai !");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.createResume(resume));
  }

  @PutMapping("/resumes")
  @ApiMessage("Update resume")
  public ResponseEntity<ResUpdateResumeDTO> updateResume(@Valid @RequestBody Resume resume)
      throws IdInvalidException {
    Optional<Resume> r = this.resumeService.getResumeById(resume.getId());

    if (r.isEmpty()) {
      throw new IdInvalidException("Resume khong ton tai !");
    }

    Resume result = r.get();
    result.setStatus(resume.getStatus());
    return ResponseEntity.ok().body(this.resumeService.updateResume(resume));
  }

  @DeleteMapping("/resumes/{id}")
  @ApiMessage("Delete a resume")
  public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
    Optional<Resume> r = this.resumeService.getResumeById(id);
    if (r.isEmpty()) {
      throw new IdInvalidException("Resume voi id = " + id + " khong ton tai !");
    }

    this.resumeService.deleteResume(id);
    return ResponseEntity.ok().body(null);
  }

  @GetMapping("/resumes/{id}")
  @ApiMessage("Get a resume by id")
  public ResponseEntity<ResResumeDTO> getResume(@PathVariable("id") long id)
      throws IdInvalidException {
    Optional<Resume> r = this.resumeService.getResumeById(id);
    if (r.isEmpty()) {
      throw new IdInvalidException("Resume voi id = " + id + " khong ton tai !");
    }

    return ResponseEntity.ok().body(this.resumeService.convertResumeToResResumeDTO(r.get()));
  }

  @GetMapping("/resumes")
  @ApiMessage("Get all resume")
  public ResponseEntity<ResultPaginationDTO> getAllResumes(
      @Filter Specification<Resume> spec,
      Pageable pageable) {
    List<Long> arrJobIds = null;
    String email =
        SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin()
            .get() : "";

    User currentUser = this.userService.getUserByUserName(email);
    if (currentUser != null) {
      Company c = currentUser.getCompany();
      if (c != null) {
        List<Job> companyJobs = c.getJobs();
        if (companyJobs != null && companyJobs.size() > 0) {
          arrJobIds = companyJobs.stream().map(i -> i.getId()).collect(Collectors.toList());
        }
      }
    }

    Specification<Resume> jobInSpec = filterSpecificationConverter.convert(
        filterBuilder.field("job")
            .in(filterBuilder.input(arrJobIds)).get());

    Specification<Resume> finalSpec = jobInSpec.and(spec);

    return ResponseEntity.ok().body(this.resumeService.getAllResumes(finalSpec, pageable));
  }

  @PostMapping("/resumes/by-user")
  @ApiMessage("Get all resumes of current user")
  public ResponseEntity<ResultPaginationDTO> getAllResumesOfCurrentUser(Pageable pageable) {
    return ResponseEntity.ok().body(this.resumeService.getResumeByUser(pageable));
  }
}
