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

    /** Vị trí cột bắt đầu chứa thông tin chứng chỉ trong chi tiết nhân viên (ADM003). */
    private static final int CERTIFICATION_START_INDEX = 9;

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

        // Sử dụng biến index++ để lấy tuần tự giá trị các cột theo đúng thứ tự SELECT (ADM002)
        int index = 0;
        return new EmployeeListDTO(
                toLong(row[index++]),        // Cột 0: ID nhân viên
                toStr(row[index++]),         // Cột 1: Họ tên nhân viên
                toStr(row[index++]),         // Cột 2: Ngày sinh nhân viên (yyyy/MM/dd)
                toStr(row[index++]),         // Cột 3: Tên phòng ban
                toStr(row[index++]),         // Cột 4: Email nhân viên
                toStr(row[index++]),         // Cột 5: Số điện thoại nhân viên
                toStr(row[index++]),         // Cột 6: Tên chứng chỉ cao nhất
                toStr(row[index++]),         // Cột 7: Ngày hết hạn chứng chỉ (yyyy/MM/dd)
                toBigDecimal(row[index++]),  // Cột 8: Điểm số chứng chỉ
                toRole(row[index++])         // Cột 9: Quyền hạn (1: admin, 0: user)
        );
    }

    /**
     * Chuyển danh sách dòng kết quả truy vấn chi tiết nhân viên thành EmployeeDetailDTO.
     *
     * Do câu truy vấn sử dụng LEFT JOIN giữa bảng nhân viên và bảng chứng chỉ:
     * - Nếu nhân viên có N chứng chỉ, query trả về N dòng với thông tin nhân viên
     *   (các cột 0 - 8) lặp lại ở mỗi dòng.
     * - Nếu nhân viên không có chứng chỉ, query trả về 1 dòng và các cột chứng chỉ
     *   (các cột 9 - 13) mang giá trị null.
     *
     * @param rows Danh sách các dòng kết quả từ câu truy vấn EmployeeRepository.findEmployeeDetail
     * @return DTO chi tiết nhân viên kèm danh sách chứng chỉ, hoặc null nếu danh sách rỗng
     * @throws IllegalArgumentException Khi dòng dữ liệu không đủ số cột tối thiểu
     */
    public EmployeeDetailDTO toDetailDTO(List<Object[]> rows) {
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        // Dòng đầu tiên luôn tồn tại và chứa đầy đủ thông tin cơ bản của nhân viên
        Object[] firstRow = rows.get(0);
        if (firstRow == null || firstRow.length < EXPECTED_DETAIL_COLUMN_COUNT) {
            throw new IllegalArgumentException(
                    "Employee detail query must return at least " + EXPECTED_DETAIL_COLUMN_COUNT + " columns"
            );
        }

        // Trích xuất thông tin chung của nhân viên từ dòng đầu tiên (các cột 0 - 8)
        // Dùng biến index++ để lấy tuần tự theo đúng thứ tự SELECT của ADM003
        int index = 0;
        Long employeeId = toLong(firstRow[index++]);            // Cột 0: ID nhân viên
        String employeeLoginId = toStr(firstRow[index++]);      // Cột 1: Tên đăng nhập nhân viên
        String employeeName = toStr(firstRow[index++]);         // Cột 2: Họ tên nhân viên
        String employeeNameKana = toStr(firstRow[index++]);     // Cột 3: Họ tên theo Katakana
        String employeeBirthDate = toStr(firstRow[index++]);    // Cột 4: Ngày sinh nhân viên (yyyy/MM/dd)
        String employeeEmail = toStr(firstRow[index++]);        // Cột 5: Email nhân viên
        String employeeTelephone = toStr(firstRow[index++]);    // Cột 6: Số điện thoại nhân viên
        Long departmentId = toLong(firstRow[index++]);          // Cột 7: ID phòng ban
        String departmentName = toStr(firstRow[index++]);       // Cột 8: Tên phòng ban

        // Gom danh sách chứng chỉ của nhân viên từ tất cả các dòng kết quả (các cột 9 - 13)
        List<EmployeeCertificationDTO> employeeCertifications = new ArrayList<>();
        for (Object[] row : rows) {
            // Cột certificationId bắt đầu từ CERTIFICATION_START_INDEX = 9.
            // Nếu certificationId khác null nghĩa là nhân viên có chứng chỉ ở dòng này (do LEFT JOIN)
            if (row != null && row.length >= EXPECTED_DETAIL_COLUMN_COUNT && row[CERTIFICATION_START_INDEX] != null) {
                int certIndex = CERTIFICATION_START_INDEX;
                employeeCertifications.add(EmployeeCertificationDTO.builder()
                        .certificationId(toLong(row[certIndex++]))       // Cột 9: ID chứng chỉ
                        .certificationName(toStr(row[certIndex++]))     // Cột 10: Tên chứng chỉ
                        .startDate(toStr(row[certIndex++]))             // Cột 11: Ngày cấp chứng chỉ (yyyy/MM/dd)
                        .endDate(toStr(row[certIndex++]))               // Cột 12: Ngày hết hạn chứng chỉ (yyyy/MM/dd)
                        .score(toBigDecimal(row[certIndex++]))          // Cột 13: Điểm số chứng chỉ
                        .build());
            }
        }

        // Tạo và trả về DTO chi tiết nhân viên hoàn chỉnh
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
