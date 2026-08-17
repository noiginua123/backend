/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentMapper.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import org.springframework.stereotype.Component;

import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.DepartmentEntity;

/**
 * Chuyển đổi DepartmentEntity sang DepartmentDTO.
 *
 * @author thanhvinh
 */
@Component
public class DepartmentMapper {

    /**
     * Convert 1 entity phòng ban sang DTO.
     *
     * @param entity entity phòng ban.
     * @return DepartmentDTO tương ứng, null nếu entity null.
     */
    public DepartmentDTO toDto(DepartmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return new DepartmentDTO(entity.getDepartmentId(), entity.getDepartmentName());
    }
}
