/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * SecurityConfiguration.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config;

import com.luvina.la.config.jwt.AuthEntryPoint;
import com.luvina.la.config.jwt.JwtTokenFilter;
import com.luvina.la.config.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Lớp cấu hình bảo mật Spring Security và lọc JWT cho ứng dụng.
 *
 * @author thanhvinh
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Khởi tạo SecurityConfiguration với JwtTokenProvider và UserDetailsService.
     *
     * @param tokenProvider Provider tạo và xác thực Token JWT
     * @param userDetailsService Service tải thông tin người dùng
     */
    public SecurityConfiguration(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Tạo Bean Filter chặn bắt request để xác thực JWT.
     *
     * @return JwtTokenFilter
     */
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(this.tokenProvider, this.userDetailsService);
    }

    /**
     * Tạo Bean AuthenticationManager từ AuthenticationConfiguration.
     *
     * @param authenticationConfiguration Cấu hình Authentication
     * @return AuthenticationManager
     * @throws Exception Ngoại lệ nếu có lỗi trong quá trình lấy manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Tạo Bean PasswordEncoder sử dụng thuật toán BCrypt.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình chuỗi lọc bảo mật SecurityFilterChain cho các HTTP requests.
     *
     * @param http Đối tượng HttpSecurity để cấu hình
     * @return SecurityFilterChain đã được build
     * @throws Exception Ngoại lệ khi xây dựng filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Bật CORS và tắt CSRF cho kiến trúc Stateless REST API
        http.cors().and().csrf().disable();

        // Cấu hình headers và tắt frameOptions
        http.headers().frameOptions().disable();

        // Thiết lập quản lý phiên làm việc Stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Phân quyền cho các Endpoints
        http.authorizeRequests(authz -> authz
                // Các endpoint công khai
                .antMatchers(Constants.ENDPOINTS_PUBLIC).permitAll()
                // Các endpoint yêu cầu quyền USER hoặc ADMIN
                .antMatchers(Constants.ENDPOINTS_WITH_ROLE).hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
        );

        // Đăng ký EntryPoint xử lý lỗi xác thực 401
        http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());

        // Đăng ký JWT Filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Cấu hình CorsFilter cho phép truy cập chéo tên miền (Cross-Origin).
     *
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        if (Constants.IS_CROSS_ALLOW) {
            config.addAllowedOriginPattern("*");
        } else {
            config.addAllowedOrigin("*");
        }
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
