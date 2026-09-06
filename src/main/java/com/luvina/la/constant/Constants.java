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

    /**
     * Khởi tạo private để ngăn việc tạo thể hiện của lớp hằng số.
     */
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

    /** Danh sách các endpoint công khai không cần xác thực. */
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/login/**",
            "/error/**"
    };

    /** Danh sách các endpoint yêu cầu quyền hạn người dùng. */
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

    /** Mã trả về khi thành công. */
    public static final Long CODE_SUCCESS = 200L;

    /** Mã trả về khi lỗi hệ thống. */
    public static final Long CODE_ERROR = 500L;

    /** Số ký tự tối đa của điều kiện tìm kiếm họ tên nhân viên. */
    public static final int EMPLOYEE_NAME_MAX_LENGTH = 125;

    /** Tên đăng nhập quản trị viên bị loại khỏi danh sách ADM002. */
    public static final String ADMIN_LOGIN_ID = "admin";

    /** Vị trí bản ghi mặc định của ADM002. */
    public static final int DEFAULT_EMPLOYEE_OFFSET = 0;

    /** Số bản ghi cố định trên mỗi trang ADM002. */
    public static final int DEFAULT_EMPLOYEE_PAGE_SIZE = 20;

    // =========================================================================
    // ADM004 - Thêm nhân viên (validate + tạo mới)
    // =========================================================================

    /** Số ký tự tối đa của Login ID và số điện thoại. */
    public static final int MAX_LENGTH_50 = 50;

    /** Số ký tự tối đa của họ tên, tên kana và email. */
    public static final int MAX_LENGTH_125 = 125;

    /** Độ dài tối thiểu của mật khẩu. */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /** Độ dài tối đa của mật khẩu. */
    public static final int PASSWORD_MAX_LENGTH = 50;

    /** Quyền người dùng thông thường (employee_role = 0). */
    public static final int ROLE_USER = 0;

    /** Quyền quản trị viên (employee_role = 1). */
    public static final int ROLE_ADMIN = 1;

    /** Định dạng ngày tháng hiển thị và lưu trữ. */
    public static final String DATE_FORMAT = "yyyy/MM/dd";

    /** Regex Login ID: bắt đầu bằng chữ hoặc gạch dưới, sau đó là chữ / số / gạch dưới. */
    public static final String REGEX_LOGIN_ID = "^[a-zA-Z_][a-zA-Z0-9_]*$";

    /** Regex Katakana chỉ cho phép half-size (半角カタカナ \uFF65-\uFF9F) và khoảng trắng half-width. */
    public static final String REGEX_KATAKANA = "^[\\uFF65-\\uFF9F ]+$";

    /** Regex ký tự 1 byte (half-size) dùng cho số điện thoại. */
    public static final String REGEX_HALF_SIZE = "^[\\u0020-\\u007E]+$";

    // =========================================================================
    // Error Message Codes (ER001 - ER023)
    // =========================================================================

    /** Mã lỗi chưa nhập trường bắt buộc. */
    public static final String ER001 = "ER001";

    /** Mã lỗi chưa chọn trường bắt buộc. */
    public static final String ER002 = "ER002";

    /** Mã lỗi dữ liệu đã tồn tại. */
    public static final String ER003 = "ER003";

    /** Mã lỗi dữ liệu không tồn tại. */
    public static final String ER004 = "ER004";

    /** Mã lỗi sai định dạng. */
    public static final String ER005 = "ER005";

    /** Mã lỗi vượt quá độ dài tối đa. */
    public static final String ER006 = "ER006";

    /** Mã lỗi độ dài ngoài khoảng cho phép. */
    public static final String ER007 = "ER007";

    /** Mã lỗi chỉ chấp nhận ký tự 1 byte (bán giác). */
    public static final String ER008 = "ER008";

    /** Mã lỗi phải là ký tự kana. */
    public static final String ER009 = "ER009";

    /** Mã lỗi phải là ký tự hiragana. */
    public static final String ER010 = "ER010";

    /** Mã lỗi ngày không hợp lệ. */
    public static final String ER011 = "ER011";

    /** Mã lỗi ngày hết hạn nhỏ hơn hoặc bằng ngày cấp. */
    public static final String ER012 = "ER012";

    /** Mã lỗi chỉnh sửa người dùng không tồn tại. */
    public static final String ER013 = "ER013";

    /** Mã lỗi xóa người dùng không tồn tại. */
    public static final String ER014 = "ER014";

    /** Mã lỗi thao tác cơ sở dữ liệu. */
    public static final String ER015 = "ER015";

    /** Mã lỗi sai thông tin đăng nhập. */
    public static final String ER016 = "ER016";

    /** Mã lỗi mật khẩu xác nhận không khớp. */
    public static final String ER017 = "ER017";

    /** Mã lỗi trường bán giác / số nửa độ rộng. */
    public static final String ER018 = "ER018";

    /** Mã lỗi sai định dạng tên đăng nhập. */
    public static final String ER019 = "ER019";

    /** Mã lỗi kiểm tra người dùng admin. */
    public static final String ER020 = "ER020";

    /** Mã lỗi thứ tự sắp xếp không hợp lệ. */
    public static final String ER021 = "ER021";

    /** Mã lỗi trang không tồn tại. */
    public static final String ER022 = "ER022";

    /** Mã lỗi hệ thống. */
    public static final String ER023 = "ER023";

    // =========================================================================
    // Information / Action Message Codes (MSG001 - MSG005)
    // =========================================================================

    /** Mã thông báo đăng ký người dùng thành công. */
    public static final String MSG001 = "MSG001";

    /** Mã thông báo cập nhật người dùng thành công. */
    public static final String MSG002 = "MSG002";

    /** Mã thông báo xóa người dùng thành công. */
    public static final String MSG003 = "MSG003";

    /** Mã thông báo xác nhận trước khi xóa. */
    public static final String MSG004 = "MSG004";

    /** Mã thông báo không tìm thấy người dùng. */
    public static final String MSG005 = "MSG005";
}
