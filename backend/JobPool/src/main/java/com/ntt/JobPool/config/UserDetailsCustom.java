package com.ntt.JobPool.config;

import com.ntt.JobPool.service.UserService;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ntt.JobPool.domain.User;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userService.getUserByUserName(username);
    if (user == null) {
      throw new UsernameNotFoundException("Username/password khong hop le");
    }
    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
  }

}