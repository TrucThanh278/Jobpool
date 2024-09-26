package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Permission;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO.Meta;
import com.ntt.JobPool.repository.PermissionRepository;
import com.turkraft.springfilter.boot.Filter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  @Autowired
  private PermissionRepository permissionRepository;

  public boolean isPermissionExist(Permission permission) {
    return this.permissionRepository.existsByModuleAndApiPathAndMethod(
        permission.getModule(), permission.getApiPath(), permission.getMethod()
    );
  }

  public boolean isSameName(Permission p) {
    Permission permissionDB = this.getPermissionById(p.getId());
    if (permissionDB != null) {
      if (permissionDB.getName().equals(p.getName())) {
        return true;
      }
    }
    return false;
  }

  public Permission save(Permission p) {
    return this.permissionRepository.save(p);
  }

  public Permission getPermissionById(long id) {
    Optional<Permission> p = this.permissionRepository.findById(id);
    if (p.isPresent()) {
      return p.get();
    }
    return null;
  }

  public Permission updatePermission(Permission permission) {
    Permission permissionDB = this.getPermissionById(permission.getId());

    if (permissionDB != null) {
      permissionDB.setName(permission.getName());
      permissionDB.setApiPath(permission.getApiPath());
      permissionDB.setMethod(permission.getMethod());
      permissionDB.setModule(permission.getModule());

      permissionDB = this.permissionRepository.save(permissionDB);
      return permissionDB;
    }
    return null;
  }

  public ResultPaginationDTO getAllPermission(@Filter Specification<Permission> spec,
      Pageable pageable) {
    Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
    ResultPaginationDTO res = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new Meta();

    meta.setPage(pageable.getPageNumber() + 1);
    meta.setPageSize(pageable.getPageSize());

    meta.setPages(pagePermission.getTotalPages());
    meta.setTotal(pagePermission.getTotalElements());

    res.setMeta(meta);
    res.setResult(pagePermission.getContent());
    return res;
  }

  public void delete(long id) {
    // delete permission_role
    Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
    Permission currentPermission = permissionOptional.get();
    currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

    this.permissionRepository.delete(currentPermission);
  }
}
