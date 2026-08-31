/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeService.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service;

import com.luvina.la.exception.AppException;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.payload.response.ListEmployeeResponse;

/**
 * Interface cung cấp các nghiệp vụ liên quan đến nhân viên.
 *
 * @author thanhvinh
 */
public interface EmployeeService {

    /**
     * Kiểm tra điều kiện đầu vào, tìm kiếm và phân trang danh sách nhân viên,
     * bao gồm cả tài khoản admin.
     *
     * @param request Request chứa điều kiện tìm kiếm, sắp xếp và phân trang
     * @return Response chứa code thành công, tổng số bản ghi và danh sách nhân viên
     * @throws AppException Khi sort, offset, limit, departmentId hoặc employeeName không hợp lệ
     */
    ListEmployeeResponse searchEmployees(EmployeeSearchRequest request);
}
