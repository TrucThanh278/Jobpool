package com.ntt.JobPool.domain.response.job;

import com.ntt.JobPool.utils.enums.LevelEnum;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateJobDTO {

  private long id;
  private String name;

  private String location;

  private double salary;

  private int quantity;

  private LevelEnum level;

  private Instant startDate;
  private Instant endDate;
  private boolean isActive;

  private List<String> skills;

  private Instant createdAt;
  private String createdBy;
}
