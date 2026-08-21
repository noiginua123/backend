/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentServiceImpl.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.mapper.DepartmentMapper;
import com.luvina.la.payload.response.ListDepartmentResponse;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;

/**
 * Hiện thực các nghiệp vụ liên quan đến phòng ban.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * Lấy danh sách tất cả phòng ban, sắp xếp theo ID tăng dần và chuyển đổi sang DTO.
     *
     * @return ListDepartmentResponse chứa mã thành công 200 và danh sách phòng ban
     */
    @Override
    public ListDepartmentResponse getListDepartments() {
        // 1. Lấy danh sách phòng ban từ cơ sở dữ liệu sắp xếp theo ID tăng dần
        List<DepartmentEntity> entities = departmentRepository.findAllByOrderByDepartmentIdAsc();

        // 2. Chuyển đổi danh sách Entity sang danh sách DTO
        List<DepartmentDTO> departments = entities.stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());

        // 3. Trả về kết quả đóng gói response
        return new ListDepartmentResponse(Constants.CODE_SUCCESS, departments);
    }
}
