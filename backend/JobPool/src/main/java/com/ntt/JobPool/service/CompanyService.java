package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Company;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface CompanyService {
    public Company saveCompany(Company c);

    public ResultPaginationDTO getAllCompanies(Specification s, Pageable p);

    public Company updateCompany(Company c);

    public void deleteCompany(long id);

    public Optional<Company> findCompanyById(long id);
}
