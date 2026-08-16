/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * AuthEntryPoint.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config.jwt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Lớp xử lý lỗi khi người dùng chưa xác thực (401 Unauthorized) cố gắng truy cập tài nguyên được bảo vệ.
 *
 * @author thanhvinh
 */
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Bắn lỗi 401 Unauthorized về phía Client khi xác thực thất bại.
     *
     * @param request Yêu cầu HTTP gửi từ Client
     * @param response Phản hồi HTTP trả về Client
     * @param authException Ngoại lệ xác thực của Spring Security
     * @throws IOException Lỗi I/O khi gửi phản hồi
     * @throws ServletException Lỗi Servlet
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
