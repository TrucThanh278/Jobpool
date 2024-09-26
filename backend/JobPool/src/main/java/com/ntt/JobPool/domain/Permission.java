package com.ntt.JobPool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntt.JobPool.utils.SecurityUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotBlank(message = "Name khong duoc de trong !")
  private String name;
  @NotBlank(message = "apiPath khong duoc de trong !")
  private String apiPath;
  @NotBlank(message = "method khong duoc de trong !")
  private String method;
  @NotBlank(message = "module khong duoc de trong !")
  private String module;
  private Instant createdAt;
  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
  @JsonIgnore
  private List<Role> roles;

  @PrePersist
  public void handleBeforeCreate() {
    this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";

    this.createdAt = Instant.now();
  }

  @PreUpdate
  public void handleBeforeUpdate() {
    this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";

    this.updatedAt = Instant.now();
  }

  public Permission(String name, String apiPath, String method, String module) {
    this.name = name;
    this.apiPath = apiPath;
    this.method = method;
    this.module = module;
  }

}
