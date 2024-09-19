package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.response.ResCreateUserDTO;
import com.ntt.JobPool.domain.response.ResUpdateUserDTO;
import com.ntt.JobPool.domain.response.ResUserDTO;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.UserService;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/v1")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/users/{id}")
  public ResponseEntity<ResUserDTO> getUserById(@PathVariable(name = "id") long id) {

    User user = this.userService.getUserById(id);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(this.userService.convertToResUserDTO(user));
  }

  @GetMapping("/users")
  @ApiMessage("fetch all users")
  public ResponseEntity<ResultPaginationDTO> getUsers(
      @Filter Specification spec, Pageable pageable) {

    ResultPaginationDTO users = this.userService.getAllUsers(spec, pageable);

    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @PostMapping("/users")
  @ApiMessage("Create a new user")
  public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody @Valid User user)
      throws IdInvalidException {
    boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
    if (isEmailExist) {
      throw new IdInvalidException(
          "Email " + user.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
    }

    String hashPassword = this.passwordEncoder.encode(user.getPassword());
    user.setPassword(hashPassword);
    User createdUser = this.userService.handleCreateUser(user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(this.userService.convertToResCreateUserDTO(createdUser));
  }

  @PutMapping("/users")
  @ApiMessage("Update a user")
  public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user)
      throws IdInvalidException {
    User u = this.userService.updateUser(user);

    if (u == null) {
      throw new IdInvalidException("User với id = " + user.getId() + " không tồn tại !");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(this.userService.convertToRestResUpdateUserDTO(u));
  }

  @DeleteMapping("/users/{id}")
  @ApiMessage("Delete a user")
  public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") long userId)
      throws IdInvalidException {
    User u = this.userService.getUserById(userId);
    if (u == null) {
      throw new IdInvalidException("User với id = " + userId + " không tồn tại !");
    }

    this.userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
