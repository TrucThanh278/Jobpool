package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Job;
import com.ntt.JobPool.repository.JobRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

  @Autowired
  private MailSender mailSender;

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Autowired
  private JobRepository jobRepository;

  public void sendSimpleEmail() {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo("trucnguyenthanh56@gmail.com");
    msg.setSubject("Testing from Spring Boot");
    msg.setText("Hello World from Spring Boot Email");
    this.mailSender.send(msg);
  }

  public void sendEmailSync(String to, String subject, String content, boolean isMultipart,
      boolean isHtml) {
// Prepare message using a Spring helper
    MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
          isMultipart, StandardCharsets.UTF_8.name());
      message.setTo(to);
      message.setSubject(subject);
      message.setText(content, isHtml);
      this.javaMailSender.send(mimeMessage);
    } catch (MailException | MessagingException e) {
      System.out.println("ERROR SEND EMAIL: " + e);
    }
  }

  @Async
  public void sendEmailFromTemplateSync(
      String to,
      String subject,
      String templateName,
      String username,
      Object value) {

    Context context = new Context();
    context.setVariable("name", username);
    context.setVariable("jobs", value);

    String content = this.templateEngine.process(templateName, context);
    this.sendEmailSync(to, subject, content, false, true);
  }


}
