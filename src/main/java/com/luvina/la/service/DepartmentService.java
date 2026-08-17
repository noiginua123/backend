/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentService.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.service;

import com.luvina.la.payload.response.ListDepartmentResponse;

/**
 * Interface nghiệp vụ cho chức năng phòng ban.
 *
 * @author thanhvinh
 */
public interface DepartmentService {

    /**
     * Lấy danh sách tất cả phòng ban.
     *
     * @return ListDepartmentResponse gồm code = 200 và danh sách phòng ban.
     */
    ListDepartmentResponse getListDepartments();
}
