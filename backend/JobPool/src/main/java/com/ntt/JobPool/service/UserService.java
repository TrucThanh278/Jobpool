package com.ntt.JobPool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.domain.dto.Meta;
import com.ntt.JobPool.domain.dto.ResultPaginationDTO;
import com.ntt.JobPool.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

    public User getUserUserName(String userName) {
        return this.userRepository.findUserByEmail(userName);
    }

    // public ResultPaginationDTO getAllUsers(Pageable pageable) {
    // Page<User> rs = this.userRepository.findAll(pageable);
    // ResultPaginationDTO result = new ResultPaginationDTO();
    // Meta meta = new Meta();
    // meta.setPage(rs.getNumber() + 1);
    // meta.setPageSize(rs.getSize());
    // meta.setPages(rs.getTotalPages());
    // meta.setTotal(rs.getTotalElements());
    // result.setMeta(meta);
    // result.setResult(rs.getContent());
    // return result;
    // }

    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> rs = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();

//        Query từ dưới database
//        meta.setPage(rs.getNumber() + 1);
//        meta.setPageSize(rs.getSize());

//        Lấy từ front-end
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setTotal(meta.getPageSize());

        meta.setPages(rs.getTotalPages());
        meta.setTotal(rs.getTotalElements());
        result.setMeta(meta);
        result.setResult(rs.getContent());
        return result;
    }

    public User updateUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    public void deleteUser(long userId) {
        this.userRepository.deleteById(userId);
    }
}
