/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapperTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.luvina.la.dto.EmployeeListDTO;

/**
 * Kiểm thử mapping theo thứ tự cột native query của ADM002.
 *
 * @author thanhvinh
 */
class EmployeeMapperTest {

    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    /**
     * Kiểm tra mapping đủ mười cột và quy đổi role Boolean.
     */
    @Test
    void shouldMapEmployeeRow() {
        Object[] row = new Object[] {
                10,
                "Nguyen Van A",
                "2000/01/01",
                "DEV",
                "a@example.com",
                "0900000000",
                "N1",
                "2030/01/01",
                180,
                true
        };

        EmployeeListDTO result = employeeMapper.toDTO(row);

        assertEquals(10L, result.getEmployeeId());
        assertEquals("Nguyen Van A", result.getEmployeeName());
        assertEquals(new BigDecimal("180"), result.getScore());
        assertEquals(1, result.getRole());
    }

    /**
     * Kiểm tra các cột chứng chỉ null và role số 0.
     */
    @Test
    void shouldMapNullableCertificationAndNumericRole() {
        Object[] row = new Object[] {
                11L,
                "Tran Thi B",
                null,
                "QA",
                "b@example.com",
                null,
                null,
                null,
                null,
                0
        };

        EmployeeListDTO result = employeeMapper.toDTO(row);

        assertNull(result.getCertificationName());
        assertNull(result.getScore());
        assertEquals(0, result.getRole());
    }

    /**
     * Kiểm tra mapper từ chối row sai số lượng cột.
     */
    @Test
    void shouldRejectUnexpectedColumnCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeMapper.toDTO(new Object[] {1L})
        );
    }
}
