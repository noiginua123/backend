/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * ListDepartmentResponse.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;
import java.util.List;

import com.luvina.la.dto.DepartmentDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response cho API Get List Departments (trường hợp thành công).
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListDepartmentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã code (200 khi thành công). */
    private Long code;

    /** Danh sách phòng ban. */
    private List<DepartmentDTO> departments;
}
