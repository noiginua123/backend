/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * Constants.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.constant;

/**
 * Lớp định nghĩa các hằng số dùng chung trong toàn bộ hệ thống.
 *
 * @author thanhvinh
 */
public final class Constants {

    private Constants() {
    }

    /** Profile môi trường phát triển (Dev). */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

    /** Profile môi trường production (Prod). */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    /** Cho phép CORS cross-origin. */
    public static final boolean IS_CROSS_ALLOW = true;

    /** Khóa bí mật JWT. */
    public static final String JWT_SECRET = "Luvina-Academe";

    /** Thời hạn token JWT (7 ngày tính bằng giây). */
    public static final long JWT_EXPIRATION = 160 * 60 * 60;

    /** Danh sách các endpoints công khai không cần xác thực. */
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/login/**",
            "/error/**"
    };

    /** Danh sách các endpoints yêu cầu quyền hạn người dùng. */
    public static final String[] ENDPOINTS_WITH_ROLE = new String[] {
            "/user/**",
            "/employee/**",
            "/employee",
            "/employees/**",
            "/employees",
            "/department/**",
            "/department",
            "/departments/**",
            "/departments",
            "/certification/**",
            "/certifications/**"
    };

    /** Danh sách các thuộc tính của Employee được đóng gói vào claims của JWT Token. */
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

    /** Code trả về khi thành công. */
    public static final Long CODE_SUCCESS = 200L;

    /** Code trả về khi lỗi hệ thống. */
    public static final Long CODE_ERROR = 500L;

    /** Số ký tự tối đa của điều kiện tìm kiếm họ tên nhân viên. */
    public static final int EMPLOYEE_NAME_MAX_LENGTH = 125;

    /** Nhãn trường họ tên dùng trong tham số message. */
    public static final String FIELD_LABEL_FULLNAME = "氏名";

    /** Nhãn trường phòng ban dùng trong tham số message. */
    public static final String FIELD_LABEL_DEPARTMENT_ID = "部門ID";

    /** Mã lỗi vượt quá độ dài tối đa. */
    public static final String ER006 = "ER006";

    /** Mã lỗi trường bán giác / số nửa độ rộng. */
    public static final String ER018 = "ER018";

    /** Mã lỗi thứ tự sắp xếp không hợp lệ. */
    public static final String ER021 = "ER021";

    /** Mã lỗi hệ thống. */
    public static final String ER023 = "ER023";
}
