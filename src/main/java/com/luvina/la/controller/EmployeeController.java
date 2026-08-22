/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeController.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.service.EmployeeService;

/**
 * Controller tiếp nhận và xử lý các yêu cầu liên quan đến nhân viên.
 *
 * @author thanhvinh
 */
@RestController
@RequestMapping({"/employee", "/employees"})
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * API tìm kiếm, sắp xếp và phân trang danh sách nhân viên.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @param departmentId ID phòng ban
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên (ASC / DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC / DESC)
     * @param ordEndDate Hướng sắp xếp theo ngày hết hạn chứng chỉ (ASC / DESC)
     * @param offset Vị trí bản ghi bắt đầu lấy
     * @param limit Số lượng bản ghi tối đa trên một trang
     * @return ResponseEntity chứa ListEmployeeResponse
     */
    @GetMapping
    public ResponseEntity<ListEmployeeResponse> getEmployees(
            @RequestParam(name = "employee_name", required = false, defaultValue = "") String employeeName,
            @RequestParam(name = "department_id", required = false, defaultValue = "") String departmentId,
            @RequestParam(name = "ord_employee_name", required = false, defaultValue = "") String ordEmployeeName,
            @RequestParam(name = "ord_certification_name", required = false, defaultValue = "") String ordCertificationName,
            @RequestParam(name = "ord_end_date", required = false, defaultValue = "") String ordEndDate,
            @RequestParam(name = "priority_sort", required = false, defaultValue = "employeeName") String prioritySort,
            @RequestParam(name = "offset", required = false, defaultValue = "") String offset,
            @RequestParam(name = "limit", required = false, defaultValue = "") String limit) {
        ListEmployeeResponse response = employeeService.searchEmployees(
                employeeName,
                departmentId,
                ordEmployeeName,
                ordCertificationName,
                ordEndDate,
                prioritySort,
                offset,
                limit
        );
        return ResponseEntity.ok(response);
    }
}
