package com.ntt.JobPool.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ntt.JobPool.domain.Company;
import com.ntt.JobPool.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company saveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return this.companyRepository.findAll();
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
