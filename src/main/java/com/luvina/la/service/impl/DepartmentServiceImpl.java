/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentServiceImpl.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.mapper.DepartmentMapper;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;

/**
 * Triển khai các nghiệp vụ liên quan đến phòng ban.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    /**
     * Khởi tạo service phòng ban.
     *
     * @param departmentRepository Repository truy vấn phòng ban
     * @param departmentMapper Mapper chuyển entity phòng ban sang DTO
     */
    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * Lấy danh sách tất cả phòng ban, sắp xếp theo ID tăng dần và chuyển sang DTO.
     *
     * @return Danh sách DTO phòng ban
     */
    @Override
    public List<DepartmentDTO> getListDepartments() {
        List<DepartmentEntity> entities = departmentRepository.findAllByOrderByDepartmentIdAsc();
        return entities.stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
