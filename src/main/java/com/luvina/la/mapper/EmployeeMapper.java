/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapper.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.luvina.la.dto.EmployeeCertificationDTO;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;

/**
 * Chuyển mảng cột của native query danh sách và chi tiết nhân viên sang DTO.
 *
 * THỨ TỰ cột phải luôn khớp với SELECT trong EmployeeRepository.
 * Khi đổi hoặc thêm cột SELECT, bắt buộc sửa các chỉ số trong mapper này.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeMapper {

    /** Số lượng cột mong đợi trả về từ câu truy vấn danh sách nhân viên (ADM002). */
    private static final int EXPECTED_LIST_COLUMN_COUNT = 10;

    /** Số lượng cột mong đợi trả về từ câu truy vấn chi tiết nhân viên (ADM003). */
    private static final int EXPECTED_DETAIL_COLUMN_COUNT = 14;

    /**
     * Chuyển mảng cột kết quả truy vấn danh sách nhân viên thành EmployeeListDTO.
     *
     * @param row Mảng cột theo đúng thứ tự SELECT của EmployeeRepository.searchEmployees
     * @return DTO nhân viên tương ứng hoặc null nếu row rỗng
     * @throws IllegalArgumentException Khi số cột không khớp với truy vấn danh sách
     */
    public EmployeeListDTO toDTO(Object[] row) {
        if (row == null) {
            return null;
        }
        if (row.length != EXPECTED_LIST_COLUMN_COUNT) {
            throw new IllegalArgumentException(
                    "Employee query must return exactly " + EXPECTED_LIST_COLUMN_COUNT + " columns"
            );
        }

        return new EmployeeListDTO(
                toLong(row[0]),        // 0: employeeId
                toStr(row[1]),         // 1: employeeName
                toStr(row[2]),         // 2: employeeBirthDate (yyyy/MM/dd)
                toStr(row[3]),         // 3: departmentName
                toStr(row[4]),         // 4: employeeEmail
                toStr(row[5]),         // 5: employeeTelephone
                toStr(row[6]),         // 6: certificationName
                toStr(row[7]),         // 7: endDate (yyyy/MM/dd)
                toBigDecimal(row[8]),  // 8: score
                toRole(row[9])         // 9: role
        );
    }

    /**
     * Chuyển danh sách dòng kết quả truy vấn chi tiết nhân viên thành EmployeeDetailDTO.
     *
     * @param rows Danh sách các dòng kết quả từ câu truy vấn EmployeeRepository.findEmployeeDetail
     * @return DTO chi tiết nhân viên kèm danh sách chứng chỉ, hoặc null nếu danh sách rỗng
     * @throws IllegalArgumentException Khi dòng dữ liệu không đủ số cột tối thiểu
     */
    public EmployeeDetailDTO toDetailDTO(List<Object[]> rows) {
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        Object[] firstRow = rows.get(0);
        if (firstRow == null || firstRow.length < EXPECTED_DETAIL_COLUMN_COUNT) {
            throw new IllegalArgumentException(
                    "Employee detail query must return at least " + EXPECTED_DETAIL_COLUMN_COUNT + " columns"
            );
        }

        Long employeeId = toLong(firstRow[0]);            // 0: employeeId
        String employeeLoginId = toStr(firstRow[1]);      // 1: employeeLoginId
        String employeeName = toStr(firstRow[2]);         // 2: employeeName
        String employeeNameKana = toStr(firstRow[3]);     // 3: employeeNameKana
        String employeeBirthDate = toStr(firstRow[4]);    // 4: employeeBirthDate (yyyy/MM/dd)
        String employeeEmail = toStr(firstRow[5]);        // 5: employeeEmail
        String employeeTelephone = toStr(firstRow[6]);    // 6: employeeTelephone
        Long departmentId = toLong(firstRow[7]);          // 7: departmentId
        String departmentName = toStr(firstRow[8]);       // 8: departmentName

        List<EmployeeCertificationDTO> employeeCertifications = new ArrayList<>();
        for (Object[] row : rows) {
            // Cột 9 là certificationId, nếu null nghĩa là nhân viên không có chứng chỉ (do LEFT JOIN)
            if (row != null && row.length >= EXPECTED_DETAIL_COLUMN_COUNT && row[9] != null) {
                employeeCertifications.add(EmployeeCertificationDTO.builder()
                        .certificationId(toLong(row[9]))       // 9: certificationId
                        .certificationName(toStr(row[10]))     // 10: certificationName
                        .startDate(toStr(row[11]))             // 11: startDate (yyyy/MM/dd)
                        .endDate(toStr(row[12]))               // 12: endDate (yyyy/MM/dd)
                        .score(toBigDecimal(row[13]))          // 13: score
                        .build());
            }
        }

        return EmployeeDetailDTO.builder()
                .employeeId(employeeId)
                .employeeLoginId(employeeLoginId)
                .employeeName(employeeName)
                .employeeNameKana(employeeNameKana)
                .employeeBirthDate(employeeBirthDate)
                .employeeEmail(employeeEmail)
                .employeeTelephone(employeeTelephone)
                .departmentId(departmentId)
                .departmentName(departmentName)
                .certifications(employeeCertifications)
                .build();
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
            throw new IllegalArgumentException("Numeric value expected");
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
