package com.ntt.JobPool.repository;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ntt.JobPool.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>,
    JpaSpecificationExecutor<Company> {

  List<Company> findAll();

  Optional<Company> findCompanyById(long id);
}
