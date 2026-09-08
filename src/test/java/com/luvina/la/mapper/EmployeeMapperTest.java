/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapperTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;

/**
 * Kiểm thử mapping theo thứ tự cột native query của ADM002 và ADM003.
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

    /**
     * Kiểm tra mapping chi tiết nhân viên có kèm nhiều chứng chỉ.
     */
    @Test
    void shouldMapEmployeeDetailWithMultipleCertifications() {
        Object[] row1 = new Object[] {
                1L, "vinhnt", "Nguyen Thanh Vinh", "グエン・タイン・ビン", "2000/01/01",
                "vinh@example.com", "0901234567", 2L, "Kỹ thuật",
                10L, "N1", "2023/01/01", "2024/01/01", new BigDecimal("170.5")
        };
        Object[] row2 = new Object[] {
                1L, "vinhnt", "Nguyen Thanh Vinh", "グエン・タイン・ビン", "2000/01/01",
                "vinh@example.com", "0901234567", 2L, "Kỹ thuật",
                11L, "N2", "2021/01/01", "2022/01/01", 160
        };

        EmployeeDetailDTO result = employeeMapper.toDetailDTO(List.of(row1, row2));

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertEquals("vinhnt", result.getEmployeeLoginId());
        assertEquals("Nguyen Thanh Vinh", result.getEmployeeName());
        assertEquals("グエン・タイン・ビン", result.getEmployeeNameKana());
        assertEquals("2000/01/01", result.getEmployeeBirthDate());
        assertEquals("vinh@example.com", result.getEmployeeEmail());
        assertEquals("0901234567", result.getEmployeeTelephone());
        assertEquals(2L, result.getDepartmentId());
        assertEquals("Kỹ thuật", result.getDepartmentName());

        assertEquals(2, result.getCertifications().size());
        assertEquals(10L, result.getCertifications().get(0).getCertificationId());
        assertEquals("N1", result.getCertifications().get(0).getCertificationName());
        assertEquals(new BigDecimal("170.5"), result.getCertifications().get(0).getScore());
        assertEquals(11L, result.getCertifications().get(1).getCertificationId());
        assertEquals(new BigDecimal("160"), result.getCertifications().get(1).getScore());
    }

    /**
     * Kiểm tra mapping chi tiết nhân viên khi không có chứng chỉ nào (LEFT JOIN trả null).
     */
    @Test
    void shouldMapEmployeeDetailWithoutCertifications() {
        Object[] row = new Object[] {
                2L, "user2", "Tran Thi B", "チャン・ティ・ビー", "1995/05/10",
                "b@example.com", "0987654321", 1L, "Phát triển",
                null, null, null, null, null
        };

        EmployeeDetailDTO result = employeeMapper.toDetailDTO(Collections.singletonList(row));

        assertNotNull(result);
        assertEquals(2L, result.getEmployeeId());
        assertEquals("user2", result.getEmployeeLoginId());
        assertTrue(result.getCertifications().isEmpty());
    }

    /**
     * Kiểm tra mapper trả null khi danh sách dòng rỗng hoặc null.
     */
    @Test
    void shouldReturnNullWhenDetailRowsEmptyOrNull() {
        assertNull(employeeMapper.toDetailDTO(null));
        assertNull(employeeMapper.toDetailDTO(Collections.emptyList()));
    }

    /**
     * Kiểm tra mapper từ chối row chi tiết khi không đủ số cột mong đợi.
     */
    @Test
    void shouldRejectUnexpectedDetailColumnCount() {
        Object[] invalidRow = new Object[] {1L, "vinhnt"};
        assertThrows(
                IllegalArgumentException.class,
                () -> employeeMapper.toDetailDTO(Collections.singletonList(invalidRow))
        );
    }
}
