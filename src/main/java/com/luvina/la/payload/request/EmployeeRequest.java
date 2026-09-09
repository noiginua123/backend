/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRequest.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.payload.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Payload chứa thông tin nhân viên (ADM004 - Thêm mới và Chỉnh sửa).
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** ID nhân viên (ＩＤ) - dùng cho luồng chỉnh sửa (ADM004). */
    private String employeeId;

    /** Tên tài khoản đăng nhập (アカウント名). */
    private String employeeLoginId;

    /** Họ tên nhân viên (氏名). */
    private String employeeName;

    /** Họ tên Katakana (カタカナ氏名). */
    private String employeeNameKana;

    /** Ngày sinh theo định dạng yyyy/MM/dd (生年月日). */
    private String employeeBirthDate;

    /** Địa chỉ email (メールアドレス). */
    private String employeeEmail;

    /** Số điện thoại (電話番号). */
    private String employeeTelephone;

    /** Mật khẩu đăng nhập (パスワード). */
    private String employeeLoginPassword;

    /** ID phòng ban / nhóm (グループ). */
    private String departmentId;

    /** Danh sách chứng chỉ đính kèm (chấp nhận cả Object đơn lẻ lẫn Array). */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<CertificationRequest> certifications;

    /**
     * Lấy employeeId dưới dạng Long nếu hợp lệ.
     *
     * @return ID nhân viên kiểu Long hoặc null nếu không hợp lệ
     */
    public Long getEmployeeIdAsLong() {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(employeeId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
