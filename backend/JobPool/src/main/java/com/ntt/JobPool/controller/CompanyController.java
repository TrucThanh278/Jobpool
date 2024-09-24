package com.ntt.JobPool.controller;

import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ntt.JobPool.domain.Company;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import com.ntt.JobPool.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

  @Autowired
  private CompanyService companyService;

  @PostMapping("/companies")
  public ResponseEntity<Company> createCompany(@RequestBody @Valid Company company) {
    Company savedCompany = this.companyService.saveCompany(company);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
  }

  @GetMapping("/companies")
  @ApiMessage("get all companies")
  public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification spec,
      Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(this.companyService.getAllCompanies(spec, pageable));
  }

  @GetMapping("/companies/{id}")
  @ApiMessage("get companies by id")
  public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id)
      throws IdInvalidException {
    Optional<Company> c = this.companyService.findCompanyById(id);
    if (c.isEmpty()) {
      throw new IdInvalidException("Cong ty voi id = " + id + " khong ton tai !");
    }
    return ResponseEntity.ok().body(c.get());
  }

  @PutMapping("/companies")
  public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
    return ResponseEntity.status(HttpStatus.OK).body(this.companyService.updateCompany(company));
  }

  @DeleteMapping("/companies/{id}")
  public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
    this.companyService.deleteCompany(id);
    return ResponseEntity.ok().body(null);
  }

}
