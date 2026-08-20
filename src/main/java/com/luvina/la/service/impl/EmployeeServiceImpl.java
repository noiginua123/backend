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
import com.luvina.la.exception.AppException;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;

/**
 * Hiện thực các nghiệp vụ liên quan đến nhân viên.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 5;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

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
        validateSortOrder(ordEmployeeName);
        validateSortOrder(ordCertificationName);
        validateSortOrder(ordEndDate);

        // 2. Validate và parse offset
        int parsedOffset = validateAndParseOffset(offset);

        // 3. Validate và parse limit
        int parsedLimit = validateAndParseLimit(limit);

        // 4. Parse department_id
        Long parsedDepartmentId = parseDepartmentId(departmentId);

        // 5. Escape ký tự đặc biệt trong employee_name
        String escapedEmployeeName = escapeEmployeeName(employeeName);

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

    /**
     * Kiểm tra giá trị tham số sắp xếp (chỉ chấp nhận rỗng hoặc "ASC", "DESC").
     *
     * @param order Giá trị hướng sắp xếp
     * @throws AppException khi giá trị không hợp lệ (mã lỗi ER021)
     */
    private void validateSortOrder(String order) {
        if (order != null && !order.trim().isEmpty()) {
            String trimmed = order.trim();
            if (!"ASC".equals(trimmed) && !"DESC".equals(trimmed)) {
                throw new AppException(Constants.ER021);
            }
        }
    }

    /**
     * Kiểm tra và chuyển đổi chuỗi offset sang số nguyên >= 0.
     *
     * @param offset Chuỗi offset
     * @return Số nguyên offset hợp lệ
     * @throws AppException khi chuỗi không phải số nguyên >= 0 (mã lỗi ER018)
     */
    private int validateAndParseOffset(String offset) {
        if (offset == null || offset.trim().isEmpty()) {
            return DEFAULT_OFFSET;
        }
        String trimmed = offset.trim();
        if (!trimmed.matches("^[0-9]+$")) {
            throw new AppException(Constants.ER018, List.of("オフセット"));
        }
        try {
            int val = Integer.parseInt(trimmed);
            if (val < 0) {
                throw new AppException(Constants.ER018, List.of("オフセット"));
            }
            return val;
        } catch (NumberFormatException e) {
            throw new AppException(Constants.ER018, List.of("オフセット"));
        }
    }

    /**
     * Kiểm tra và chuyển đổi chuỗi limit sang số nguyên > 0.
     *
     * @param limit Chuỗi limit
     * @return Số nguyên limit hợp lệ
     * @throws AppException khi chuỗi không phải số nguyên > 0 (mã lỗi ER018)
     */
    private int validateAndParseLimit(String limit) {
        if (limit == null || limit.trim().isEmpty()) {
            return DEFAULT_LIMIT;
        }
        String trimmed = limit.trim();
        if (!trimmed.matches("^[0-9]+$")) {
            throw new AppException(Constants.ER018, List.of("リミット"));
        }
        try {
            int val = Integer.parseInt(trimmed);
            if (val <= 0) {
                throw new AppException(Constants.ER018, List.of("リミット"));
            }
            return val;
        } catch (NumberFormatException e) {
            throw new AppException(Constants.ER018, List.of("リミット"));
        }
    }

    /**
     * Chuyển đổi chuỗi department_id sang Long.
     *
     * @param departmentId Chuỗi ID phòng ban
     * @return Long ID phòng ban hoặc null nếu chuỗi rỗng
     */
    private Long parseDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return null;
        }
        return Long.parseLong(departmentId.trim());
    }

    /**
     * Escape các ký tự đặc biệt cho câu lệnh LIKE và bao quanh bởi '%'.
     *
     * @param employeeName Tên nhân viên gốc
     * @return Chuỗi đã escape hoặc null nếu tên nhân viên rỗng
     */
    private String escapeEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }
        String escaped = employeeName.trim()
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        return "%" + escaped + "%";
    }
}
