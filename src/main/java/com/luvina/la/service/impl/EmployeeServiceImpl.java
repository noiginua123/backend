/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImpl.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.entity.EmployeeCertificationEntity;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.CertificationRequest;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.CommonValidator;

/**
 * Triển khai các nghiệp vụ truy vấn và thêm mới dữ liệu nhân viên.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    /** Đối tượng định dạng ngày tháng theo hằng số hệ thống. */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    /** Repository truy vấn và thao tác dữ liệu nhân viên. */
    private final EmployeeRepository employeeRepository;

    /** Mapper chuyển đổi mảng cột truy vấn native sang DTO. */
    private final EmployeeMapper employeeMapper;

    /** Repository thao tác với thông tin chứng chỉ nhân viên. */
    private final EmployeeCertificationRepository employeeCertificationRepository;

    /** Bộ mã hóa mật khẩu bảo mật BCrypt. */
    private final PasswordEncoder passwordEncoder;

    /** Validator dùng chung cho các thao tác kiểm tra dữ liệu cơ bản. */
    private final CommonValidator commonValidator;

    /**
     * Khởi tạo service với repository, mapper, repository chứng chỉ và bộ mã hóa mật khẩu.
     *
     * @param employeeRepository Repository truy vấn dữ liệu nhân viên
     * @param employeeMapper Mapper chuyển mảng cột native query sang DTO
     * @param employeeCertificationRepository Repository chứng chỉ của nhân viên
     * @param passwordEncoder Bộ mã hóa mật khẩu
     * @param commonValidator Validator dùng chung
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               EmployeeCertificationRepository employeeCertificationRepository,
                               PasswordEncoder passwordEncoder,
                               CommonValidator commonValidator) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeCertificationRepository = employeeCertificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.commonValidator = commonValidator;
    }

    /**
     * Đếm tổng số nhân viên thỏa mãn điều kiện, loại trừ tài khoản admin.
     *
     * @param employeeName Mẫu LIKE tên nhân viên đã escape, hoặc null nếu không lọc
     * @param departmentId ID phòng ban, hoặc null nếu không lọc
     * @return Tổng số nhân viên thỏa mãn
     */
    @Override
    public long getTotalRecords(String employeeName, Long departmentId) {
        return employeeRepository.countEmployees(
                employeeName,
                departmentId,
                Constants.ADMIN_LOGIN_ID
        );
    }

    /**
     * Lấy và mapping danh sách nhân viên đã sắp xếp, phân trang, loại trừ admin.
     *
     * @param employeeName Mẫu LIKE tên nhân viên đã escape, hoặc null nếu không lọc
     * @param departmentId ID phòng ban, hoặc null nếu không lọc
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên (ASC/DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC/DESC)
     * @param ordEndDate Hướng sắp xếp theo ngày hết hạn (ASC/DESC)
     * @param prioritySort Cột sắp xếp ưu tiên
     * @param limit Số bản ghi tối đa
     * @param offset Vị trí bản ghi bắt đầu
     * @return Danh sách DTO nhân viên
     */
    @Override
    public List<EmployeeListDTO> getEmployees(String employeeName,
                                              Long departmentId,
                                              String ordEmployeeName,
                                              String ordCertificationName,
                                              String ordEndDate,
                                              String prioritySort,
                                              int limit,
                                              int offset) {
        List<Object[]> rows = employeeRepository.searchEmployees(
                employeeName,
                departmentId,
                ordEmployeeName,
                ordCertificationName,
                ordEndDate,
                prioritySort,
                Constants.ADMIN_LOGIN_ID,
                limit,
                offset
        );
        return rows.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Thêm mới nhân viên (ADM004) kèm chứng chỉ nếu có.
     *
     * @param employeeRequest Dữ liệu nhân viên đã qua validate
     * @return ID nhân viên vừa được tạo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addEmployee(EmployeeRequest employeeRequest) {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmployeeLoginId(employeeRequest.getEmployeeLoginId().trim());
        employee.setEmployeeName(employeeRequest.getEmployeeName().trim());
        employee.setEmployeeNameKana(employeeRequest.getEmployeeNameKana().trim());
        employee.setEmployeeBirthDate(LocalDate.parse(
                employeeRequest.getEmployeeBirthDate().trim(),
                DATE_FORMATTER
        ));
        employee.setEmployeeEmail(employeeRequest.getEmployeeEmail().trim());
        employee.setEmployeeTelephone(employeeRequest.getEmployeeTelephone().trim());
        employee.setEmployeeLoginPassword(passwordEncoder.encode(employeeRequest.getEmployeeLoginPassword()));
        employee.setDepartmentId(Long.valueOf(employeeRequest.getDepartmentId().trim()));
        employee.setRole(Constants.ROLE_USER);

        EmployeeEntity savedEmployee = employeeRepository.save(employee);

        List<CertificationRequest> certifications = employeeRequest.getCertifications();
        if (certifications != null) {
            for (CertificationRequest certification : certifications) {
                if (certification == null
                        || commonValidator.isEmpty(certification.getCertificationId())) {
                    continue;
                }
                EmployeeCertificationEntity entity = new EmployeeCertificationEntity();
                entity.setEmployeeId(savedEmployee.getEmployeeId());
                entity.setCertificationId(Long.valueOf(certification.getCertificationId().trim()));
                entity.setStartDate(LocalDate.parse(
                        certification.getStartDate().trim(),
                        DATE_FORMATTER
                ));
                entity.setEndDate(LocalDate.parse(
                        certification.getEndDate().trim(),
                        DATE_FORMATTER
                ));
                entity.setScore(new BigDecimal(certification.getScore().trim()));
                employeeCertificationRepository.save(entity);
            }
        }
        return savedEmployee.getEmployeeId();
    }

    /**
     * Kiểm tra sự tồn tại của nhân viên theo ID.
     *
     * @param employeeId ID nhân viên cần kiểm tra
     * @return true nếu nhân viên tồn tại, ngược lại false
     */
    @Override
    public boolean checkExistsEmployeeById(Long employeeId) {
        if (employeeId == null) {
            return false;
        }
        return employeeRepository.existsById(employeeId);
    }
}
