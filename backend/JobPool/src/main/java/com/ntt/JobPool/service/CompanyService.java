package com.ntt.JobPool.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntt.JobPool.domain.Company;
import com.ntt.JobPool.domain.dto.Meta;
import com.ntt.JobPool.domain.dto.ResultPaginationDTO;
import com.ntt.JobPool.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO getAllCompanies(Pageable page) {
        Page<Company> rs = this.companyRepository.findAll(page);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(rs.getNumber() + 1);
        meta.setPageSize(rs.getSize());
        meta.setPages(rs.getTotalPages());
        meta.setTotal(rs.getTotalElements());
        result.setMeta(meta);
        result.setResult(rs.getContent());
        return result;
    }

    public Company updateCompany(Company company) {
        Optional<Company> c = this.companyRepository.findById(company.getId());
        if (c.isPresent()) {
            Company currentCompany = c.get();
            currentCompany.setName(company.getName());
            currentCompany.setAddress(company.getAddress());
            currentCompany.setLogo(company.getLogo());
            currentCompany.setDescription(company.getDescription());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    public void deleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }
}
