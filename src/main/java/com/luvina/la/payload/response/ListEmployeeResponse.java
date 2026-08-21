/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * ListEmployeeResponse.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;
import java.util.List;

import com.luvina.la.dto.EmployeeListDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response cho API Get List Employees.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListEmployeeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã code, 200 khi thành công. */
    private Long code;

    /** Tổng số bản ghi thỏa mãn điều kiện tìm kiếm. */
    private Long totalRecords;

    /** Danh sách nhân viên. */
    private List<EmployeeListDTO> employees;
}
