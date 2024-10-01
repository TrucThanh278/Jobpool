package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.Company;
import com.ntt.JobPool.domain.Resume;
import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.response.ResCreateUserDTO;
import com.ntt.JobPool.domain.response.ResUpdateUserDTO;
import com.ntt.JobPool.domain.response.ResUserDTO;
import com.ntt.JobPool.domain.response.ResUserDTO.RoleUser;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private RoleService roleService;

  public User handleCreateUser(User user) {
    if (user.getCompany() != null) {
      Optional<Company> c = this.companyService.findCompanyById(user.getCompany().getId());
      user.setCompany(c.isPresent() ? c.get() : null);
    }

    if (user.getRole() != null) {
      Role r = this.roleService.getRoleById(user.getRole().getId());
      user.setRole(r != null ? r : null);
    }
    return this.userRepository.save(user);
  }

  public User getUserById(Long userId) {
    return this.userRepository.findById(userId).orElse(null);
  }

  public User getUserByUserName(String userName) {
    return this.userRepository.findUserByEmail(userName);
  }

  public Boolean isEmailExist(String email) {
    return this.userRepository.existsByEmail(email);
  }

  public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
    Page<User> rs = this.userRepository.findAll(spec, pageable);
    ResultPaginationDTO result = new ResultPaginationDTO();
    ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

//        Query từ dưới database
//        meta.setPage(rs.getNumber() + 1);
//        meta.setPageSize(rs.getSize());

//        Lấy từ front-end
    meta.setPage(pageable.getPageNumber() + 1);
    meta.setTotal(meta.getPageSize());

    meta.setPages(rs.getTotalPages());
    meta.setTotal(rs.getTotalElements());
    result.setMeta(meta);

    List<ResUserDTO> listUsers = rs.getContent().stream().map(u -> this.convertToResUserDTO(u))
        .collect(Collectors.toList());

    result.setResult(listUsers);

    return result;
  }

  public ResCreateUserDTO convertToResCreateUserDTO(User user) {
    ResCreateUserDTO u = new ResCreateUserDTO();
    ResCreateUserDTO.CompanyUser c = new ResCreateUserDTO.CompanyUser();
    u.setId(user.getId());
    u.setAge(user.getAge());
    u.setName(user.getName());
    u.setEmail(user.getEmail());
    u.setGender(user.getGender());
    u.setAddress(user.getAddress());
    u.setCreateAt(user.getCreatedAt());

    if (user.getCompany() != null) {
      c.setId(user.getCompany().getId());
      c.setName(user.getCompany().getName());
      u.setCompany(c);
    }

    return u;
  }

  public ResUserDTO convertToResUserDTO(User user) {
    ResUserDTO u = new ResUserDTO();
    ResUserDTO.CompanyUser c = new ResUserDTO.CompanyUser();
    ResUserDTO.RoleUser r = new RoleUser();
    u.setId(user.getId());
    u.setAge(user.getAge());
    u.setName(user.getName());
    u.setEmail(user.getEmail());
    u.setGender(user.getGender());
    u.setAddress(user.getAddress());
    u.setCreateAt(user.getCreatedAt());
    u.setUpdateAt(user.getUpdatedAt());

    if (user.getCompany() != null) {
      c.setId(user.getCompany().getId());
      c.setName(user.getCompany().getName());
      u.setCompany(c);
    }

    if (user.getRole() != null) {
      r.setId(user.getRole().getId());
      r.setName(user.getRole().getName());
      u.setRole(r);
    }

    return u;
  }

  public ResUpdateUserDTO convertToRestResUpdateUserDTO(User user) {
    ResUpdateUserDTO u = new ResUpdateUserDTO();
    ResUpdateUserDTO.CompanyUser c = new ResUpdateUserDTO.CompanyUser();

    u.setId(user.getId());
    u.setName(user.getName());
    u.setAge(user.getAge());
    u.setGender(user.getGender());
    u.setUpdatedAt(user.getUpdatedAt());
    u.setCreatedAt(user.getCreatedAt());

    if (user.getCompany() != null) {
      c.setId(user.getCompany().getId());
      c.setName(user.getCompany().getName());
      u.setCompany(c);
    }
    return u;
  }

  public User updateUser(User user) {
    User u = this.getUserById(user.getId());

    if (u != null) {
      u.setAddress(user.getAddress());
      u.setGender(user.getGender());
      u.setName(user.getName());
      u.setName(user.getName());
    }

    if (user.getCompany() != null) {
      Optional<Company> c = this.companyService.findCompanyById(user.getCompany().getId());
      u.setCompany(c.isPresent() ? c.get() : null);
    }

    if (user.getRole() != null) {
      Role r = this.roleService.getRoleById(user.getRole().getId());
      u.setRole(r != null ? r : null);
    }

    return this.userRepository.save(u);
  }

  public void deleteUser(long userId) {
    Optional<User> optionUser = this.userRepository.findUserById(userId);
    User currentUser = optionUser.get();
    currentUser.getResumes().forEach(resume -> resume.setUser(null));

    this.userRepository.deleteById(userId);
  }

  public void updateUserToken(String token, String email) {
    User u = this.getUserByUserName(email);
    if (u != null) {
      u.setRefreshToken(token);
      this.userRepository.save(u);
    }
  }

  public User getUserByRefreshTokenAndEmail(String token, String email) {
    return this.userRepository.findByRefreshTokenAndEmail(token, email);
  }
}
