/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentDTO.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) chứa thông tin phòng ban.
 *
 * @author thanhvinh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long departmentId;
    private String departmentName;
}
