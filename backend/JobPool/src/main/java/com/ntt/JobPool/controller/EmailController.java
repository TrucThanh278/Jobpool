package com.ntt.JobPool.controller;

import com.ntt.JobPool.service.EmailService;
import com.ntt.JobPool.service.SubscriberService;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.SpringTemplateEngine;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

  @Autowired
  private SubscriberService subscriberService;

  private SpringTemplateEngine templateEngine;

  @GetMapping("/email")
  @ApiMessage("Send email")
  public String sendEmail() {
    this.subscriberService.sendSubscribersEmailJobs();
    return "ok";
  }


}
