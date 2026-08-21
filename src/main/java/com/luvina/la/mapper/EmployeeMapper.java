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
 * Chuyển đổi projection danh sách nhân viên sang DTO response.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeMapper {

    /**
     * Chuyển projection kết quả truy vấn thành DTO.
     *
     * @param projection Projection chứa dữ liệu nhân viên
     * @return DTO nhân viên tương ứng hoặc null
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
                projection.getScore(),
                Boolean.TRUE.equals(projection.getRole()) ? 1 : 0
        );
    }
}
