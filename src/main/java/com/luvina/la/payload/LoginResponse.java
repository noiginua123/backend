/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * LoginResponse.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.payload;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload chứa thông tin phản hồi sau khi thực hiện đăng nhập.
 *
 * @author thanhvinh
 */
@Data
@NoArgsConstructor
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String tokenType;
    private Map<String, String> errors = new HashMap<>();

    /**
     * Khởi tạo LoginResponse thành công với accessToken.
     *
     * @param accessToken Chuỗi JWT Token
     */
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }

    /**
     * Khởi tạo LoginResponse thất bại với map các mã lỗi.
     *
     * @param errors Map chứa thông tin lỗi
     */
    public LoginResponse(Map<String, String> errors) {
        this.errors = errors;
    }
}
