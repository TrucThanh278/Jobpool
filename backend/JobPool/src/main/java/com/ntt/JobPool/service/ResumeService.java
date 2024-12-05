package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Resume;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.resume.ResCreateResumeDTO;
import com.ntt.JobPool.domain.response.resume.ResResumeDTO;
import com.ntt.JobPool.domain.response.resume.ResUpdateResumeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ResumeService {

    public boolean checkResumeExistByUserAndJob(Resume resume);

    public ResCreateResumeDTO createResume(Resume resume);

    public ResUpdateResumeDTO updateResume(Resume resume);

    public void deleteResume(long id);

    public ResResumeDTO convertResumeToResResumeDTO(Resume resume);

    public ResultPaginationDTO getAllResumes(Specification<Resume> s, Pageable p);

    public ResultPaginationDTO getResumeByUser(Pageable pageable);


}
