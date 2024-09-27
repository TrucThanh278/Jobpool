package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Subscriber;
import com.ntt.JobPool.service.SubscriberService;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

  @Autowired
  private SubscriberService subscriberService;

  @PostMapping("/subscribers")
  @ApiMessage("Create a subscriber")
  public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber sub)
      throws IdInvalidException {
    // check email
    boolean isExist = this.subscriberService.isExistsByEmail(sub.getEmail());
    if (isExist == true) {
      throw new IdInvalidException("Email " + sub.getEmail() + " đã tồn tại");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(sub));
  }

  @PutMapping("/subscribers")
  @ApiMessage("Update a subscriber")
  public ResponseEntity<Subscriber> update(@RequestBody Subscriber subsRequest)
      throws IdInvalidException {
    // check id
    Subscriber subsDB = this.subscriberService.getById(subsRequest.getId());
    if (subsDB == null) {
      throw new IdInvalidException("Id " + subsRequest.getId() + " không tồn tại");
    }
    return ResponseEntity.ok().body(this.subscriberService.update(subsDB, subsRequest));
  }

}
