package com.ntt.JobPool.repository;

import com.ntt.JobPool.domain.Company;
import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ntt.JobPool.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findUserById(long id);

  User findUserByEmail(String email);

  List<User> findAll();

  Boolean existsByEmail(String email);

  User findByRefreshTokenAndEmail(String refresh_token, String email);

  List<User> findByCompany(Company company);
}
