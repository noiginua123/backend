/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImplTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.mapper.EmployeeMapper;
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

    private EmployeeServiceImpl employeeService;

    /**
     * Khởi tạo service với repository và mapper giả lập trước mỗi test.
     */
    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeMapper = mock(EmployeeMapper.class);
        EmployeeCertificationRepository employeeCertificationRepository = mock(EmployeeCertificationRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
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
}
