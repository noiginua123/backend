/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapper.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Interface MapStruct thực hiện ánh xạ hai chiều giữa Employee Entity và EmployeeDTO.
 *
 * @author thanhvinh
 */
@Mapper
public interface EmployeeMapper {

    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);

    /**
     * Chuyển đổi từ EmployeeDTO sang Employee Entity.
     *
     * @param dto Đối tượng DTO của nhân viên
     * @return Đối tượng Entity của nhân viên
     */
    EmployeeEntity toEntity(EmployeeDTO dto);

    /**
     * Chuyển đổi từ Employee Entity sang EmployeeDTO kèm theo tên phòng ban.
     *
     * @param entity Đối tượng Entity của nhân viên
     * @return Đối tượng DTO của nhân viên
     */
    @Mapping(source = "department.departmentName", target = "departmentName")
    EmployeeDTO toDto(EmployeeEntity entity);

    /**
     * Chuyển đổi danh sách Employee Entity sang danh sách EmployeeDTO.
     *
     * @param list Danh sách các đối tượng Entity
     * @return Danh sách các đối tượng DTO
     */
    Iterable<EmployeeDTO> toList(Iterable<EmployeeEntity> list);
}
