package com.ntt.JobPool.domain.response.resume;

import com.ntt.JobPool.utils.enums.ResumeStateEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResResumeDTO {

  private long id;
  private String url;
  private String email;

  @Enumerated(EnumType.STRING)
  private ResumeStateEnum status;
  private Instant createdAt;
  private Instant updatedAt;

  private String createdBy;
  private String updatedBy;

  private UserResume user;
  private JobResume job;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class UserResume {

    private long id;
    private String name;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class JobResume {

    private long id;
    private String name;
  }
}
