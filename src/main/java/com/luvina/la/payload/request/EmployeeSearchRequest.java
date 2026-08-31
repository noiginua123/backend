/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeSearchRequest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Chứa các tham số tìm kiếm, sắp xếp và phân trang của ADM002.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchRequest {

    private String employeeName;

    private String departmentId;

    private String ordEmployeeName;

    private String ordCertificationName;

    private String ordEndDate;

    private String prioritySort;

    private String offset;

    private String limit;
}
