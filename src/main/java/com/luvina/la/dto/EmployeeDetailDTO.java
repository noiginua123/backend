/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeDetailDTO.java, 08/09/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO chứa thông tin chi tiết nhân viên phục vụ các màn hình ADM003 và ADM004.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailDTO implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** ID nhân viên. */
    private Long employeeId;

    /** Tên tài khoản đăng nhập. */
    private String employeeLoginId;

    /** Họ và tên nhân viên. */
    private String employeeName;

    /** Họ và tên phiên âm Katakana. */
    private String employeeNameKana;

    /** Ngày sinh nhân viên định dạng yyyy/MM/dd. */
    private String employeeBirthDate;

    /** Địa chỉ email nhân viên. */
    private String employeeEmail;

    /** Số điện thoại nhân viên. */
    private String employeeTelephone;

    /** ID phòng ban/bộ phận. */
    private Long departmentId;

    /** Tên phòng ban/bộ phận. */
    private String departmentName;

    /** Danh sách chứng chỉ tiếng Nhật của nhân viên. */
    private List<EmployeeCertificationDTO> certifications;
}
