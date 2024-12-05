package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.domain.Subscriber;
import com.ntt.JobPool.domain.response.email.ResEmailJob;

public interface SubscriberService {

    public boolean isExistsByEmail(String email);

    public Subscriber create(Subscriber subs);

    public Subscriber update(Subscriber subsDB, Subscriber subsRequest);

    public Subscriber getById(long id);

    public ResEmailJob convertJobToSendEmail(Job job);

    public void sendSubscribersEmailJobs();

    public Subscriber getByEmail(String email);

}
