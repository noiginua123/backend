/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * AppException.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Ngoại lệ tùy chỉnh cho các lỗi nghiệp vụ trong hệ thống.
 *
 * @author thanhvinh
 */
@Getter
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Mã lỗi nghiệp vụ (ví dụ: ER018, ER021). */
    private final String code;

    /** Danh sách tham số thay thế trong thông báo lỗi. */
    private final transient List<Object> params;

    /**
     * Khởi tạo AppException chỉ với mã lỗi (danh sách params rỗng).
     *
     * @param code Mã lỗi
     */
    public AppException(String code) {
        this(code, new ArrayList<>());
    }

    /**
     * Khởi tạo AppException với mã lỗi và danh sách tham số.
     *
     * @param code Mã lỗi
     * @param params Danh sách tham số truyền vào thông báo lỗi
     */
    public AppException(String code, List<Object> params) {
        super(code);
        this.code = code;
        this.params = params != null ? params : new ArrayList<>();
    }
}
