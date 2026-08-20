/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeService.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service;

import com.luvina.la.payload.response.ListEmployeeResponse;

/**
 * Interface cung cấp các nghiệp vụ liên quan đến nhân viên.
 *
 * @author thanhvinh
 */
public interface EmployeeService {

    /**
     * Tìm kiếm, sắp xếp và phân trang danh sách nhân viên.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @param departmentId ID phòng ban
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên (ASC / DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC / DESC)
     * @param ordEndDate Hướng sắp xếp theo ngày kết thúc chứng chỉ (ASC / DESC)
     * @param offset Vị trí bắt đầu bản ghi (chuỗi số nguyên >= 0)
     * @param limit Số lượng bản ghi mỗi trang (chuỗi số nguyên > 0)
     * @return ListEmployeeResponse chứa tổng số bản ghi và danh sách nhân viên
     */
    ListEmployeeResponse searchEmployees(String employeeName,
                                        String departmentId,
                                        String ordEmployeeName,
                                        String ordCertificationName,
                                        String ordEndDate,
                                        String offset,
                                        String limit);
}
