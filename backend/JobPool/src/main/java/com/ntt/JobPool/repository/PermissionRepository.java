package com.ntt.JobPool.repository;

import com.ntt.JobPool.domain.Permission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>,
    JpaSpecificationExecutor<Permission> {

  boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);

  List<Permission> findByInId(List<Long> ids);
}
