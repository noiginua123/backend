/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentServiceImpl.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.mapper.DepartmentMapper;
import com.luvina.la.payload.response.ListDepartmentResponse;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;

/**
 * Hiện thực nghiệp vụ lấy danh sách phòng ban.
 *
 * @author thanhvinh
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * Lấy toàn bộ phòng ban từ DB, map sang DTO và tạo response code = 200.
     *
     * @return ListDepartmentResponse dữ liệu phòng ban.
     */
    @Override
    public ListDepartmentResponse getListDepartments() {
        // if (true) {
        //     throw new RuntimeException("Test ER023");
        // }
        List<DepartmentEntity> entities = departmentRepository.findAll();
        List<DepartmentDTO> departments = entities.stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());

        ListDepartmentResponse response = new ListDepartmentResponse();
        response.setCode(Constants.CODE_SUCCESS);
        response.setDepartments(departments);
        return response;
    }
}
