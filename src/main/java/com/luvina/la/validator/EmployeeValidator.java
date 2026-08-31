/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidator.java, 21/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.luvina.la.constant.Constants;
import com.luvina.la.constant.SortOrder;
import com.luvina.la.exception.AppException;

/**
 * Kiểm tra dữ liệu đầu vào của chức năng danh sách nhân viên.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeValidator {

    private static final int MAX_DEPARTMENT_ID_DIGITS = 18;

    private final CommonValidator commonValidator;

    /**
     * Khởi tạo validator nhân viên với các phép kiểm tra dùng chung.
     *
     * @param commonValidator Validator dùng chung
     */
    public EmployeeValidator(CommonValidator commonValidator) {
        this.commonValidator = commonValidator;
    }

    /**
     * Kiểm tra hướng sắp xếp, chỉ chấp nhận rỗng, ASC hoặc DESC.
     *
     * @param order Hướng sắp xếp
     * @throws AppException Khi hướng sắp xếp không hợp lệ
     */
    public void validateSortOrder(String order) {
        if (!commonValidator.isEmpty(order)) {
            String trimmedOrder = order.trim();
            if (!SortOrder.isSupported(trimmedOrder)) {
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
        return commonValidator.parseUnsignedInt(
                offset,
                Constants.DEFAULT_EMPLOYEE_OFFSET,
                true,
                Constants.FIELD_LABEL_OFFSET
        );
    }

    /**
     * Kiểm tra và chuyển limit sang số nguyên dương.
     *
     * @param limit Limit dạng chuỗi
     * @return Limit hợp lệ
     * @throws AppException Khi limit không phải số nguyên dương
     */
    public int validateAndParseLimit(String limit) {
        return commonValidator.parseUnsignedInt(
                limit,
                Constants.DEFAULT_EMPLOYEE_PAGE_SIZE,
                false,
                Constants.FIELD_LABEL_LIMIT
        );
    }

    /**
     * Chuyển ID phòng ban sang số nguyên dương.
     *
     * @param departmentId ID phòng ban dạng chuỗi
     * @return ID phòng ban hoặc null
     * @throws AppException Khi ID phòng ban không hợp lệ
     */
    public Long parseDepartmentId(String departmentId) {
        if (commonValidator.isEmpty(departmentId)) {
            return null;
        }

        String trimmedDepartmentId = departmentId.trim();
        if (!commonValidator.isHalfWidthNumber(
                trimmedDepartmentId,
                MAX_DEPARTMENT_ID_DIGITS
        )) {
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

        if (commonValidator.isEmpty(employeeName)) {
            return null;
        }

        String escapedEmployeeName = employeeName.trim()
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        return "%" + escapedEmployeeName + "%";
    }

}
