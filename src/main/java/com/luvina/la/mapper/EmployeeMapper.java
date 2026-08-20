/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapper.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import org.springframework.stereotype.Component;

import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.dto.EmployeeListItemProjection;

/**
 * Component chuyển đổi dữ liệu từ EmployeeListItemProjection sang EmployeeListDTO.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeMapper {

    /**
     * Convert một projection kết quả truy vấn nhân viên sang DTO phẳng.
     *
     * @param projection projection chứa dữ liệu dòng truy vấn
     * @return EmployeeListDTO tương ứng, hoặc null nếu projection là null
     */
    public EmployeeListDTO toDTO(EmployeeListItemProjection projection) {
        if (projection == null) {
            return null;
        }
        return new EmployeeListDTO(
                projection.getEmployeeId(),
                projection.getEmployeeName(),
                projection.getEmployeeBirthDate(),
                projection.getDepartmentName(),
                projection.getEmployeeEmail(),
                projection.getEmployeeTelephone(),
                projection.getCertificationName(),
                projection.getEndDate(),
                projection.getScore()
        );
    }
}
