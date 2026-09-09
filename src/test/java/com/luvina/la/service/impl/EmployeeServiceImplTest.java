/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImplTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.entity.EmployeeCertificationEntity;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.exception.AppException;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.CertificationRequest;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.validator.CommonValidator;

/**
 * Kiểm thử service danh sách nhân viên: uỷ quyền truy vấn Repository và mapping DTO.
 *
 * <p>Việc kiểm tra và chuẩn hóa tham số đã chuyển sang tầng Controller nên được
 * kiểm thử ở {@code EmployeeControllerTest}.</p>
 *
 * @author thanhvinh
 */
class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;

    private EmployeeMapper employeeMapper;

    private EmployeeCertificationRepository employeeCertificationRepository;

    private PasswordEncoder passwordEncoder;

    private EmployeeServiceImpl employeeService;

    /**
     * Khởi tạo service với repository và mapper giả lập trước mỗi test.
     */
    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeMapper = mock(EmployeeMapper.class);
        employeeCertificationRepository = mock(EmployeeCertificationRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                employeeMapper,
                employeeCertificationRepository,
                passwordEncoder,
                new CommonValidator()
        );
    }

    /**
     * Kiểm tra getTotalRecords trả đúng số bản ghi và truyền admin login id xuống Repository.
     */
    @Test
    void shouldReturnTotalRecordsFromRepository() {
        when(employeeRepository.countEmployees("%an%", 5L, Constants.ADMIN_LOGIN_ID))
                .thenReturn(9L);

        long total = employeeService.getTotalRecords("%an%", 5L);

        assertEquals(9L, total);
        verify(employeeRepository).countEmployees("%an%", 5L, Constants.ADMIN_LOGIN_ID);
    }

    /**
     * Kiểm tra getEmployees truyền đủ tham số (kèm admin login id) và mapping kết quả.
     */
    @Test
    void shouldPassArgumentsAndMapRowsWhenSearching() {
        Object[] row = new Object[] {1L};
        EmployeeListDTO dto = mock(EmployeeListDTO.class);
        when(employeeRepository.searchEmployees(
                "%an%", 5L, "ASC", "DESC", "ASC", "employeeName",
                Constants.ADMIN_LOGIN_ID, 20, 0
        )).thenReturn(Collections.singletonList(row));
        when(employeeMapper.toDTO(row)).thenReturn(dto);

        List<EmployeeListDTO> result = employeeService.getEmployees(
                "%an%", 5L, "ASC", "DESC", "ASC", "employeeName", 20, 0);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(employeeRepository).searchEmployees(
                "%an%", 5L, "ASC", "DESC", "ASC", "employeeName",
                Constants.ADMIN_LOGIN_ID, 20, 0);
    }

    /**
     * Kiểm tra checkExistsEmployeeById trả về true khi nhân viên tồn tại.
     */
    @Test
    void shouldReturnTrueWhenEmployeeExists() {
        when(employeeRepository.existsById(10L)).thenReturn(true);

        boolean exists = employeeService.checkExistsEmployeeById(10L);

        assertTrue(exists);
        verify(employeeRepository).existsById(10L);
    }

    /**
     * Kiểm tra checkExistsEmployeeById trả về false khi nhân viên không tồn tại.
     */
    @Test
    void shouldReturnFalseWhenEmployeeDoesNotExist() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        boolean exists = employeeService.checkExistsEmployeeById(99L);

        assertFalse(exists);
        verify(employeeRepository).existsById(99L);
    }

    /**
     * Kiểm tra checkExistsEmployeeById trả về false khi truyền null.
     */
    @Test
    void shouldReturnFalseWhenEmployeeIdIsNull() {
        boolean exists = employeeService.checkExistsEmployeeById(null);

        assertFalse(exists);
        verify(employeeRepository, never()).existsById(any());
    }

    /**
     * Kiểm tra cập nhật nhân viên thành công khi có thay đổi mật khẩu và chứng chỉ.
     */
    @Test
    void shouldUpdateEmployeeSuccessfullyWhenPasswordAndCertificationProvided() {
        EmployeeEntity existing = new EmployeeEntity();
        existing.setEmployeeId(1L);
        existing.setEmployeeLoginPassword("old_hashed_password");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newPassword123")).thenReturn("new_hashed_password");

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeId("1");
        request.setEmployeeLoginId("updated_login");
        request.setDepartmentId("2");
        request.setEmployeeName("Nguyen Van B");
        request.setEmployeeNameKana("ｱｲｳｴｵ");
        request.setEmployeeBirthDate("1995/05/15");
        request.setEmployeeEmail("updated@example.com");
        request.setEmployeeTelephone("0987654321");
        request.setEmployeeLoginPassword("newPassword123");

        CertificationRequest cert = new CertificationRequest();
        cert.setCertificationId("2");
        cert.setStartDate("2023/01/01");
        cert.setEndDate("2024/01/01");
        cert.setScore("850");
        request.setCertifications(List.of(cert));

        Long updatedId = employeeService.updateEmployee(request);

        assertEquals(1L, updatedId);
        assertEquals("new_hashed_password", existing.getEmployeeLoginPassword());
        assertEquals("updated_login", existing.getEmployeeLoginId());
        assertEquals("Nguyen Van B", existing.getEmployeeName());
        verify(passwordEncoder).encode("newPassword123");
        verify(employeeRepository).save(existing);
        verify(employeeCertificationRepository).deleteByEmployeeId(1L);
        verify(employeeCertificationRepository).flush();
        verify(employeeCertificationRepository).save(any(EmployeeCertificationEntity.class));
    }

    /**
     * Kiểm tra cập nhật nhân viên giữ nguyên mật khẩu cũ khi mật khẩu trong request rỗng.
     */
    @Test
    void shouldKeepExistingPasswordWhenUpdatingWithEmptyPassword() {
        EmployeeEntity existing = new EmployeeEntity();
        existing.setEmployeeId(1L);
        existing.setEmployeeLoginPassword("old_hashed_password");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeId("1");
        request.setEmployeeLoginId("updated_login");
        request.setDepartmentId("2");
        request.setEmployeeName("Nguyen Van B");
        request.setEmployeeNameKana("ｱｲｳｴｵ");
        request.setEmployeeBirthDate("1995/05/15");
        request.setEmployeeEmail("updated@example.com");
        request.setEmployeeTelephone("0987654321");
        request.setEmployeeLoginPassword("");

        Long updatedId = employeeService.updateEmployee(request);

        assertEquals(1L, updatedId);
        assertEquals("old_hashed_password", existing.getEmployeeLoginPassword());
        verify(passwordEncoder, never()).encode(any());
        verify(employeeRepository).save(existing);
    }

    /**
     * Kiểm tra ném ngoại lệ ER013 khi nhân viên cần cập nhật không tồn tại trong hệ thống.
     */
    @Test
    void shouldThrowAppExceptionER013WhenUpdatingNonExistentEmployee() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeId("99");

        AppException ex = assertThrows(AppException.class, () -> employeeService.updateEmployee(request));
        assertEquals(Constants.ER013, ex.getCode());
    }
}
