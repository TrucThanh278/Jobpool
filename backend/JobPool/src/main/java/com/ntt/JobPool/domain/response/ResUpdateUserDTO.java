package com.ntt.JobPool.domain.response;

import com.ntt.JobPool.utils.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateUserDTO {

  private long id;
  private String name;
  private GenderEnum gender;
  private String address;
  private int age;
  private Instant updatedAt;
  private Instant createdAt;
  private CompanyUser company;

  @Getter
  @Setter
  public static class CompanyUser {

    private long id;
    private String name;
  }
}
