/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * JwtTokenFilter.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter chặn bắt các HTTP request để trích xuất và xác thực JWT token từ Authorization Header.
 *
 * @author thanhvinh
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Khởi tạo JwtTokenFilter với JwtTokenProvider và UserDetailsService.
     *
     * @param tokenProvider Provider tạo và xác thực token JWT
     * @param userDetailsService Service tải thông tin người dùng
     */
    public JwtTokenFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Thực thi lọc request, kiểm tra token và nạp Authentication vào SecurityContextHolder nếu hợp lệ.
     *
     * @param request Yêu cầu HTTP
     * @param response Phản hồi HTTP
     * @param chain Chuỗi Filter
     * @throws IOException Ngoại lệ I/O
     * @throws ServletException Ngoại lệ Servlet
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        try {
            String jwt = this.getJwtFromRequest(request);
            UsernamePasswordAuthenticationToken authentication = null;
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    String username = this.tokenProvider.getUsernameFromJWT(jwt);
                    UserDetails userDetail = this.userDetailsService.loadUserByUsername(username);

                    if (StringUtils.hasText(userDetail.getUsername())) {
                        authentication = new UsernamePasswordAuthenticationToken(
                                userDetail,
                                null,
                                userDetail.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } else {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Failed on set user authentication", ex);
        }

        chain.doFilter(request, response);
    }

    /**
     * Trích xuất JWT Token từ trường Authorization trong Header của HTTP Request.
     *
     * @param request Yêu cầu HTTP
     * @return Chuỗi JWT Token (đã cắt bỏ tiền tố "Bearer "), hoặc null nếu không tồn tại
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Kiểm tra và lấy chuỗi token từ header
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
