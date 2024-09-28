package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.Skill;
import com.ntt.JobPool.domain.Subscriber;
import com.ntt.JobPool.domain.response.email.ResEmailJob;
import com.ntt.JobPool.repository.JobRepository;
import com.ntt.JobPool.repository.SkillRepository;
import com.ntt.JobPool.repository.SubscriberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {

  @Autowired
  private SubscriberRepository subscriberRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private EmailService emailService;


  public boolean isExistsByEmail(String email) {
    return this.subscriberRepository.existsByEmail(email);
  }

  public Subscriber create(Subscriber subs) {
    // check skills
    if (subs.getSkills() != null) {
      List<Long> reqSkills = subs.getSkills()
          .stream().map(x -> x.getId())
          .collect(Collectors.toList());

      List<Skill> dbSkills = this.skillRepository.findSkillByIdIn(reqSkills);
      subs.setSkills(dbSkills);
    }

    return this.subscriberRepository.save(subs);
  }

  public Subscriber update(Subscriber subsDB, Subscriber subsRequest) {
    // check skills
    if (subsRequest.getSkills() != null) {
      List<Long> reqSkills = subsRequest.getSkills()
          .stream().map(x -> x.getId())
          .collect(Collectors.toList());

      List<Skill> dbSkills = this.skillRepository.findSkillByIdIn(reqSkills);
      subsDB.setSkills(dbSkills);
    }
    return this.subscriberRepository.save(subsDB);
  }

  public Subscriber getById(long id) {
    Optional<Subscriber> subsOptional = this.subscriberRepository.findById(id);
    if (subsOptional.isPresent()) {
      return subsOptional.get();
    }
    return null;
  }

  public ResEmailJob convertJobToSendEmail(Job job) {
    ResEmailJob res = new ResEmailJob();
    res.setName(job.getName());
    res.setSalary(job.getSalary());
    res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
    List<Skill> skills = job.getSkills();
    List<ResEmailJob.SkillEmail> s = skills.stream()
        .map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
        .collect(Collectors.toList());
    res.setSkills(s);
    return res;
  }

  public void sendSubscribersEmailJobs() {
    List<Subscriber> listSubs = this.subscriberRepository.findAll();
    if (listSubs != null && listSubs.size() > 0) {
      for (Subscriber sub : listSubs) {
        List<Skill> listSkills = sub.getSkills();
        if (listSkills != null && listSkills.size() > 0) {
          List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
          if (listJobs != null && listJobs.size() > 0) {
            List<ResEmailJob> arr = listJobs.stream().map(
                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
            this.emailService.sendEmailFromTemplateSync(
                sub.getEmail(),
                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                "job",
                sub.getName(),
                arr);
          }
        }
      }
    }
  }
}
