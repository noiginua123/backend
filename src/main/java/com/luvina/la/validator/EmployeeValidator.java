/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidator.java, 21/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.luvina.la.constant.Constants;
import com.luvina.la.exception.AppException;

/**
 * Lớp kiểm tra tính hợp lệ của dữ liệu đầu vào cho các chức năng liên quan đến nhân viên.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeValidator {

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 5;

    /**
     * Kiểm tra tính hợp lệ của các tham số sắp xếp (chỉ chấp nhận rỗng hoặc "ASC", "DESC").
     *
     * @param order Giá trị hướng sắp xếp
     * @throws AppException khi giá trị không hợp lệ (mã lỗi ER021)
     */
    public void validateSortOrder(String order) {
        if (order != null && !order.trim().isEmpty()) {
            String trimmed = order.trim();
            if (!"ASC".equals(trimmed) && !"DESC".equals(trimmed)) {
                throw new AppException(Constants.ER021);
            }
        }
    }

    /**
     * Kiểm tra và parse chuỗi số nguyên không âm (tối đa 9 chữ số để an toàn tránh tràn int).
     *
     * @param raw Chuỗi cần parse
     * @param defaultValue Giá trị mặc định khi chuỗi rỗng/null
     * @param allowZero Cho phép giá trị 0 hay không
     * @param fieldLabel Nhãn dùng cho thông báo lỗi
     * @return Số nguyên hợp lệ
     * @throws AppException khi chuỗi không hợp lệ (mã lỗi ER018)
     */
    private int parseUnsignedInt(String raw, int defaultValue, boolean allowZero, String fieldLabel) {
        if (raw == null || raw.trim().isEmpty()) {
            return defaultValue;
        }
        String trimmed = raw.trim();
        if (!trimmed.matches("^[0-9]{1,9}$")) {
            throw new AppException(Constants.ER018, List.of(fieldLabel));
        }
        int value = Integer.parseInt(trimmed);
        if (!allowZero && value == 0) {
            throw new AppException(Constants.ER018, List.of(fieldLabel));
        }
        return value;
    }

    /**
     * Kiểm tra và chuyển đổi chuỗi offset sang số nguyên >= 0.
     *
     * @param offset Chuỗi offset
     * @return Số nguyên offset hợp lệ
     * @throws AppException khi chuỗi không phải số nguyên >= 0 (mã lỗi ER018)
     */
    public int validateAndParseOffset(String offset) {
        return parseUnsignedInt(offset, DEFAULT_OFFSET, true, "オフセット");
    }

    /**
     * Kiểm tra và chuyển đổi chuỗi limit sang số nguyên > 0.
     *
     * @param limit Chuỗi limit
     * @return Số nguyên limit hợp lệ
     * @throws AppException khi chuỗi không phải số nguyên > 0 (mã lỗi ER018)
     */
    public int validateAndParseLimit(String limit) {
        return parseUnsignedInt(limit, DEFAULT_LIMIT, false, "リミット");
    }

    /**
     * Chuyển đổi và kiểm tra chuỗi department_id sang Long (tối đa 18 chữ số để tránh tràn Long).
     *
     * @param departmentId Chuỗi ID phòng ban
     * @return Long ID phòng ban hoặc null nếu chuỗi rỗng
     * @throws AppException khi chuỗi không phải số nguyên dương (mã lỗi ER018)
     */
    public Long parseDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return null;
        }
        String trimmed = departmentId.trim();
        if (!trimmed.matches("^[0-9]{1,18}$")) {
            throw new AppException(Constants.ER018, List.of("部門ID"));
        }
        return Long.parseLong(trimmed);
    }

    /**
     * Escape các ký tự đặc biệt cho câu lệnh LIKE và bao quanh bởi '%'.
     *
     * @param employeeName Tên nhân viên gốc
     * @return Chuỗi đã escape hoặc null nếu tên nhân viên rỗng
     */
    public String escapeEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }
        String escaped = employeeName.trim()
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        return "%" + escaped + "%";
    }
}
