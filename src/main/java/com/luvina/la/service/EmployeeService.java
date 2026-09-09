/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeService.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service;

import java.util.List;

import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.payload.request.EmployeeRequest;

/**
 * Giao diện cung cấp các nghiệp vụ truy vấn và thêm mới dữ liệu nhân viên.
 *
 * @author thanhvinh
 */
public interface EmployeeService {

    /**
     * Đếm tổng số nhân viên thỏa mãn điều kiện tìm kiếm, loại trừ tài khoản admin.
     *
     * @param employeeName Mẫu LIKE tên nhân viên đã escape, hoặc null nếu không lọc
     * @param departmentId ID phòng ban, hoặc null nếu không lọc
     * @return Tổng số nhân viên thỏa mãn
     */
    long getTotalRecords(String employeeName, Long departmentId);

    /**
     * Lấy danh sách nhân viên đã sắp xếp và phân trang, loại trừ tài khoản admin.
     *
     * @param employeeName         Mẫu LIKE tên nhân viên đã escape, hoặc null nếu không lọc
     * @param departmentId         ID phòng ban, hoặc null nếu không lọc
     * @param ordEmployeeName      Hướng sắp xếp theo tên nhân viên (ASC/DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC/DESC)
     * @param ordEndDate           Hướng sắp xếp theo ngày hết hạn (ASC/DESC)
     * @param prioritySort         Cột sắp xếp ưu tiên
     * @param limit                Số bản ghi tối đa
     * @param offset               Vị trí bản ghi bắt đầu
     * @return Danh sách nhân viên
     */
    List<EmployeeListDTO> getEmployees(String employeeName,
            Long departmentId,
            String ordEmployeeName,
            String ordCertificationName,
            String ordEndDate,
            String prioritySort,
            int limit,
            int offset);

    /**
     * Thêm mới nhân viên (ADM004) kèm chứng chỉ (nếu có). Mật khẩu được mã hóa
     * trước khi lưu.
     *
     * @param employeeRequest Dữ liệu nhân viên đã qua validate
     * @return ID của nhân viên vừa được tạo
     */
    Long addEmployee(EmployeeRequest employeeRequest);

    /**
     * Cập nhật thông tin nhân viên (ADM004) kèm chứng chỉ nếu có.
     * Mật khẩu chỉ được cập nhật khi có giá trị mới (được mã hóa BCrypt trước khi lưu).
     *
     * @param employeeRequest Dữ liệu nhân viên đã qua validate
     * @return ID của nhân viên vừa được cập nhật
     */
    Long updateEmployee(EmployeeRequest employeeRequest);

    /**
     * Kiểm tra sự tồn tại của nhân viên theo ID.
     *
     * @param employeeId ID nhân viên cần kiểm tra
     * @return true nếu nhân viên tồn tại, ngược lại false
     */
    boolean checkExistsEmployeeById(Long employeeId);

    /**
     * Lấy thông tin chi tiết một nhân viên theo ID (ADM003 / ADM004).
     *
     * @param employeeId ID của nhân viên cần lấy chi tiết
     * @return DTO chứa thông tin chi tiết nhân viên và danh sách chứng chỉ
     */
    EmployeeDetailDTO getEmployeeDetail(Long employeeId);

    /**
     * Xóa một nhân viên và toàn bộ chứng chỉ liên quan khỏi hệ thống (ADM003).
     *
     * @param employeeId ID của nhân viên cần xóa
     */
    void deleteEmployee(Long employeeId);
}
