/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImpl.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.dto.EmployeeListItemProjection;
import com.luvina.la.exception.AppException;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.EmployeeValidator;

/**
 * Hiện thực nghiệp vụ danh sách nhân viên.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final EmployeeValidator employeeValidator;

    /**
     * Khởi tạo service với các thành phần xử lý danh sách nhân viên.
     *
     * @param employeeRepository Repository truy vấn dữ liệu nhân viên
     * @param employeeMapper Mapper chuyển projection sang DTO
     * @param employeeValidator Validator kiểm tra tham số tìm kiếm
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               EmployeeValidator employeeValidator) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeValidator = employeeValidator;
    }

    /**
     * Tìm kiếm danh sách nhân viên, bao gồm admin, theo bốn giai đoạn: validate sort, chuẩn hóa input,
     * đếm tổng số bản ghi, sau đó lấy và mapping danh sách nếu có dữ liệu.
     *
     * @param request Request chứa điều kiện tìm kiếm, sắp xếp và phân trang
     * @return Response chứa code thành công, tổng số bản ghi và danh sách nhân viên
     * @throws AppException Khi một trong các tham số đầu vào không hợp lệ
     */
    @Override
    public ListEmployeeResponse searchEmployees(EmployeeSearchRequest request) {
        // Bước 1: Chặn giá trị sort khác rỗng, ASC hoặc DESC trước khi gọi database.
        validateSortOrders(
                request.getOrdEmployeeName(),
                request.getOrdCertificationName(),
                request.getOrdEndDate()
        );

        // Bước 2: Chuyển các tham số dạng chuỗi thành dữ liệu an toàn cho Repository.
        int parsedOffset = employeeValidator.validateAndParseOffset(request.getOffset());
        int parsedLimit = employeeValidator.validateAndParseLimit(request.getLimit());
        Long parsedDepartmentId = employeeValidator.parseDepartmentId(request.getDepartmentId());
        String escapedEmployeeName = employeeValidator.validateAndEscapeEmployeeName(request.getEmployeeName());

        // Bước 3: Đếm tổng số nhân viên theo cùng điều kiện filter của query lấy danh sách.
        long totalRecords = employeeRepository.countEmployees(
                escapedEmployeeName,
                parsedDepartmentId
        );

        // Bước 4: Chỉ query danh sách khi có dữ liệu để tránh một lần truy vấn không cần thiết.
        List<EmployeeListDTO> employees = Collections.emptyList();
        if (totalRecords > 0) {
            String safeOrdEmployeeName = normalizeSortOrder(request.getOrdEmployeeName());
            String safeOrdCertificationName = normalizeSortOrder(request.getOrdCertificationName());
            String safeOrdEndDate = normalizeSortOrder(request.getOrdEndDate());
            String safePrioritySort = normalizePrioritySort(request.getPrioritySort());

            List<EmployeeListItemProjection> projections = employeeRepository.searchEmployees(
                    escapedEmployeeName,
                    parsedDepartmentId,
                    safeOrdEmployeeName,
                    safeOrdCertificationName,
                    safeOrdEndDate,
                    safePrioritySort,
                    parsedLimit,
                    parsedOffset
            );
            employees = mapToEmployeeDTOs(projections);
        }

        return new ListEmployeeResponse(Constants.CODE_SUCCESS, totalRecords, employees);
    }

    /**
     * Chuẩn hóa cột ưu tiên sắp xếp động. Chỉ chấp nhận employeeName, certificationName, endDate.
     * Mặc định là employeeName nếu null hoặc không hợp lệ.
     *
     * @param prioritySort Tên cột ưu tiên đầu vào
     * @return Tên cột ưu tiên hợp lệ
     */
    private String normalizePrioritySort(String prioritySort) {
        if (prioritySort == null) {
            return "employeeName";
        }
        String trimmed = prioritySort.trim();
        if ("certificationName".equals(trimmed) || "endDate".equals(trimmed)) {
            return trimmed;
        }
        return "employeeName";
    }

    /**
     * Kiểm tra đồng thời ba hướng sắp xếp được hỗ trợ trên màn hình ADM002.
     *
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ
     * @param ordEndDate Hướng sắp xếp theo ngày hết hạn chứng chỉ
     */
    private void validateSortOrders(String ordEmployeeName,
                                    String ordCertificationName,
                                    String ordEndDate) {
        employeeValidator.validateSortOrder(ordEmployeeName);
        employeeValidator.validateSortOrder(ordCertificationName);
        employeeValidator.validateSortOrder(ordEndDate);
    }

    /**
     * Chuẩn hóa hướng sắp xếp thành chuỗi rỗng, ASC hoặc DESC.
     *
     * @param sortOrder Hướng sắp xếp đầu vào
     * @return Hướng sắp xếp đã chuẩn hóa
     */
    private String normalizeSortOrder(String sortOrder) {
        return sortOrder != null ? sortOrder.trim() : "";
    }

    /**
     * Chuyển danh sách projection từ Repository sang DTO trả về cho frontend.
     *
     * @param projections Danh sách kết quả native query
     * @return Danh sách DTO nhân viên
     */
    private List<EmployeeListDTO> mapToEmployeeDTOs(
            List<EmployeeListItemProjection> projections) {
        return projections.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
