/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * Constants.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config;

/**
 * Lớp định nghĩa các hằng số dùng chung trong toàn bộ hệ thống.
 *
 * @author thanhvinh
 */
public final class Constants {

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final boolean IS_CROSS_ALLOW = true;

    public static final String JWT_SECRET = "Luvina-Academe";
    public static final long JWT_EXPIRATION = 160 * 60 * 60; // 7 days in seconds

    // Danh sách các endpoints công khai không cần xác thực
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/login/**",
            "/error/**"
    };

    // Danh sách các endpoints yêu cầu quyền hạn người dùng
    public static final String[] ENDPOINTS_WITH_ROLE = new String[] {
            "/user/**",
            "/employee/**",
            "/department/**",
            "/certification/**"
    };

    // Danh sách các thuộc tính của Employee được đóng gói vào claims của JWT Token
    public static final String[] ATTRIBUTIES_TO_TOKEN = new String[] {
            "employeeId",
            "departmentId",
            "employeeName",
            "employeeNameKana",
            "employeeLoginId",
            "employeeEmail",
            "employeeTelephone",
            "role"
    };

    /**
     * Private constructor để ngăn chặn việc khởi tạo instance.
     */
    private Constants() {
    }
}
