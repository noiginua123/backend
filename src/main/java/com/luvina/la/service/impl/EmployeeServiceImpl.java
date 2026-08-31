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
import com.luvina.la.dto.EmployeeSearchCriteria;
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
        validate(request);
        EmployeeSearchCriteria criteria = buildCriteria(request);
        long totalRecords = countEmployees(criteria);
        List<EmployeeListDTO> employees = fetchAndMap(criteria, totalRecords);
        return new ListEmployeeResponse(Constants.CODE_SUCCESS, totalRecords, employees);
    }

    /**
     * Kiểm tra các giá trị đầu vào của request trước khi chuẩn hóa.
     *
     * @param request Request tìm kiếm ADM002
     * @throws AppException Khi một hướng sắp xếp không hợp lệ
     */
    private void validate(EmployeeSearchRequest request) {
        validateSortOrders(
                request.getOrdEmployeeName(),
                request.getOrdCertificationName(),
                request.getOrdEndDate()
        );
    }

    /**
     * Chuyển request thô thành tiêu chí tìm kiếm đã validate và chuẩn hóa.
     *
     * @param request Request tìm kiếm ADM002
     * @return Tiêu chí an toàn để truyền xuống Repository
     * @throws AppException Khi offset, limit, departmentId hoặc employeeName không hợp lệ
     */
    private EmployeeSearchCriteria buildCriteria(EmployeeSearchRequest request) {
        return new EmployeeSearchCriteria(
                employeeValidator.validateAndEscapeEmployeeName(request.getEmployeeName()),
                employeeValidator.parseDepartmentId(request.getDepartmentId()),
                normalizeSortOrder(request.getOrdEmployeeName()),
                normalizeSortOrder(request.getOrdCertificationName()),
                normalizeSortOrder(request.getOrdEndDate()),
                normalizePrioritySort(request.getPrioritySort()),
                employeeValidator.validateAndParseOffset(request.getOffset()),
                employeeValidator.validateAndParseLimit(request.getLimit())
        );
    }

    /**
     * Đếm tổng số nhân viên theo tiêu chí tìm kiếm.
     *
     * @param criteria Tiêu chí tìm kiếm đã chuẩn hóa
     * @return Tổng số nhân viên thỏa mãn
     */
    private long countEmployees(EmployeeSearchCriteria criteria) {
        return employeeRepository.countEmployees(
                criteria.getEmployeeName(),
                criteria.getDepartmentId()
        );
    }

    /**
     * Truy vấn và chuyển dữ liệu nhân viên sang DTO khi có bản ghi.
     *
     * @param criteria Tiêu chí tìm kiếm đã chuẩn hóa
     * @param totalRecords Tổng số bản ghi thỏa mãn
     * @return Danh sách DTO nhân viên, hoặc danh sách rỗng
     */
    private List<EmployeeListDTO> fetchAndMap(
            EmployeeSearchCriteria criteria,
            long totalRecords) {
        if (totalRecords == 0) {
            return Collections.emptyList();
        }

        List<EmployeeListItemProjection> projections = employeeRepository.searchEmployees(
                criteria.getEmployeeName(),
                criteria.getDepartmentId(),
                criteria.getOrdEmployeeName(),
                criteria.getOrdCertificationName(),
                criteria.getOrdEndDate(),
                criteria.getPrioritySort(),
                criteria.getLimit(),
                criteria.getOffset()
        );
        return mapToEmployeeDTOs(projections);
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
