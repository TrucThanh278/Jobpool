package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Permission;
import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO.Meta;
import com.ntt.JobPool.repository.PermissionRepository;
import com.ntt.JobPool.repository.RoleRepository;
import com.turkraft.springfilter.boot.Filter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  public boolean existByName(String name) {
    return this.roleRepository.existsByName(name);
  }

  public Role save(Role role) {
    if (role.getPermissions() != null) {
      List<Long> listIdPermissions = role.getPermissions().stream().map(r -> r.getId()).collect(
          Collectors.toList());
      List<Permission> listPermissionsDb = this.permissionRepository.findByInId(listIdPermissions);
      role.setPermissions(listPermissionsDb);
    }

    return this.roleRepository.save(role);
  }

  public Role getRoleById(long id) {
    Optional<Role> r = this.roleRepository.findById(id);
    if (r.isPresent()) {
      return r.get();
    }
    return null;
  }

  public Role update(Role r) {
    Role roleDB = this.getRoleById(r.getId());
    // check permissions
    if (r.getPermissions() != null) {
      List<Long> reqPermissions = r.getPermissions()
          .stream().map(x -> x.getId())
          .collect(Collectors.toList());

      List<Permission> dbPermissions = this.permissionRepository.findByInId(reqPermissions);
      r.setPermissions(dbPermissions);
    }

    roleDB.setName(r.getName());
    roleDB.setDescription(r.getDescription());
    roleDB.setActive(r.isActive());
    roleDB.setPermissions(r.getPermissions());
    roleDB = this.roleRepository.save(roleDB);
    return roleDB;
  }

  public ResultPaginationDTO getAllRoles(@Filter Specification<Role> spec, Pageable pageable) {
    Page<Role> pageRoles = this.roleRepository.findAll(spec, pageable);
    ResultPaginationDTO res = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new Meta();

    meta.setPage(pageable.getPageNumber() + 1);
    meta.setPageSize(pageable.getPageSize());

    meta.setPages(pageRoles.getTotalPages());
    meta.setTotal(pageRoles.getTotalElements());

    res.setMeta(meta);
    res.setResult(pageRoles.getContent());
    return res;
  }

  public void delete(long id) {
    this.roleRepository.deleteById(id);
  }
}
