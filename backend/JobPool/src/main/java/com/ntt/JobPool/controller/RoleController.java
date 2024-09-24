package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.RoleService;
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
  private RoleService roleService;

  @PostMapping("/roles")
  @ApiMessage("Create new role")
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
    if (this.roleService.existByName(role.getName())) {
      throw new IdInvalidException("Role voi name " + role.getName() + " da ton tai !");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.save(role));
  }

  @PutMapping("/roles")
  @ApiMessage("Update a role")
  public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
    if (this.roleService.getRoleById(role.getId()) == null) {
      throw new IdInvalidException("Role voi id = " + role.getId() + " khong ton tai !");
    }

    if (this.roleService.existByName(role.getName())) {
      throw new IdInvalidException("Role co ten " + role.getName() + " da ton tai !");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.update(role));
  }

  @GetMapping("/roles")
  @ApiMessage("Get all roles")
  ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec,
      Pageable pageable) {
    return ResponseEntity.ok().body(this.roleService.getAllRoles(spec, pageable));
  }

  @DeleteMapping("/roles/{id}")
  @ApiMessage("Delete a role")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    // check id
    if (this.roleService.getRoleById(id) == null) {
      throw new IdInvalidException("Role với id = " + id + " không tồn tại");
    }
    this.roleService.delete(id);
    return ResponseEntity.ok().body(null);
  }

}
