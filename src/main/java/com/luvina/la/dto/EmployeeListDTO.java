/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeListDTO.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO chứa thông tin phẳng của nhân viên trả về cho client.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID nhân viên (employees.employee_id). */
    private Long employeeId;

    /** Tên nhân viên (employees.employee_name). */
    private String employeeName;

    /** Ngày sinh nhân viên định dạng yyyy/MM/dd. */
    private String employeeBirthDate;

    /** Tên phòng ban (departments.department_name). */
    private String departmentName;

    /** Địa chỉ email nhân viên (employees.employee_email). */
    private String employeeEmail;

    /** Số điện thoại liên hệ (employees.employee_telephone). */
    private String employeeTelephone;

    /** Tên chứng chỉ cao nhất (certifications.certification_name). */
    private String certificationName;

    /** Ngày kết thúc chứng chỉ định dạng yyyy/MM/dd. */
    private String endDate;

    /** Điểm số chứng chỉ (employees_certifications.score). */
    private BigDecimal score;
}
