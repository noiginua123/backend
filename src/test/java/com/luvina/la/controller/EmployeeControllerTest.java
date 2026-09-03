/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeControllerTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.http.ResponseEntity;

import com.luvina.la.constant.Constants;
import com.luvina.la.constant.SortField;
import com.luvina.la.payload.request.EmployeeSearchRequest;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.CommonValidator;
import com.luvina.la.validator.EmployeeValidator;

/**
 * Kiểm thử tầng Controller ADM002: chuẩn hóa tham số sắp xếp và điều phối Service.
 *
 * @author thanhvinh
 */
class EmployeeControllerTest {

    private EmployeeService employeeService;

    private EmployeeController employeeController;

    /**
     * Khởi tạo controller với service giả lập và validator thật trước mỗi test.
     */
    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        CertificationRepository certificationRepository = mock(CertificationRepository.class);
        EmployeeValidator employeeValidator =
                new EmployeeValidator(
                        new CommonValidator(),
                        createMessageSource(),
                        employeeRepository,
                        departmentRepository,
                        certificationRepository
                );
        employeeController = new EmployeeController(employeeService, employeeValidator);
        when(employeeService.getTotalRecords(any(), any())).thenReturn(1L);
        when(employeeService.getEmployees(
                any(), any(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()
        )).thenReturn(Collections.emptyList());
    }

    /**
     * Tạo nguồn nhãn tối thiểu cho validator trong unit test.
     *
     * @return Message source chứa nhãn offset và limit
     */
    private StaticMessageSource createMessageSource() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("field.offset", Locale.getDefault(), "offset");
        messageSource.addMessage("field.limit", Locale.getDefault(), "limit");
        return messageSource;
    }

    /**
     * Kiểm tra hướng sắp xếp tên nhân viên rỗng được chuẩn hóa thành ASC.
     */
    @Test
    void shouldDefaultEmployeeNameOrderToAsc() {
        employeeController.getEmployees(createRequest("", "DESC", "DESC"));

        verifyGetEmployees("ASC", "DESC", "DESC");
    }

    /**
     * Kiểm tra cả ba hướng sắp xếp rỗng đều được chuẩn hóa thành ASC.
     */
    @Test
    void shouldDefaultAllEmptyOrdersToAsc() {
        employeeController.getEmployees(createRequest("", "", ""));

        verifyGetEmployees("ASC", "ASC", "ASC");
    }

    /**
     * Kiểm tra khi không có bản ghi thì không truy vấn danh sách và trả về rỗng.
     */
    @Test
    void shouldNotQueryListWhenNoRecords() {
        when(employeeService.getTotalRecords(any(), any())).thenReturn(0L);

        ResponseEntity<ListEmployeeResponse> response =
                employeeController.getEmployees(createRequest("ASC", "ASC", "ASC"));

        verify(employeeService, never()).getEmployees(
                any(), any(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
        assertEquals(Constants.CODE_SUCCESS, response.getBody().getCode());
        assertEquals(0L, response.getBody().getTotalRecords().longValue());
        assertTrue(response.getBody().getEmployees().isEmpty());
    }

    /**
     * Tạo request chỉ khác nhau ở cấu hình hướng sắp xếp.
     *
     * @param employeeNameOrder Hướng sort tên nhân viên
     * @param certificationNameOrder Hướng sort tên chứng chỉ
     * @param endDateOrder Hướng sort ngày hết hạn
     * @return Request dùng cho test
     */
    private EmployeeSearchRequest createRequest(String employeeNameOrder,
                                                String certificationNameOrder,
                                                String endDateOrder) {
        return new EmployeeSearchRequest(
                "",
                "",
                employeeNameOrder,
                certificationNameOrder,
                endDateOrder,
                SortField.EMPLOYEE_NAME_VALUE,
                "",
                ""
        );
    }

    /**
     * Xác minh Service nhận đúng hướng sắp xếp đã chuẩn hóa và tham số phân trang mặc định.
     *
     * @param ordEmployeeName Hướng sort tên mong đợi
     * @param ordCertificationName Hướng sort chứng chỉ mong đợi
     * @param ordEndDate Hướng sort ngày hết hạn mong đợi
     */
    private void verifyGetEmployees(String ordEmployeeName,
                                    String ordCertificationName,
                                    String ordEndDate) {
        verify(employeeService).getEmployees(
                null,
                null,
                ordEmployeeName,
                ordCertificationName,
                ordEndDate,
                SortField.EMPLOYEE_NAME_VALUE,
                Constants.DEFAULT_EMPLOYEE_PAGE_SIZE,
                0
        );
    }
}
