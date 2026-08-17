/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentDTO.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO chứa thông tin phòng ban để trả về cho client.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID phòng ban (departments.department_id). */
    private Long departmentId;

    /** Tên phòng ban (departments.department_name). */
    private String departmentName;
}
