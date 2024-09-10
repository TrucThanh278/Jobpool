package com.ntt.JobPool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ntt.JobPool.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User save(User user);

    User findUserById(long id);

    User findUserByEmail(String email);

    List<User> findAll();

    Boolean existsByEmail(String email);
}
