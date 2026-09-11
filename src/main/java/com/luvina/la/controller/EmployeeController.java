/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeController.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.constant.Constants;
import com.luvina.la.constant.SortField;
import com.luvina.la.constant.SortOrder;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.dto.MessageDTO;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.payload.response.EmployeeDetailResponse;
import com.luvina.la.payload.response.EmployeeResponse;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.payload.response.MessageResponse;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.EmployeeValidator;

/**
 * Controller tiếp nhận và xử lý các yêu cầu liên quan đến nhân viên.
 * Tầng Controller chịu trách nhiệm kiểm tra tham số đầu vào (qua EmployeeValidator),
 * chuẩn hóa tham số sắp xếp, gọi Service xử lý và đóng gói response cho client.
 * Phục vụ các chức năng: ADM002 (danh sách nhân viên) và ADM004 (thêm mới nhân viên).
 *
 * @author thanhvinh
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    /** Service xử lý nghiệp vụ liên quan đến nhân viên. */
    private final EmployeeService employeeService;

    /** Validator kiểm tra tính hợp lệ dữ liệu nhân viên. */
    private final EmployeeValidator employeeValidator;

    /**
     * Khởi tạo EmployeeController với các dependency cần thiết.
     *
     * @param employeeService Service xử lý nghiệp vụ nhân viên
     * @param employeeValidator Validator kiểm tra tính hợp lệ dữ liệu nhân viên
     */
    public EmployeeController(EmployeeService employeeService,
                              EmployeeValidator employeeValidator) {
        this.employeeService = employeeService;
        this.employeeValidator = employeeValidator;
    }

    /**
     * API tìm kiếm, sắp xếp và phân trang danh sách nhân viên (ADM002).
     *
     * @param request Đối tượng chứa các điều kiện tìm kiếm, sắp xếp và phân trang
     * @return ResponseEntity chứa danh sách nhân viên và tổng số bản ghi (ListEmployeeResponse)
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
        List<EmployeeListDTO> employees;
        if (totalRecords > 0) {
            employees = employeeService.getEmployees(
                    employeeName,
                    departmentId,
                    ordEmployeeName,
                    ordCertificationName,
                    ordEndDate,
                    prioritySort,
                    limit,
                    offset
            );
        } else {
            employees = Collections.emptyList();
        }

        // 4. Đóng gói response và gắn code thành công
        ListEmployeeResponse response = new ListEmployeeResponse(
                Constants.CODE_SUCCESS,
                totalRecords,
                employees
        );
        return ResponseEntity.ok(response);
    }

    /**
     * API thêm mới thông tin nhân viên vào hệ thống (ADM004/ADM005).
     * Thực hiện kiểm tra tính hợp lệ của dữ liệu đầu vào trước khi lưu vào cơ sở dữ liệu.
     *
     * @param request Dữ liệu thông tin nhân viên cần thêm mới từ form ADM004
     * @return ResponseEntity chứa EmployeeResponse kèm ID nhân viên và thông báo kết quả
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(
            @RequestParam(value = "mode", required = false) String mode,
            @RequestBody EmployeeRequest request) {
        boolean isEdit = "edit".equalsIgnoreCase(mode);
        MessageDTO messageDto = employeeValidator.validateAddEditEmployee(request, isEdit);
        if (messageDto != null) {
            MessageResponse messageResponse = new MessageResponse(messageDto.getCode(), messageDto.getParams());
            EmployeeResponse errorResponse = new EmployeeResponse(Constants.CODE_ERROR, null, messageResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        Long employeeId = employeeService.addEmployee(request);
        MessageResponse message = new MessageResponse(Constants.MSG001, new ArrayList<>());
        EmployeeResponse response = new EmployeeResponse(Constants.CODE_SUCCESS, employeeId, message);
        return ResponseEntity.ok(response);
    }

    /**
     * API cập nhật thông tin nhân viên vào hệ thống (ADM004).
     * Thực hiện kiểm tra tính hợp lệ của dữ liệu đầu vào trước khi cập nhật vào cơ sở dữ liệu.
     *
     * @param mode    Chế độ thao tác từ URL (ví dụ: mode=edit)
     * @param request Dữ liệu thông tin nhân viên cần chỉnh sửa từ form ADM004
     * @return ResponseEntity chứa EmployeeResponse kèm ID nhân viên và thông báo kết quả (MSG002)
     */
    @PutMapping
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @RequestParam(value = "mode", required = false) String mode,
            @RequestBody EmployeeRequest request) {
        boolean isEdit = mode == null || !"add".equalsIgnoreCase(mode);
        MessageDTO messageDto = employeeValidator.validateAddEditEmployee(request, isEdit);
        if (messageDto != null) {
            MessageResponse messageResponse = new MessageResponse(messageDto.getCode(), messageDto.getParams());
            EmployeeResponse errorResponse = new EmployeeResponse(Constants.CODE_ERROR, null, messageResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        Long employeeId = employeeService.updateEmployee(request);
        MessageResponse message = new MessageResponse(Constants.MSG002, new ArrayList<>());
        EmployeeResponse response = new EmployeeResponse(Constants.CODE_SUCCESS, employeeId, message);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<EmployeeResponse> addEmployee(EmployeeRequest request) {
        return addEmployee(null, request);
    }

    public ResponseEntity<EmployeeResponse> updateEmployee(EmployeeRequest request) {
        return updateEmployee(null, request);
    }

    /**
     * API lấy thông tin chi tiết một nhân viên theo ID (ADM003 / ADM004).
     *
     * @param employeeId ID nhân viên cần lấy thông tin chi tiết
     * @return ResponseEntity chứa EmployeeDetailResponse
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDetailResponse> getEmployeeDetail(
            @PathVariable("employeeId") Long employeeId) {
        employeeValidator.validateGetEmployeeDetail(employeeId);
        EmployeeDetailDTO employeeDetailDTO = employeeService.getEmployeeDetail(employeeId);
        EmployeeDetailResponse response = new EmployeeDetailResponse(
                Constants.CODE_SUCCESS,
                employeeDetailDTO
        );
        return ResponseEntity.ok(response);
    }

    /**
     * API kiểm tra sự tồn tại của nhân viên theo ID (siêu nhẹ, không fetch chi tiết).
     *
     * @param employeeId ID nhân viên cần kiểm tra
     * @return ResponseEntity chứa boolean true nếu tồn tại, false nếu không
     */
    @GetMapping("/{employeeId}/exists")
    public ResponseEntity<Boolean> checkEmployeeExists(
            @PathVariable("employeeId") Long employeeId) {
        boolean exists = employeeService.checkExistsEmployeeById(employeeId);
        return ResponseEntity.ok(exists);
    }

    /**
     * API xóa một nhân viên khỏi hệ thống kèm các chứng chỉ liên quan (ADM003).
     *
     * @param employeeId ID nhân viên cần xóa
     * @return ResponseEntity chứa EmployeeResponse với thông báo MSG003
     */
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> deleteEmployee(
            @PathVariable("employeeId") Long employeeId) {
        employeeValidator.validateDeleteEmployee(employeeId);
        employeeService.deleteEmployee(employeeId);

        MessageResponse message = new MessageResponse(Constants.MSG003, new ArrayList<>());
        EmployeeResponse response = new EmployeeResponse(Constants.CODE_SUCCESS, employeeId, message);
        return ResponseEntity.ok(response);
    }

    /**
     * Bind các tham số truy vấn tìm kiếm từ request vào đối tượng EmployeeSearchRequest.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @param departmentId ID phòng ban cần lọc
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên (ASC/DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC/DESC)
     * @param ordEndDate Hướng sắp xếp theo ngày kết thúc chứng chỉ (ASC/DESC)
     * @param prioritySort Cột ưu tiên sắp xếp
     * @param offset Vị trí bắt đầu lấy bản ghi
     * @param limit Số lượng bản ghi tối đa trên một trang
     * @return Đối tượng EmployeeSearchRequest chứa các tham số đã bind
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
