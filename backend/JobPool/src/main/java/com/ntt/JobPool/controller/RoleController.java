package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.impl.RoleServiceImpl;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

  @Autowired
  private RoleServiceImpl roleServiceImpl;

  @PostMapping("/roles")
  @ApiMessage("Create new role")
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
    if (this.roleServiceImpl.existByName(role.getName())) {
      throw new IdInvalidException("Role voi name " + role.getName() + " da ton tai !");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.roleServiceImpl.save(role));
  }

  @PutMapping("/roles")
  @ApiMessage("Update a role")
  public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
    if (this.roleServiceImpl.getRoleById(role.getId()) == null) {
      throw new IdInvalidException("Role voi id = " + role.getId() + " khong ton tai !");
    }

    return ResponseEntity.status(HttpStatus.OK).body(this.roleServiceImpl.update(role));
  }

  @GetMapping("/roles")
  @ApiMessage("Get all roles")
  ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec,
      Pageable pageable) {
    return ResponseEntity.ok().body(this.roleServiceImpl.getAllRoles(spec, pageable));
  }

  @GetMapping("/roles/{id}")
  @ApiMessage("Get role by id")
  public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws IdInvalidException {
    Role role = this.roleServiceImpl.getRoleById(id);
    if (role == null) {
      throw new IdInvalidException("Resume với id = " + id + " không tồn tại !");
    }

    return ResponseEntity.ok().body(role);
  }

  @DeleteMapping("/roles/{id}")
  @ApiMessage("Delete a role")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    // check id
    if (this.roleServiceImpl.getRoleById(id) == null) {
      throw new IdInvalidException("Role với id = " + id + " không tồn tại");
    }
    this.roleServiceImpl.delete(id);
    return ResponseEntity.ok().body(null);
  }

}
