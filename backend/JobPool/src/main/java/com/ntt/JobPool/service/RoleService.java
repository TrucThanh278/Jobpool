package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    public boolean existByName(String name);

    public Role save(Role role);

    public Role getRoleById(long id);

    public Role update(Role r);

    public ResultPaginationDTO getAllRoles(@Filter Specification<Role> spec, Pageable pageable);

    public void delete(long id);
}
