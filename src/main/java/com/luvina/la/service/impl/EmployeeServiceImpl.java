/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImpl.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.entity.EmployeeCertificationEntity;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.exception.AppException;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.CertificationRequest;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

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

    /** Repository truy vấn dữ liệu phòng ban. */
    private final DepartmentRepository departmentRepository;

    /** Repository truy vấn dữ liệu chứng chỉ. */
    private final CertificationRepository certificationRepository;

    /** Nguồn cung cấp message đa ngôn ngữ. */
    private final MessageSource messageSource;

    /**
     * Khởi tạo service với repository, mapper, repository chứng chỉ, bộ mã hóa mật
     * khẩu,
     * repository phòng ban và nguồn message.
     *
     * @param employeeRepository              Repository truy vấn dữ liệu nhân viên
     * @param employeeMapper                  Mapper chuyển mảng cột native query
     *                                        sang DTO
     * @param employeeCertificationRepository Repository chứng chỉ của nhân viên
     * @param passwordEncoder                 Bộ mã hóa mật khẩu
     * @param commonValidator                 Validator dùng chung
     * @param departmentRepository            Repository truy vấn phòng ban
     * @param certificationRepository         Repository truy vấn chứng chỉ
     * @param messageSource                   Nguồn cung cấp message
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            EmployeeCertificationRepository employeeCertificationRepository,
            PasswordEncoder passwordEncoder,
            CommonValidator commonValidator,
            DepartmentRepository departmentRepository,
            CertificationRepository certificationRepository,
            MessageSource messageSource) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeCertificationRepository = employeeCertificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.commonValidator = commonValidator;
        this.departmentRepository = departmentRepository;
        this.certificationRepository = certificationRepository;
        this.messageSource = messageSource;
    }

    /**
     * Constructor tương thích cho các bài kiểm thử đơn vị.
     *
     * @param employeeRepository              Repository truy vấn dữ liệu nhân viên
     * @param employeeMapper                  Mapper chuyển mảng cột native query
     *                                        sang DTO
     * @param employeeCertificationRepository Repository chứng chỉ của nhân viên
     * @param passwordEncoder                 Bộ mã hóa mật khẩu
     * @param commonValidator                 Validator dùng chung
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            EmployeeCertificationRepository employeeCertificationRepository,
            PasswordEncoder passwordEncoder,
            CommonValidator commonValidator) {
        this(employeeRepository, employeeMapper, employeeCertificationRepository,
                passwordEncoder, commonValidator, null, null, null);
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
                Constants.ADMIN_LOGIN_ID);
    }

    /**
     * Lấy và mapping danh sách nhân viên đã sắp xếp, phân trang, loại trừ admin.
     *
     * @param employeeName         Mẫu LIKE tên nhân viên đã escape, hoặc null nếu
     *                             không lọc
     * @param departmentId         ID phòng ban, hoặc null nếu không lọc
     * @param ordEmployeeName      Hướng sắp xếp theo tên nhân viên (ASC/DESC)
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ (ASC/DESC)
     * @param ordEndDate           Hướng sắp xếp theo ngày hết hạn (ASC/DESC)
     * @param prioritySort         Cột sắp xếp ưu tiên
     * @param limit                Số bản ghi tối đa
     * @param offset               Vị trí bản ghi bắt đầu
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
                offset);
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
                DATE_FORMATTER));
        employee.setEmployeeEmail(employeeRequest.getEmployeeEmail().trim());
        employee.setEmployeeTelephone(employeeRequest.getEmployeeTelephone().trim());
        employee.setEmployeeLoginPassword(passwordEncoder.encode(employeeRequest.getEmployeeLoginPassword()));
        employee.setDepartmentId(Long.valueOf(employeeRequest.getDepartmentId().trim()));
        employee.setRole(Constants.ROLE_USER);

        EmployeeEntity employeeEntity = employeeRepository.save(employee);

        List<CertificationRequest> certifications = employeeRequest.getCertifications();
        if (certifications != null) {
            for (CertificationRequest certification : certifications) {
                if (certification == null
                        || commonValidator.isEmpty(certification.getCertificationId())) {
                    continue;
                }
                EmployeeCertificationEntity entity = new EmployeeCertificationEntity();
                entity.setEmployeeId(employeeEntity.getEmployeeId());
                entity.setCertificationId(Long.valueOf(certification.getCertificationId().trim()));
                entity.setStartDate(LocalDate.parse(
                        certification.getStartDate().trim(),
                        DATE_FORMATTER));
                entity.setEndDate(LocalDate.parse(
                        certification.getEndDate().trim(),
                        DATE_FORMATTER));
                entity.setScore(new BigDecimal(certification.getScore().trim()));
                employeeCertificationRepository.save(entity);
            }
        }
        return employeeEntity.getEmployeeId();
    }

    /**
     * Cập nhật thông tin nhân viên (ADM004) kèm chứng chỉ nếu có.
     * Mật khẩu chỉ được cập nhật khi có giá trị mới (được mã hóa BCrypt trước khi lưu).
     *
     * @param employeeRequest Dữ liệu nhân viên đã qua validate
     * @return ID nhân viên vừa được cập nhật
     * @throws AppException ER013 nếu không tìm thấy nhân viên, ER015 nếu lỗi cơ sở dữ liệu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateEmployee(EmployeeRequest employeeRequest) {
        try {
            Long employeeId = Long.valueOf(employeeRequest.getEmployeeId().trim());
            EmployeeEntity employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> {
                        String idLabel = messageSource != null
                                ? messageSource.getMessage(Constants.FIELD_ID, null, LocaleContextHolder.getLocale())
                                : "ＩＤ";
                        return new AppException(Constants.ER013, List.of(idLabel));
                    });

            employee.setDepartmentId(Long.valueOf(employeeRequest.getDepartmentId().trim()));
            employee.setEmployeeName(employeeRequest.getEmployeeName().trim());
            employee.setEmployeeNameKana(employeeRequest.getEmployeeNameKana().trim());
            employee.setEmployeeBirthDate(LocalDate.parse(
                    employeeRequest.getEmployeeBirthDate().trim(),
                    DATE_FORMATTER));
            employee.setEmployeeEmail(employeeRequest.getEmployeeEmail().trim());
            employee.setEmployeeTelephone(employeeRequest.getEmployeeTelephone().trim());
            employee.setEmployeeLoginId(employeeRequest.getEmployeeLoginId().trim());

            // Chỉ cập nhật mật khẩu nếu có nhập mật khẩu mới
            if (!commonValidator.isEmpty(employeeRequest.getEmployeeLoginPassword())) {
                employee.setEmployeeLoginPassword(passwordEncoder.encode(employeeRequest.getEmployeeLoginPassword()));
            }

            employeeRepository.save(employee);

            // Xóa toàn bộ chứng chỉ cũ
            employeeCertificationRepository.deleteByEmployeeId(employeeId);
            employeeCertificationRepository.flush();

            // Thêm chứng chỉ mới nếu có
            List<CertificationRequest> certifications = employeeRequest.getCertifications();
            if (certifications != null) {
                for (CertificationRequest certification : certifications) {
                    if (certification == null
                            || commonValidator.isEmpty(certification.getCertificationId())) {
                        continue;
                    }
                    EmployeeCertificationEntity entity = new EmployeeCertificationEntity();
                    entity.setEmployeeId(employeeId);
                    entity.setCertificationId(Long.valueOf(certification.getCertificationId().trim()));
                    entity.setStartDate(LocalDate.parse(
                            certification.getStartDate().trim(),
                            DATE_FORMATTER));
                    entity.setEndDate(LocalDate.parse(
                            certification.getEndDate().trim(),
                            DATE_FORMATTER));
                    entity.setScore(new BigDecimal(certification.getScore().trim()));
                    employeeCertificationRepository.save(entity);
                }
            }

            return employeeId;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(Constants.ER015);
        }
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

    /**
     * Lấy thông tin chi tiết một nhân viên theo ID (ADM003 / ADM004).
     *
     * @param employeeId ID của nhân viên cần lấy chi tiết
     * @return DTO chứa thông tin chi tiết nhân viên và danh sách chứng chỉ
     * @throws AppException Khi không tìm thấy nhân viên (ER013)
     */
    @Override
    public EmployeeDetailDTO getEmployeeDetail(Long employeeId) {
        List<Object[]> rows = employeeRepository.findEmployeeDetail(employeeId);

        if (rows == null || rows.isEmpty()) {
            String idLabel = messageSource != null
                    ? messageSource.getMessage(Constants.FIELD_ID, null, LocaleContextHolder.getLocale())
                    : "ＩＤ";
            throw new AppException(Constants.ER013, List.of(idLabel));
        }

        return employeeMapper.toDetailDTO(rows);
    }

    /**
     * Xóa một nhân viên và toàn bộ chứng chỉ liên quan khỏi hệ thống (ADM003).
     *
     * @param employeeId ID của nhân viên cần xóa
     * @throws AppException Khi không tìm thấy nhân viên (ER014) hoặc cố xóa Admin
     *                      (ER020)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEmployee(Long employeeId) {
        String idLabel = messageSource != null
                ? messageSource.getMessage(Constants.FIELD_ID, null, LocaleContextHolder.getLocale())
                : "ＩＤ";

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(Constants.ER014, List.of(idLabel)));

        if (Constants.ROLE_ADMIN == employee.getRole()
                || Constants.ADMIN_LOGIN_ID.equals(employee.getEmployeeLoginId())) {
            throw new AppException(Constants.ER020);
        }

        employeeCertificationRepository.deleteByEmployeeId(employeeId);
        employeeRepository.deleteById(employeeId);
    }
}
