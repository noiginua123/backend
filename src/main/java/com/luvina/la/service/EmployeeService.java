/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeService.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service;

import com.luvina.la.exception.AppException;
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
     * @param employeeName Tên cần tìm gần đúng, hoặc chuỗi rỗng nếu không tìm theo tên
     * @param departmentId ID phòng ban dạng chuỗi, hoặc chuỗi rỗng nếu lấy tất cả
     * @param ordEmployeeName ASC/DESC để sort tên, hoặc chuỗi rỗng
     * @param ordCertificationName ASC/DESC để sort chứng chỉ, hoặc chuỗi rỗng
     * @param ordEndDate ASC/DESC để sort ngày hết hạn, hoặc chuỗi rỗng
     * @param prioritySort Cột ưu tiên làm tiêu chí sort chính (employeeName / certificationName / endDate)
     * @param offset Vị trí bản ghi bắt đầu, phải là số nguyên không âm
     * @param limit Số bản ghi tối đa, phải là số nguyên dương
     * @return Response chứa code thành công, tổng số bản ghi và danh sách nhân viên
     * @throws AppException Khi sort, offset, limit, departmentId hoặc employeeName không hợp lệ
     */
    ListEmployeeResponse searchEmployees(String employeeName,
                                         String departmentId,
                                         String ordEmployeeName,
                                         String ordCertificationName,
                                         String ordEndDate,
                                         String prioritySort,
                                         String offset,
                                         String limit);
}
