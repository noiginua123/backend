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

    /** Tên đăng nhập quản trị viên bị loại khỏi danh sách ADM002. */
    public static final String ADMIN_LOGIN_ID = "admin";

    /** Vị trí bản ghi mặc định của ADM002. */
    public static final int DEFAULT_EMPLOYEE_OFFSET = 0;

    /** Số bản ghi cố định trên mỗi trang ADM002. */
    public static final int DEFAULT_EMPLOYEE_PAGE_SIZE = 20;

    // =========================================================================
    // Field Labels (Nhãn trường trong thông báo lỗi/thông tin)
    // =========================================================================

    /** Nhãn trường ID người dùng / nhân viên. */
    public static final String FIELD_LABEL_ID = "ＩＤ";

    /** Nhãn trường tên đăng nhập / account. */
    public static final String FIELD_LABEL_LOGIN_ID = "アカウント名";

    /** Nhãn trường họ tên dùng trong tham số message. */
    public static final String FIELD_LABEL_FULLNAME = "氏名";

    /** Nhãn trường họ tên Katakana. */
    public static final String FIELD_LABEL_FULLNAME_KANA = "カタカナ氏名";

    /** Nhãn trường ngày sinh. */
    public static final String FIELD_LABEL_BIRTH_DATE = "生年月日";

    /** Nhãn trường địa chỉ email. */
    public static final String FIELD_LABEL_EMAIL = "メールアドレス";

    /** Nhãn trường số điện thoại. */
    public static final String FIELD_LABEL_TEL = "電話番号";

    /** Nhãn trường mật khẩu. */
    public static final String FIELD_LABEL_PASSWORD = "パスワード";

    /** Nhãn trường mật khẩu xác nhận. */
    public static final String FIELD_LABEL_PASSWORD_CONFIRM = "パスワード（確認）";

    /** Nhãn trường phòng ban (ID) dùng trong tham số message. */
    public static final String FIELD_LABEL_DEPARTMENT_ID = "部門ID";

    /** Nhãn trường nhóm / phòng ban. */
    public static final String FIELD_LABEL_GROUP = "グループ";

    /** Nhãn trường offset dùng trong tham số message. */
    public static final String FIELD_LABEL_OFFSET = "オフセット";

    /** Nhãn trường limit dùng trong tham số message. */
    public static final String FIELD_LABEL_LIMIT = "リミット";

    /** Nhãn trường chứng chỉ / bằng cấp. */
    public static final String FIELD_LABEL_CERTIFICATION = "資格";

    /** Nhãn trường ngày cấp chứng chỉ. */
    public static final String FIELD_LABEL_START_DATE = "資格交付日";

    /** Nhãn trường ngày hết hạn chứng chỉ. */
    public static final String FIELD_LABEL_END_DATE = "失効日";

    /** Nhãn trường điểm số chứng chỉ. */
    public static final String FIELD_LABEL_SCORE = "点数";

    // =========================================================================
    // Error Message Codes (ER001 - ER023)
    // =========================================================================

    /** Mã lỗi chưa nhập trường bắt buộc: Hãy nhập [Tên hạng mục]. */
    public static final String ER001 = "ER001";

    /** Mã lỗi chưa chọn trường bắt buộc: Hãy nhập/chọn [Tên hạng mục]. */
    public static final String ER002 = "ER002";

    /** Mã lỗi dữ liệu đã tồn tại: [Tên hạng mục] đã tồn tại. */
    public static final String ER003 = "ER003";

    /** Mã lỗi dữ liệu không tồn tại: [Tên hạng mục] không tồn tại. */
    public static final String ER004 = "ER004";

    /** Mã lỗi sai định dạng: Hãy nhập [Tên hạng mục] đúng định dạng xxx. */
    public static final String ER005 = "ER005";

    /** Mã lỗi vượt quá độ dài tối đa: [Tên hạng mục] không được vượt quá xxxx ký tự. */
    public static final String ER006 = "ER006";

    /** Mã lỗi độ dài ngoài khoảng: [Tên hạng mục] phải >= xxx và <= xxx ký tự. */
    public static final String ER007 = "ER007";

    /** Mã lỗi chỉ chấp nhận ký tự 1 byte (bán giác): [Tên hạng mục] chỉ chấp nhận ký tự 1 byte. */
    public static final String ER008 = "ER008";

    /** Mã lỗi phải là ký tự kana: [Tên hạng mục] phải là ký tự kana. */
    public static final String ER009 = "ER009";

    /** Mã lỗi phải là ký tự hiragana: [Tên hạng mục] phải là ký tự hiragana. */
    public static final String ER010 = "ER010";

    /** Mã lỗi ngày không hợp lệ: [Tên hạng mục] không hợp lệ. */
    public static final String ER011 = "ER011";

    /** Mã lỗi ngày hết hạn nhỏ hơn hoặc bằng ngày cấp: [Ngày hết hạn] phải lớn hơn [Ngày cấp chứng chỉ]. */
    public static final String ER012 = "ER012";

    /** Mã lỗi chỉnh sửa user không tồn tại: User không tồn tại. */
    public static final String ER013 = "ER013";

    /** Mã lỗi xóa user không tồn tại: User không tồn tại. */
    public static final String ER014 = "ER014";

    /** Mã lỗi thao tác database: Hệ thống đang có lỗi. */
    public static final String ER015 = "ER015";

    /** Mã lỗi sai thông tin đăng nhập: [Tên đăng nhập] hoặc [Mật khẩu] bị sai. */
    public static final String ER016 = "ER016";

    /** Mã lỗi mật khẩu xác nhận không khớp: [Mật khẩu xác nhận] không đúng. */
    public static final String ER017 = "ER017";

    /** Mã lỗi trường bán giác / số nửa độ rộng: [Tên hạng mục] phải là số halfsize. */
    public static final String ER018 = "ER018";

    /** Mã lỗi sai định dạng tên đăng nhập: [Tên đăng nhập] chỉ chấp nhận (a-z, A-Z, 0-9, _); ký tự đầu không phải số. */
    public static final String ER019 = "ER019";

    /** Mã lỗi kiểm tra user admin: Không thể xóa user admin. */
    public static final String ER020 = "ER020";

    /** Mã lỗi thứ tự sắp xếp không hợp lệ: Thứ tự sắp xếp phải là ASC, DESC. */
    public static final String ER021 = "ER021";

    /** Mã lỗi trang không tồn tại: Page not found. */
    public static final String ER022 = "ER022";

    /** Mã lỗi hệ thống: Hệ thống đang có lỗi. */
    public static final String ER023 = "ER023";

    // =========================================================================
    // Information / Action Message Codes (MSG001 - MSG005)
    // =========================================================================

    /** Mã thông báo đăng ký user thành công: ユーザの登録が完了しました。 */
    public static final String MSG001 = "MSG001";

    /** Mã thông báo cập nhật user thành công: ユーザの更新が完了しました。 */
    public static final String MSG002 = "MSG002";

    /** Mã thông báo xóa user thành công: ユーザの削除が完了しました。 */
    public static final String MSG003 = "MSG003";

    /** Mã thông báo xác nhận trước khi xóa: 削除しますが、よろしいでしょうか。 */
    public static final String MSG004 = "MSG004";

    /** Mã thông báo không tìm thấy user: 検索条件に該当するユーザが見つかりません。 */
    public static final String MSG005 = "MSG005";
}
