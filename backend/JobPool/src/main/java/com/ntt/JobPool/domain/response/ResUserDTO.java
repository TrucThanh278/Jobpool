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
public class ResUserDTO {

  private long id;
  private String name;
  private String email;
  private GenderEnum gender;
  private String address;
  private int age;
  private Instant createAt;
  private Instant updateAt;
  private CompanyUser company;


  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CompanyUser {

    private long id;
    private String name;
  }
}
