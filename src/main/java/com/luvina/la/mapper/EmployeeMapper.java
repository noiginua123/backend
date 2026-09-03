/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapper.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.luvina.la.dto.EmployeeListDTO;

/**
 * Chuyển mảng cột của native query danh sách nhân viên sang DTO response.
 *
 * THỨ TỰ cột phải luôn khớp với SELECT trong EmployeeRepository:
 * employeeId, employeeName, employeeBirthDate, departmentName, employeeEmail,
 * employeeTelephone, certificationName, endDate, score, role. Khi đổi hoặc thêm
 * cột SELECT, bắt buộc sửa các chỉ số trong mapper này.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeMapper {

    /** Vị trí cột ID nhân viên trong mảng kết quả truy vấn native. */
    private static final int IDX_EMPLOYEE_ID = 0;

    /** Vị trí cột họ tên nhân viên trong mảng kết quả truy vấn native. */
    private static final int IDX_EMPLOYEE_NAME = 1;

    /** Vị trí cột ngày sinh nhân viên trong mảng kết quả truy vấn native. */
    private static final int IDX_EMPLOYEE_BIRTH_DATE = 2;

    /** Vị trí cột tên phòng ban trong mảng kết quả truy vấn native. */
    private static final int IDX_DEPARTMENT_NAME = 3;

    /** Vị trí cột email nhân viên trong mảng kết quả truy vấn native. */
    private static final int IDX_EMPLOYEE_EMAIL = 4;

    /** Vị trí cột số điện thoại nhân viên trong mảng kết quả truy vấn native. */
    private static final int IDX_EMPLOYEE_TELEPHONE = 5;

    /** Vị trí cột tên chứng chỉ trong mảng kết quả truy vấn native. */
    private static final int IDX_CERTIFICATION_NAME = 6;

    /** Vị trí cột ngày hết hạn chứng chỉ trong mảng kết quả truy vấn native. */
    private static final int IDX_END_DATE = 7;

    /** Vị trí cột điểm số chứng chỉ trong mảng kết quả truy vấn native. */
    private static final int IDX_SCORE = 8;

    /** Vị trí cột vai trò nhân viên trong mảng kết quả truy vấn native. */
    private static final int IDX_ROLE = 9;

    /** Số lượng cột mong đợi trả về từ câu truy vấn native. */
    private static final int EXPECTED_COLUMN_COUNT = 10;

    /**
     * Chuyển mảng cột kết quả truy vấn thành DTO.
     *
     * @param row Mảng cột theo đúng thứ tự SELECT của EmployeeRepository
     * @return DTO nhân viên tương ứng hoặc null
     * @throws IllegalArgumentException Khi số cột hoặc kiểu dữ liệu không khớp
     */
    public EmployeeListDTO toDTO(Object[] row) {
        if (row == null) {
            return null;
        }
        if (row.length != EXPECTED_COLUMN_COUNT) {
            throw new IllegalArgumentException("Employee query must return exactly 10 columns");
        }

        return new EmployeeListDTO(
                toLong(row[IDX_EMPLOYEE_ID]),
                toStr(row[IDX_EMPLOYEE_NAME]),
                toStr(row[IDX_EMPLOYEE_BIRTH_DATE]),
                toStr(row[IDX_DEPARTMENT_NAME]),
                toStr(row[IDX_EMPLOYEE_EMAIL]),
                toStr(row[IDX_EMPLOYEE_TELEPHONE]),
                toStr(row[IDX_CERTIFICATION_NAME]),
                toStr(row[IDX_END_DATE]),
                toBigDecimal(row[IDX_SCORE]),
                toRole(row[IDX_ROLE])
        );
    }

    /**
     * Chuyển giá trị Number sang Long.
     *
     * @param value Giá trị cần chuyển
     * @return Giá trị Long hoặc null
     * @throws IllegalArgumentException Khi giá trị không phải Number
     */
    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("Employee ID must be numeric");
        }
        return ((Number) value).longValue();
    }

    /**
     * Chuyển giá trị sang chuỗi.
     *
     * @param value Giá trị cần chuyển
     * @return Chuỗi tương ứng hoặc null
     */
    private String toStr(Object value) {
        return value == null ? null : value.toString();
    }

    /**
     * Chuyển giá trị số sang BigDecimal.
     *
     * @param value Giá trị cần chuyển
     * @return Giá trị BigDecimal hoặc null
     * @throws IllegalArgumentException Khi giá trị không phải Number
     */
    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        throw new IllegalArgumentException("Employee score must be numeric");
    }

    /**
     * Quy đổi role dạng Boolean hoặc Number thành 1/0.
     *
     * @param value Giá trị role từ native query
     * @return 1 khi role là true/khác 0; ngược lại là 0
     * @throws IllegalArgumentException Khi kiểu dữ liệu không được hỗ trợ
     */
    private Integer toRole(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? 1 : 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 0 ? 0 : 1;
        }
        throw new IllegalArgumentException("Employee role must be boolean or numeric");
    }
}
