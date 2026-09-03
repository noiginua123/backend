/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeController.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.constant.Constants;
import com.luvina.la.constant.SortField;
import com.luvina.la.constant.SortOrder;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.payload.response.EmployeeResponse;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.payload.response.MessageResponse;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.EmployeeValidator;

/**
 * Controller tiếp nhận và xử lý các yêu cầu liên quan đến nhân viên.
 *
 * <p>Tầng Controller chịu trách nhiệm: kiểm tra tham số đầu vào (qua
 * EmployeeValidator), chuẩn hóa tham số sắp xếp, gọi Service, rồi đóng gói response.
 * Phục vụ ADM002 (danh sách) và ADM004 (validate + thêm mới).</p>
 *
 * @author thanhvinh
 */
@RestController
@RequestMapping({"/employee", "/employees"})
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeValidator employeeValidator;

    /**
     * Khởi tạo controller với service nghiệp vụ và validator nhân viên.
     *
     * @param employeeService Service truy vấn dữ liệu nhân viên
     * @param employeeValidator Validator kiểm tra tham số
     */
    public EmployeeController(EmployeeService employeeService,
                              EmployeeValidator employeeValidator) {
        this.employeeService = employeeService;
        this.employeeValidator = employeeValidator;
    }

    /**
     * API tìm kiếm, sắp xếp và phân trang danh sách nhân viên (ADM002).
     *
     * @param request Request chứa điều kiện tìm kiếm, sắp xếp và phân trang
     * @return ResponseEntity chứa ListEmployeeResponse
     */
    @GetMapping
    public ResponseEntity<ListEmployeeResponse> getEmployees(
            @ModelAttribute EmployeeSearchRequest request) {
        // 1. Kiểm tra tham số đầu vào (thứ tự quyết định mã lỗi ER trả về trước tiên)
        employeeValidator.validateSortOrder(request.getOrdEmployeeName());
        employeeValidator.validateSortOrder(request.getOrdCertificationName());
        employeeValidator.validateSortOrder(request.getOrdEndDate());
        String employeeName = employeeValidator.validateAndEscapeEmployeeName(request.getEmployeeName());
        Long departmentId = employeeValidator.parseDepartmentId(request.getDepartmentId());
        int offset = employeeValidator.validateAndParseOffset(request.getOffset());
        int limit = employeeValidator.validateAndParseLimit(request.getLimit());

        // 2. Chuẩn hóa tham số sắp xếp (áp mặc định, không ném lỗi)
        String ordEmployeeName = SortOrder.fromValueOrDefault(request.getOrdEmployeeName()).getValue();
        String ordCertificationName = SortOrder.fromValueOrDefault(request.getOrdCertificationName()).getValue();
        String ordEndDate = SortOrder.fromValueOrDefault(request.getOrdEndDate()).getValue();
        String prioritySort = SortField.fromValueOrDefault(request.getPrioritySort()).getValue();

        // 3. Gọi service lấy dữ liệu; chỉ truy vấn danh sách khi có bản ghi
        long totalRecords = employeeService.getTotalRecords(employeeName, departmentId);
        List<EmployeeListDTO> employees = totalRecords > 0
                ? employeeService.getEmployees(employeeName, departmentId, ordEmployeeName,
                        ordCertificationName, ordEndDate, prioritySort, limit, offset)
                : Collections.emptyList();

        // 4. Đóng gói response và gắn code thành công
        ListEmployeeResponse response = new ListEmployeeResponse(
                Constants.CODE_SUCCESS,
                totalRecords,
                employees
        );
        return ResponseEntity.ok(response);
    }

    /**
     * API kiểm tra dữ liệu trước khi sang màn xác nhận ADM005 (không ghi DB).
     *
     * @param request Dữ liệu nhân viên từ form ADM004
     * @return ResponseEntity chứa EmployeeResponse chỉ gồm code thành công
     */
    @PostMapping("/validate")
    public ResponseEntity<EmployeeResponse> validateEmployee(
            @RequestBody EmployeeRequest request) {
        employeeValidator.validateForCreate(request);
        EmployeeResponse response = new EmployeeResponse(Constants.CODE_SUCCESS, null, null);
        return ResponseEntity.ok(response);
    }

    /**
     * API thêm mới nhân viên (ADM004/ADM005). Validate lại trước khi ghi DB.
     *
     * @param request Dữ liệu nhân viên từ form ADM004
     * @return ResponseEntity chứa EmployeeResponse kèm id và message MSG001
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(
            @RequestBody EmployeeRequest request) {
        employeeValidator.validateForCreate(request);
        Long employeeId = employeeService.addEmployee(request);
        MessageResponse message = new MessageResponse(Constants.MSG001, new ArrayList<>());
        EmployeeResponse response = new EmployeeResponse(Constants.CODE_SUCCESS, employeeId, message);
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
