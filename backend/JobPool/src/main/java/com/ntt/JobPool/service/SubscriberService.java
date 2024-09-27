package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Skill;
import com.ntt.JobPool.domain.Subscriber;
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
}
