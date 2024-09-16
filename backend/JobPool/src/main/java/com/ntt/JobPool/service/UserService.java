package com.ntt.JobPool.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ntt.JobPool.domain.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User saveUser(User user) {
    return this.userRepository.save(user);
  }

  public User getUserById(Long userId) {
    return this.userRepository.findById(userId).orElse(null);
  }

  public User getUserByUserName(String userName) {
    return this.userRepository.findUserByEmail(userName);
  }

  public Boolean isEmailExist(String email) {
    return this.userRepository.existsByEmail(email);
  }

  public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
    Page<User> rs = this.userRepository.findAll(spec, pageable);
    ResultPaginationDTO result = new ResultPaginationDTO();
    Meta meta = new Meta();

//        Query từ dưới database
//        meta.setPage(rs.getNumber() + 1);
//        meta.setPageSize(rs.getSize());

//        Lấy từ front-end
    meta.setPage(pageable.getPageNumber() + 1);
    meta.setTotal(meta.getPageSize());

    meta.setPages(rs.getTotalPages());
    meta.setTotal(rs.getTotalElements());
    result.setMeta(meta);

    List<ResUserDTO> listUser = rs.getContent()
        .stream()
        .map(item -> new ResUserDTO(
            item.getId(),
            item.getName(),
            item.getEmail(),
            item.getGender(),
            item.getAddress(),
            item.getAge(),
            item.getCreatedAt(),
            item.getUpdatedAt()
        )).collect(Collectors.toList());

    result.setResult(rs.getContent());

    return result;
  }

  public ResCreateUserDTO convertToResCreateUserDTO(User user) {
    ResCreateUserDTO u = new ResCreateUserDTO();
    u.setId(user.getId());
    u.setAge(user.getAge());
    u.setName(user.getName());
    u.setEmail(user.getEmail());
    u.setGender(user.getGender());
    u.setAddress(user.getAddress());
    u.setCreateAt(user.getCreatedAt());
    return u;
  }

  public ResUserDTO convertToResUserDTO(User user) {
    ResUserDTO u = new ResUserDTO();
    u.setId(user.getId());
    u.setAge(user.getAge());
    u.setName(user.getName());
    u.setEmail(user.getEmail());
    u.setGender(user.getGender());
    u.setAddress(user.getAddress());
    u.setCreateAt(user.getCreatedAt());
    u.setUpdateAt(user.getUpdatedAt());
    return u;
  }

  public ResUpdateUserDTO convertToRestResUpdateUserDTO(User user) {
    ResUpdateUserDTO u = new ResUpdateUserDTO();
    u.setId(user.getId());
    u.setName(user.getName());
    u.setAge(user.getAge());
    u.setGender(user.getGender());
    u.setUpdateAt(user.getUpdatedAt());
    return u;
  }

  public User updateUser(User user) {
    User u = this.getUserById(user.getId());

    if (u != null) {
      u.setAddress(user.getAddress());
      u.setGender(user.getGender());
      u.setName(user.getName());
      u.setName(user.getName());
    }

    return this.userRepository.save(user);
  }

  public void deleteUser(long userId) {
    this.userRepository.deleteById(userId);
  }

  public void updateUserToken(String token, String email) {
    User u = this.getUserByUserName(email);
    if (u != null) {
      u.setRefreshToken(token);
      this.userRepository.save(u);
    }
  }

  public User getUserByRefreshTokenAndEmail(String token, String email) {
    return this.userRepository.findByRefreshTokenAndEmail(token, email);
  }
}
