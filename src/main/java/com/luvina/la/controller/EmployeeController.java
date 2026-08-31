/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeController.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.constant.SortField;

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
     * @param request Request chứa điều kiện tìm kiếm, sắp xếp và phân trang
     * @return ResponseEntity chứa ListEmployeeResponse
     */
    @GetMapping
    public ResponseEntity<ListEmployeeResponse> getEmployees(
            @ModelAttribute EmployeeSearchRequest request) {
        ListEmployeeResponse response = employeeService.searchEmployees(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Bind các query param theo contract API vào request object của ADM002.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @param departmentId ID phòng ban
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ
     * @param ordEndDate Hướng sắp xếp theo ngày hết hạn
     * @param prioritySort Cột sắp xếp ưu tiên
     * @param offset Vị trí bản ghi bắt đầu
     * @param limit Số bản ghi tối đa
     * @return Request object đã bind đầy đủ query param
     */
    @ModelAttribute
    public EmployeeSearchRequest bindEmployeeSearchRequest(
            @RequestParam(name = "employee_name", required = false, defaultValue = "") String employeeName,
            @RequestParam(name = "department_id", required = false, defaultValue = "") String departmentId,
            @RequestParam(name = "ord_employee_name", required = false, defaultValue = "") String ordEmployeeName,
            @RequestParam(name = "ord_certification_name", required = false, defaultValue = "") String ordCertificationName,
            @RequestParam(name = "ord_end_date", required = false, defaultValue = "") String ordEndDate,
            @RequestParam(name = "priority_sort", required = false,
                    defaultValue = SortField.EMPLOYEE_NAME_VALUE) String prioritySort,
            @RequestParam(name = "offset", required = false, defaultValue = "") String offset,
            @RequestParam(name = "limit", required = false, defaultValue = "") String limit) {
        return new EmployeeSearchRequest(
                employeeName,
                departmentId,
                ordEmployeeName,
                ordCertificationName,
                ordEndDate,
                prioritySort,
                offset,
                limit
        );
    }
}
