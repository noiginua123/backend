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

    /** ID nhân viên. */
    private Long employeeId;

    /** Tên nhân viên. */
    private String employeeName;

    /** Ngày sinh nhân viên định dạng yyyy/MM/dd. */
    private String employeeBirthDate;

    /** Tên phòng ban. */
    private String departmentName;

    /** Địa chỉ email nhân viên. */
    private String employeeEmail;

    /** Số điện thoại nhân viên. */
    private String employeeTelephone;

    /** Tên chứng chỉ cao nhất. */
    private String certificationName;

    /** Ngày hết hạn chứng chỉ định dạng yyyy/MM/dd. */
    private String endDate;

    /** Điểm số chứng chỉ. */
    private BigDecimal score;

    /** Quyền của nhân viên: 1 là admin, 0 là user. */
    private Integer role;
}
