package com.ntt.JobPool.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RestResponse<T> {

  private int statusCode;
  private String error;
  private Object message;
  private T data;
}
