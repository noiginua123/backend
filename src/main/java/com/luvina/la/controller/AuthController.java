/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * AuthController.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import com.luvina.la.config.jwt.AuthUserDetails;
import com.luvina.la.config.jwt.JwtTokenProvider;
import com.luvina.la.config.jwt.UserDetailsServiceImpl;
import com.luvina.la.payload.request.LoginRequest;
import com.luvina.la.payload.response.LoginResponse;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller tiếp nhận và xử lý các yêu cầu xác thực người dùng (Đăng nhập, kiểm tra Token).
 *
 * @author thanhvinh
 */
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Khởi tạo AuthController với các dependency cần thiết.
     *
     * @param authenticationManager Quản lý xác thực của Spring Security
     * @param tokenProvider Provider xử lý token JWT
     * @param userDetailsService Service tải thông tin người dùng
     */
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * API xử lý đăng nhập hệ thống và cấp phát JWT token.
     *
     * @param loginRequest Payload chứa username và password người dùng gửi lên
     * @param request Yêu cầu HTTP từ Client
     * @return LoginResponse chứa chuỗi token nếu thành công hoặc mã lỗi nếu thất bại
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = tokenProvider.generateToken((AuthUserDetails) authentication.getPrincipal());
            return new LoginResponse(accessToken);
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            log.warn("Login failed for user {}: {}", loginRequest.getUsername(), ex.getMessage());
            errors.put("code", "100");
        } catch (AuthenticationException ex) {
            log.warn("Authentication error: {}", ex.getMessage());
            errors.put("code", "100");
        } catch (Exception ex) {
            log.error("Unknown error during login: ", ex);
            errors.put("code", "000");
        }
        return new LoginResponse(errors);
    }

    /**
     * API kiểm tra token xác thực người dùng có hợp lệ hay không.
     *
     * @return Map chứa thông báo kết quả kiểm tra token
     */
    @GetMapping("/test-auth")
    public Map<String, String> testAuth() {
        Map<String, String> testData = new HashMap<>();
        testData.put("msg", "Token is valid");
        return testData;
    }
}
