package com.ntt.JobPool.controller;

import com.ntt.JobPool.domain.User;
import com.ntt.JobPool.domain.request.ReqLoginDTO;
import com.ntt.JobPool.domain.response.ResLoginDTO.UserGetAccount;
import com.ntt.JobPool.service.UserService;
import com.ntt.JobPool.utils.annotations.ApiMessage;
import com.ntt.JobPool.utils.exception.IdInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntt.JobPool.domain.response.ResLoginDTO;
import com.ntt.JobPool.utils.SecurityUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @Autowired
  private AuthenticationManagerBuilder authenticationManagerBuilder;

  @Autowired
  private SecurityUtil securityUtil;

  @Autowired
  private UserService userService;

  @Value("${trucnguyen.jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenExpiration;

  @PostMapping("/auth/login")
  public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO)
      throws Exception {
    // Nạp input gồm username/password vào Security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        reqLoginDTO.getUsername(), reqLoginDTO.getPassword());

    // xác thực người dùng => cần viết hàm loadUserByUsername
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    // set thông tin người dùng đăng nhập vào context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    ResLoginDTO res = new ResLoginDTO();
    //Lay user hien tai dang dang nhap de gan cho user trong response
    User currentUser = this.userService.getUserByUserName(reqLoginDTO.getUsername());
    if (currentUser != null) {
      ResLoginDTO.UserLogin u = new ResLoginDTO.UserLogin(
          currentUser.getId(), currentUser.getEmail(), currentUser.getName(),
          currentUser.getRole());
      res.setUser(u);
    }

    // create token
    String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);

    res.setAccessToken(access_token);

    String refresh_token = this.securityUtil.createRefreshToken(reqLoginDTO.getUsername(), res);

    //    update user refresh token
    this.userService.updateUserToken(refresh_token, reqLoginDTO.getUsername());

    //    set cookies
    ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
  }

  @GetMapping("/auth/account")
  @ApiMessage("Get account")
  public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
    String email =
        SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
            : "";

    User currentUser = this.userService.getUserByUserName(email);
    ResLoginDTO.UserLogin u = new ResLoginDTO.UserLogin();
    ResLoginDTO.UserGetAccount userGetAccount = new UserGetAccount();

    if (currentUser != null) {
      u.setId(currentUser.getId());
      u.setEmail(currentUser.getEmail());
      u.setName(currentUser.getName());
      u.setRole(currentUser.getRole());
      userGetAccount.setUser(u);
    }

    return ResponseEntity.ok().body(userGetAccount);
  }

  @GetMapping("/auth/refresh")
  @ApiMessage("Get user by refresh token")
  public ResponseEntity<ResLoginDTO> getRefreshToken(
      @CookieValue(name = "refresh_token") String refresh_token) throws IdInvalidException {
    Jwt decodedRefreshToken = this.securityUtil.checkValidRefreshToken(refresh_token);
    String email = decodedRefreshToken.getSubject();

    User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);

    if (currentUser == null) {
      throw new IdInvalidException("refresh token khong hop le!");
    }

    ResLoginDTO res = new ResLoginDTO();
    //Lay user hien tai dang dang nhap de gan cho user trong response
    ResLoginDTO.UserLogin u = new ResLoginDTO.UserLogin(
        currentUser.getId(), currentUser.getEmail(), currentUser.getName(), currentUser.getRole());
    res.setUser(u);

    // create token
    String access_token = this.securityUtil.createAccessToken(email, res);

    res.setAccessToken(access_token);

    String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

//    update user refresh token
    this.userService.updateUserToken(new_refresh_token, email);

//    set cookies
    ResponseCookie resCookies = ResponseCookie.from("refresh_token", new_refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);
  }

  @PostMapping("/auth/logout")
  @ApiMessage("Logout user")
  public ResponseEntity<Void> logout() throws IdInvalidException {
    String email =
        SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
            : null;

    if (email == null) {
      throw new IdInvalidException("Access Token khong hop le");
    }

    this.userService.updateUserToken(null, email);

    ResponseCookie refresh_token = ResponseCookie.from("refresh_token", null).maxAge(0).build();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refresh_token.toString()).build();
  }

}
