package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.response.job.ResCreateJobDTO;
import com.ntt.JobPool.domain.response.job.ResUpdateJobDTO;

import java.util.Optional;

public interface JobService {
    public ResCreateJobDTO createJob(Job job);

    public Optional<Job> getJobById(long id);

    public ResUpdateJobDTO updateJob(Job newJob, Job dbJob);

    public void deleteJob(long id);
}
