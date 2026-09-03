/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentService.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.service;

import java.util.List;

import com.luvina.la.dto.DepartmentDTO;

/**
 * Interface nghiệp vụ cho chức năng phòng ban.
 *
 * @author thanhvinh
 */
public interface DepartmentService {

    /**
     * Lấy danh sách tất cả phòng ban (sắp xếp theo ID tăng dần).
     * Chỉ trả về dữ liệu; Controller chịu trách nhiệm đóng gói code HTTP vào response.
     *
     * @return Danh sách DTO phòng ban
     */
    List<DepartmentDTO> getListDepartments();
}
