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
 * Kiểm tra dữ liệu đầu vào của chức năng danh sách nhân viên.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeValidator {

    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_LIMIT = 5;

    /**
     * Kiểm tra hướng sắp xếp, chỉ chấp nhận rỗng, ASC hoặc DESC.
     *
     * @param order Hướng sắp xếp
     * @throws AppException Khi hướng sắp xếp không hợp lệ
     */
    public void validateSortOrder(String order) {
        if (order != null && !order.trim().isEmpty()) {
            String trimmedOrder = order.trim();
            if (!"ASC".equals(trimmedOrder) && !"DESC".equals(trimmedOrder)) {
                throw new AppException(Constants.ER021);
            }
        }
    }

    /**
     * Kiểm tra và chuyển offset sang số nguyên không âm.
     *
     * @param offset Offset dạng chuỗi
     * @return Offset hợp lệ
     * @throws AppException Khi offset không phải số nguyên không âm
     */
    public int validateAndParseOffset(String offset) {
        return parseUnsignedInt(offset, DEFAULT_OFFSET, true, "オフセット");
    }

    /**
     * Kiểm tra và chuyển limit sang số nguyên dương.
     *
     * @param limit Limit dạng chuỗi
     * @return Limit hợp lệ
     * @throws AppException Khi limit không phải số nguyên dương
     */
    public int validateAndParseLimit(String limit) {
        return parseUnsignedInt(limit, DEFAULT_LIMIT, false, "リミット");
    }

    /**
     * Chuyển ID phòng ban sang số nguyên dương.
     *
     * @param departmentId ID phòng ban dạng chuỗi
     * @return ID phòng ban hoặc null
     * @throws AppException Khi ID phòng ban không hợp lệ
     */
    public Long parseDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return null;
        }

        String trimmedDepartmentId = departmentId.trim();
        if (!trimmedDepartmentId.matches("^[0-9]{1,18}$")) {
            throw new AppException(
                    Constants.ER018,
                    List.of(Constants.FIELD_LABEL_DEPARTMENT_ID)
            );
        }

        Long parsedDepartmentId = Long.valueOf(trimmedDepartmentId);
        if (parsedDepartmentId == 0L) {
            throw new AppException(
                    Constants.ER018,
                    List.of(Constants.FIELD_LABEL_DEPARTMENT_ID)
            );
        }
        return parsedDepartmentId;
    }

    /**
     * Kiểm tra độ dài và escape tên nhân viên cho điều kiện LIKE.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @return Mẫu LIKE đã escape hoặc null
     * @throws AppException Khi tên nhân viên vượt quá 125 ký tự
     */
    public String validateAndEscapeEmployeeName(String employeeName) {
        if (employeeName == null) {
            return null;
        }

        int characterCount = employeeName.codePointCount(0, employeeName.length());
        if (characterCount > Constants.EMPLOYEE_NAME_MAX_LENGTH) {
            throw new AppException(
                    Constants.ER006,
                    List.of(Constants.EMPLOYEE_NAME_MAX_LENGTH, Constants.FIELD_LABEL_FULLNAME)
            );
        }

        if (employeeName.trim().isEmpty()) {
            return null;
        }

        String escapedEmployeeName = employeeName.trim()
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        return "%" + escapedEmployeeName + "%";
    }

    /**
     * Chuyển chuỗi số nguyên không âm sang int.
     *
     * @param raw Giá trị đầu vào
     * @param defaultValue Giá trị mặc định
     * @param allowZero Có cho phép giá trị 0 hay không
     * @param fieldLabel Nhãn trường dùng trong message
     * @return Giá trị số nguyên hợp lệ
     * @throws AppException Khi giá trị không hợp lệ
     */
    private int parseUnsignedInt(String raw,
                                 int defaultValue,
                                 boolean allowZero,
                                 String fieldLabel) {
        if (raw == null || raw.trim().isEmpty()) {
            return defaultValue;
        }

        String trimmedValue = raw.trim();
        if (!trimmedValue.matches("^[0-9]{1,9}$")) {
            throw new AppException(Constants.ER018, List.of(fieldLabel));
        }

        int parsedValue = Integer.parseInt(trimmedValue);
        if (!allowZero && parsedValue == 0) {
            throw new AppException(Constants.ER018, List.of(fieldLabel));
        }
        return parsedValue;
    }
}
