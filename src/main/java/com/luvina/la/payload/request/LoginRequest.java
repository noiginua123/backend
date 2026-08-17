/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * LoginRequest.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.payload.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload chứa thông tin yêu cầu đăng nhập từ người dùng.
 *
 * @author thanhvinh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
