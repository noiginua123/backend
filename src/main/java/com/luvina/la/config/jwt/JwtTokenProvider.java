/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * JwtTokenProvider.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luvina.la.constant.Constants;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Lớp cung cấp các tiện ích tạo (generate), bóc tách và xác thực (validate) JWT token.
 *
 * @author thanhvinh
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * Tạo JWT token mới cho người dùng sau khi xác thực thành công.
     *
     * @param userDetails Thông tin chi tiết của người dùng đã xác thực
     * @return Chuỗi JWT Token được ký số bởi thuật toán HMAC512
     */
    public String generateToken(AuthUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Constants.JWT_EXPIRATION * 1000);

        return JWT.create()
                .withIssuer("self")
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withSubject(userDetails.getEmployee().getEmployeeLoginId())
                .withClaim("employee", toMap(userDetails.getEmployee()))
                .sign(Algorithm.HMAC512(Constants.JWT_SECRET));
    }

    /**
     * Bóc tách các trường thông tin của đối tượng Employee sang Map để đưa vào Token Claim.
     *
     * @param obj Đối tượng cần trích xuất thông tin
     * @return Map chứa các cặp Key - Value thông tin của đối tượng
     */
    private Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (Arrays.stream(Constants.ATTRIBUTIES_TO_TOKEN).anyMatch(field.getName()::equals)) {
                try {
                    map.put(field.getName(), field.get(obj));
                } catch (IllegalAccessException iae) {
                    log.warn("Cannot access field: {}", field.getName(), iae);
                }
            }
        }
        return map;
    }

    /**
     * Trích xuất username (Subject) từ chuỗi JWT token.
     *
     * @param token Chuỗi JWT token
     * @return Tên đăng nhập của người dùng
     */
    public String getUsernameFromJWT(String token) {
        return JWT.decode(token).getSubject();
    }

    /**
     * Kiểm tra tính hợp lệ và hạn sử dụng của JWT Token.
     *
     * @param authToken Chuỗi JWT Token cần kiểm tra
     * @return true nếu token hợp lệ và chưa hết hạn, ngược lại trả về false
     */
    public boolean validateToken(String authToken) {
        try {
            DecodedJWT token = JWT.require(Algorithm.HMAC512(Constants.JWT_SECRET)).build().verify(authToken);

            // Kiểm tra ngày hết hạn của token
            Date expireAt = token.getExpiresAt();
            if (expireAt.compareTo(new Date()) > 0) {
                return true;
            }
        } catch (JWTVerificationException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        }
        return false;
    }
}
