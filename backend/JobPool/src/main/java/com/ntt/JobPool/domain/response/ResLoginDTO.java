package com.ntt.JobPool.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResLoginDTO {

  @JsonProperty("access_token")
  private String accessToken;
  private UserLogin user;

  @Setter
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserLogin {

    private long id;
    private String email;
    private String name;
    private Role role;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserGetAccount {

    private UserLogin user;
  }

  
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserInsideToken {

    private long id;
    private String email;
    private String name;
  }
}
