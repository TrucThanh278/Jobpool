package com.ntt.JobPool.service;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.domain.response.ResCreateUserDTO;
import com.ntt.JobPool.domain.response.ResUpdateUserDTO;
import com.ntt.JobPool.domain.response.ResUserDTO;
import com.ntt.JobPool.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    public User handleCreateUser(User user);

    public User getUserById(Long userId);

    public User getUserByUserName(String userName);

    public Boolean isEmailExist(String email);

    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable);

    public ResCreateUserDTO convertToResCreateUserDTO(User user);

    public ResUserDTO convertToResUserDTO(User user);

    public ResUpdateUserDTO convertToRestResUpdateUserDTO(User user);

    public User updateUser(User user);

    public void deleteUser(long userId);

    public void updateUserToken(String token, String email);

    public User getUserByRefreshTokenAndEmail(String token, String email);
}
