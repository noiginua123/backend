/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentController.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.payload.response.ListDepartmentResponse;
import com.luvina.la.service.DepartmentService;

/**
 * Controller cho chức năng phòng ban.
 *
 * @author thanhvinh
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Khởi tạo controller với service phòng ban.
     *
     * @param departmentService Service xử lý nghiệp vụ phòng ban
     */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * API lấy danh sách tất cả phòng ban.
     *
     * Controller lấy dữ liệu từ service rồi đóng gói vào ListDepartmentResponse (gắn code thành công).
     *
     * @return ResponseEntity chứa ListDepartmentResponse
     */
    @GetMapping
    public ResponseEntity<ListDepartmentResponse> getListDepartments() {
        List<DepartmentDTO> departments = departmentService.getListDepartments();
        ListDepartmentResponse response = new ListDepartmentResponse(Constants.CODE_SUCCESS, departments);
        return ResponseEntity.ok(response);
    }
}
