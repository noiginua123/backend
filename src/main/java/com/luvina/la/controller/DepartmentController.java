/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentController.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.payload.response.ListDepartmentResponse;
import com.luvina.la.service.DepartmentService;

/**
 * Controller cho chức năng phòng ban.
 *
 * @author thanhvinh
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * API lấy danh sách tất cả phòng ban.
     *
     * @return ResponseEntity chứa ListDepartmentResponse (code = 200 + danh sách phòng ban).
     */
    @GetMapping
    public ResponseEntity<ListDepartmentResponse> getListDepartments() {
        return ResponseEntity.ok(departmentService.getListDepartments());
    }
}
