/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidator.java, 21/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

    private static final String FIELD_DEPARTMENT_ID_KEY = "field.departmentId";

    private static final String FIELD_FULLNAME_KEY = "field.fullname";

    private static final String FIELD_OFFSET_KEY = "field.offset";

    private static final String FIELD_LIMIT_KEY = "field.limit";

    private final CommonValidator commonValidator;

    private final MessageSource messageSource;

    /**
     * Khởi tạo validator nhân viên với các phép kiểm tra dùng chung.
     *
     * @param commonValidator Validator dùng chung
     * @param messageSource Nguồn message và nhãn trường
     */
    public EmployeeValidator(
            CommonValidator commonValidator,
            MessageSource messageSource) {
        this.commonValidator = commonValidator;
        this.messageSource = messageSource;
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
                getLabel(FIELD_OFFSET_KEY)
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
                getLabel(FIELD_LIMIT_KEY)
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
                    List.of(getLabel(FIELD_DEPARTMENT_ID_KEY))
            );
        }

        Long parsedDepartmentId = Long.valueOf(trimmedDepartmentId);
        if (parsedDepartmentId == 0L) {
            throw new AppException(
                    Constants.ER018,
                    List.of(getLabel(FIELD_DEPARTMENT_ID_KEY))
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
                    List.of(Constants.EMPLOYEE_NAME_MAX_LENGTH, getLabel(FIELD_FULLNAME_KEY))
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

    /**
     * Lấy nhãn trường từ messages.properties theo locale hiện tại.
     *
     * @param messageKey Khóa nhãn trong messages.properties
     * @return Nhãn trường đã externalize
     */
    private String getLabel(String messageKey) {
        return messageSource.getMessage(
                messageKey,
                null,
                LocaleContextHolder.getLocale()
        );
    }

}
