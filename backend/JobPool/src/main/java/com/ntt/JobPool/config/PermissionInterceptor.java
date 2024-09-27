package com.ntt.JobPool.config;

import com.ntt.JobPool.domain.Permission;
import com.ntt.JobPool.domain.Role;
import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.service.UserService;
import com.ntt.JobPool.utils.SecurityUtil;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import com.ntt.JobPool.utils.exception.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

public class PermissionInterceptor implements HandlerInterceptor {

  @Autowired
  private UserService userService;

  @Override
  @Transactional
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    String requestURI = request.getRequestURI();
    String httpMethod = request.getMethod();
    System.out.println(">>> RUN preHandle");
    System.out.println(">>> path= " + path);
    System.out.println(">>> httpMethod= " + httpMethod);
    System.out.println(">>> requestURI= " + requestURI);

    // check permission
    String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
        ? SecurityUtil.getCurrentUserLogin().get()
        : "";
    if (email != null && !email.isEmpty()) {
      User user = this.userService.getUserByUserName(email);
      if (user != null) {
        Role role = user.getRole();
        if (role != null) {
          List<Permission> permissions = role.getPermissions();
          boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
              && item.getMethod().equals(httpMethod));

          if (isAllow == false) {
            throw new PermissionException("Ban khong co quyen truy cap endpoint nay !");
          }
        } else {
          throw new PermissionException("Ban khong co quyen truy cap endpoint nay !");
        }
      }
    }

    return true;
  }
}
