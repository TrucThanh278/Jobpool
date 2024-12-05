package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Permission;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {
    public boolean isPermissionExist(Permission permission);

    public boolean isSameName(Permission p);

    public Permission save(Permission p);

    public Permission getPermissionById(long id);

    public Permission updatePermission(Permission permission);

    public ResultPaginationDTO getAllPermission(@Filter Specification<Permission> spec,
                                                Pageable pageable);

    public void delete(long id);
}
