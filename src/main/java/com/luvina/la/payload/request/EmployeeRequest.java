/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRequest.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.payload.request;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Payload nhận từ FE khi thêm mới nhân viên (ADM004/ADM005).
 *
 * <p>Toàn bộ trường đều là kiểu String để thuận tiện validate (Server không tin cậy kiểu dữ liệu từ client).
 * Danh sách certifications có 0 hoặc 1 phần tử vì giao diện ADM004 chỉ có một khối chứng chỉ.</p>
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

    /** Danh sách chứng chỉ đính kèm (0 hoặc 1 phần tử). */
    private List<CertificationRequest> certifications;
}
