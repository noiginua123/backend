/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeSearchCriteria.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Chứa các tiêu chí tìm kiếm ADM002 đã được validate và chuẩn hóa.
 *
 * @author thanhvinh
 */
@Getter
@AllArgsConstructor
public class EmployeeSearchCriteria {

    private String employeeName;

    private Long departmentId;

    private String ordEmployeeName;

    private String ordCertificationName;

    private String ordEndDate;

    private String prioritySort;

    private int offset;

    private int limit;
}
