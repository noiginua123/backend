/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeDetailResponse.java, 08/09/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luvina.la.dto.EmployeeCertificationDTO;
import com.luvina.la.dto.EmployeeDetailDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response trả về thông tin chi tiết nhân viên (ADM003 / ADM004).
 *
 * @author thanhvinh
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã trạng thái (200 khi thành công). */
    private Long code;

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

    /**
     * Khởi tạo EmployeeDetailResponse từ mã code và DTO chi tiết nhân viên.
     *
     * @param code Mã trạng thái trả về
     * @param dto DTO chứa dữ liệu chi tiết nhân viên từ service
     */
    public EmployeeDetailResponse(Long code, EmployeeDetailDTO dto) {
        this.code = code;
        if (dto != null) {
            this.employeeId = dto.getEmployeeId();
            this.employeeLoginId = dto.getEmployeeLoginId();
            this.employeeName = dto.getEmployeeName();
            this.employeeNameKana = dto.getEmployeeNameKana();
            this.employeeBirthDate = dto.getEmployeeBirthDate();
            this.employeeEmail = dto.getEmployeeEmail();
            this.employeeTelephone = dto.getEmployeeTelephone();
            this.departmentId = dto.getDepartmentId();
            this.departmentName = dto.getDepartmentName();
            this.certifications = dto.getCertifications();
        }
    }
}
