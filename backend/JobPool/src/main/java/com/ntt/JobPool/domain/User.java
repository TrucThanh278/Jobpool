package com.ntt.JobPool.domain;

import com.ntt.JobPool.utils.SecurityUtil;
import com.ntt.JobPool.utils.enums.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;

  @NotBlank(message = "Email không được để trống !")
  private String email;

  @NotBlank(message = "Mật khẩu không được để trống !")
  private String password;

  private int age;

  @Enumerated(EnumType.STRING)
  private GenderEnum gender;
  private String address;

  @Column(columnDefinition = "MEDIUMTEXT")
  private String refreshToken;
  private Instant createdAt;
  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
  }

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
}
