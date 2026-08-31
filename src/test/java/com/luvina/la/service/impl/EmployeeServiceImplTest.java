/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImplTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.validator.EmployeeValidator;

/**
 * Kiểm thử quy tắc chuẩn hóa tham số sắp xếp của ADM002.
 *
 * @author thanhvinh
 */
class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;

    private EmployeeServiceImpl employeeService;

    /**
     * Khởi tạo service và repository giả lập trước mỗi test.
     */
    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        EmployeeMapper employeeMapper = mock(EmployeeMapper.class);
        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                employeeMapper,
                new EmployeeValidator()
        );
        when(employeeRepository.countEmployees(any(), any())).thenReturn(1L);
        when(employeeRepository.searchEmployees(
                any(), any(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()
        )).thenReturn(Collections.emptyList());
    }

    /**
     * Kiểm tra cột tên ưu tiên rỗng được mặc định ASC.
     */
    @Test
    void shouldDefaultEmployeeNameOrderToAsc() {
        employeeService.searchEmployees(createRequest("", "DESC", "DESC", "employeeName"));

        verifySearch("ASC", "DESC", "DESC", "employeeName");
    }

    /**
     * Kiểm tra cột chứng chỉ ưu tiên rỗng được mặc định ASC.
     */
    @Test
    void shouldDefaultCertificationNameOrderToAsc() {
        employeeService.searchEmployees(createRequest("DESC", "", "DESC", "certificationName"));

        verifySearch("DESC", "ASC", "DESC", "certificationName");
    }

    /**
     * Kiểm tra cột ngày hết hạn ưu tiên rỗng được mặc định ASC.
     */
    @Test
    void shouldDefaultEndDateOrderToAsc() {
        employeeService.searchEmployees(createRequest("DESC", "DESC", "", "endDate"));

        verifySearch("DESC", "DESC", "ASC", "endDate");
    }

    /**
     * Kiểm tra cả ba hướng rỗng đều được chuẩn hóa thành ASC.
     */
    @Test
    void shouldDefaultAllEmptyOrdersToAsc() {
        employeeService.searchEmployees(createRequest("", "", "", "employeeName"));

        verifySearch("ASC", "ASC", "ASC", "employeeName");
    }

    /**
     * Tạo request chỉ chứa cấu hình sort cần kiểm thử.
     *
     * @param employeeNameOrder Hướng sort tên
     * @param certificationNameOrder Hướng sort chứng chỉ
     * @param endDateOrder Hướng sort ngày hết hạn
     * @param prioritySort Cột sort ưu tiên
     * @return Request dùng cho test
     */
    private EmployeeSearchRequest createRequest(
            String employeeNameOrder,
            String certificationNameOrder,
            String endDateOrder,
            String prioritySort) {
        return new EmployeeSearchRequest(
                "",
                "",
                employeeNameOrder,
                certificationNameOrder,
                endDateOrder,
                prioritySort,
                "",
                ""
        );
    }

    /**
     * Xác minh Repository nhận đúng cấu hình sort đã chuẩn hóa.
     *
     * @param employeeNameOrder Hướng sort tên mong đợi
     * @param certificationNameOrder Hướng sort chứng chỉ mong đợi
     * @param endDateOrder Hướng sort ngày hết hạn mong đợi
     * @param prioritySort Cột sort ưu tiên mong đợi
     */
    private void verifySearch(
            String employeeNameOrder,
            String certificationNameOrder,
            String endDateOrder,
            String prioritySort) {
        verify(employeeRepository).searchEmployees(
                null,
                null,
                employeeNameOrder,
                certificationNameOrder,
                endDateOrder,
                prioritySort,
                5,
                0
        );
    }
}
