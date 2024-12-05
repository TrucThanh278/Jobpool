package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.Permission;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.impl.PermissionServiceImpl;
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
public class PermissionController {

  @Autowired
  private PermissionServiceImpl permissionServiceImpl;

  @PostMapping("/permissions")
  @ApiMessage("Create new permission")
  public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission)
      throws IdInvalidException {
    if (this.permissionServiceImpl.isPermissionExist(permission)) {
      throw new IdInvalidException("Permission nay da ton tai !");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionServiceImpl.save(permission));
  }

  @PutMapping("/permissions")
  @ApiMessage("Update permission")
  public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission p)
      throws IdInvalidException {

    if (this.permissionServiceImpl.getPermissionById(p.getId()) == null) {
      throw new IdInvalidException("Permission voi id = " + p.getId() + " khong ton tai !");
    }

    if (this.permissionServiceImpl.isPermissionExist(p)) {
      if (this.permissionServiceImpl.isSameName(p)) {
        throw new IdInvalidException("Permission đã tồn tại.");
      }
    }

    return ResponseEntity.ok().body(this.permissionServiceImpl.updatePermission(p));
  }

  @GetMapping("/permissions")
  @ApiMessage("Get all permission")
  public ResponseEntity<ResultPaginationDTO> getAllPermission(
      @Filter Specification<Permission> spec,
      Pageable pageable) {
    return ResponseEntity.ok().body(this.permissionServiceImpl.getAllPermission(spec, pageable));
  }

  @DeleteMapping("/permissions/{id}")
  @ApiMessage("delete a permission")
  public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
    // check exist by id
    if (this.permissionServiceImpl.getPermissionById(id) == null) {
      throw new IdInvalidException("Permission với id = " + id + " không tồn tại.");
    }
    this.permissionServiceImpl.delete(id);
    return ResponseEntity.ok().body(null);
  }
}
