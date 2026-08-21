/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImpl.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.dto.EmployeeListItemProjection;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.EmployeeValidator;

/**
 * Hiện thực các nghiệp vụ liên quan đến nhân viên.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeValidator employeeValidator;

    /**
     * Tìm kiếm, sắp xếp và phân trang danh sách nhân viên theo tiêu chí.
     *
     * @param employeeName Tên nhân viên tìm kiếm
     * @param departmentId ID phòng ban
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên (ASC / DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC / DESC)
     * @param ordEndDate Hướng sắp xếp theo ngày kết thúc chứng chỉ (ASC / DESC)
     * @param offset Vị trí bắt đầu bản ghi
     * @param limit Số lượng bản ghi mỗi trang
     * @return ListEmployeeResponse chứa kết quả
     */
    @Override
    public ListEmployeeResponse searchEmployees(String employeeName,
                                                String departmentId,
                                                String ordEmployeeName,
                                                String ordCertificationName,
                                                String ordEndDate,
                                                String offset,
                                                String limit) {
        // 1. Validate các tham số sắp xếp
        employeeValidator.validateSortOrder(ordEmployeeName);
        employeeValidator.validateSortOrder(ordCertificationName);
        employeeValidator.validateSortOrder(ordEndDate);

        // 2. Validate và parse offset
        int parsedOffset = employeeValidator.validateAndParseOffset(offset);

        // 3. Validate và parse limit
        int parsedLimit = employeeValidator.validateAndParseLimit(limit);

        // 4. Parse department_id
        Long parsedDepartmentId = employeeValidator.parseDepartmentId(departmentId);

        // 5. Escape ký tự đặc biệt trong employee_name
        String escapedEmployeeName = employeeValidator.escapeEmployeeName(employeeName);

        // 6. Đếm tổng số bản ghi
        long totalRecords = employeeRepository.countEmployees(escapedEmployeeName, parsedDepartmentId);
        if (totalRecords == 0) {
            return new ListEmployeeResponse(Constants.CODE_SUCCESS, 0L, Collections.emptyList());
        }

        // 7. Lấy danh sách nhân viên thỏa mãn và map sang DTO
        String safeOrdEmployeeName = ordEmployeeName != null ? ordEmployeeName.trim() : "";
        String safeOrdCertificationName = ordCertificationName != null ? ordCertificationName.trim() : "";
        String safeOrdEndDate = ordEndDate != null ? ordEndDate.trim() : "";

        List<EmployeeListItemProjection> projections = employeeRepository.searchEmployees(
                escapedEmployeeName,
                parsedDepartmentId,
                safeOrdEmployeeName,
                safeOrdCertificationName,
                safeOrdEndDate,
                parsedLimit,
                parsedOffset
        );

        List<EmployeeListDTO> employees = projections.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());

        return new ListEmployeeResponse(Constants.CODE_SUCCESS, totalRecords, employees);
    }
}
