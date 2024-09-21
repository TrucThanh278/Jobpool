package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.response.job.ResCreateJobDTO;
import com.ntt.JobPool.domain.response.job.ResUpdateJobDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.JobService;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
@RequestMapping("/api/v1/")
public class JobController {

  @Autowired
  private JobService jobService;

  @PostMapping("/jobs")
  @ApiMessage("Create a job")
  public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
    return ResponseEntity.ok().body(this.jobService.createJob(job));
  }

  @GetMapping("/jobs")
  @ApiMessage("Get job with pagination")
  public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec,
      Pageable page) {
    return ResponseEntity.ok().body(this.jobService.getAllJobs(spec, page));
  }

  @GetMapping("/jobs/{id}")
  @ApiMessage("Get a job by id")
  public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
    Optional<Job> job = this.jobService.getJobById(id);
    if (!job.isPresent()) {
      throw new IdInvalidException("Job not found");
    }
    return ResponseEntity.ok().body(job.get());
  }

  @PutMapping("/jobs")
  @ApiMessage("Update a job")
  public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job)
      throws IdInvalidException {
    Optional<Job> currentJob = this.jobService.getJobById(job.getId());

    if (!currentJob.isPresent()) {
      throw new IdInvalidException("Job not found !");
    }
    return ResponseEntity.ok().body(this.jobService.updateJob(job));
  }

  @DeleteMapping("/jobs/{id}")
  @ApiMessage("Delete a job by id")
  public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
    Optional<Job> currentJob = this.jobService.getJobById(id);

    if (!currentJob.isPresent()) {
      throw new IdInvalidException("Job not found !");
    }

    this.jobService.deleteJob(id);

    return ResponseEntity.ok().body(null);
  }
}
