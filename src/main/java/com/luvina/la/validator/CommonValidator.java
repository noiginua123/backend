/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CommonValidator.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.luvina.la.constant.Constants;
import com.luvina.la.exception.AppException;

/**
 * Cung cấp các phép kiểm tra chuỗi và số dùng chung cho các validator.
 *
 * @author thanhvinh
 */
@Component
public class CommonValidator {

    private static final int MAX_INTEGER_DIGITS = 9;

    /**
     * Kiểm tra chuỗi null, rỗng hoặc chỉ chứa khoảng trắng.
     *
     * @param value Chuỗi cần kiểm tra
     * @return true nếu chuỗi không có nội dung
     */
    public boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Kiểm tra chuỗi theo biểu thức chính quy.
     *
     * @param value Chuỗi cần kiểm tra
     * @param pattern Biểu thức chính quy
     * @return true nếu chuỗi không null và khớp biểu thức
     */
    public boolean matchesPattern(String value, String pattern) {
        return value != null && value.matches(pattern);
    }

    /**
     * Kiểm tra chuỗi chỉ gồm chữ số half-width với độ dài giới hạn.
     *
     * @param value Chuỗi cần kiểm tra
     * @param maxLength Số chữ số tối đa
     * @return true nếu chuỗi là số half-width hợp lệ
     */
    public boolean isHalfWidthNumber(String value, int maxLength) {
        String pattern = "^[0-9]{1," + maxLength + "}$";
        return matchesPattern(value, pattern);
    }

    /**
     * Chuyển chuỗi số nguyên không âm sang int.
     *
     * @param raw Giá trị đầu vào
     * @param defaultValue Giá trị mặc định khi đầu vào rỗng
     * @param allowZero Có cho phép giá trị 0 hay không
     * @param fieldLabel Nhãn trường dùng trong message
     * @return Giá trị số nguyên hợp lệ
     * @throws AppException Khi giá trị không phải số half-width hợp lệ
     */
    public int parseUnsignedInt(
            String raw,
            int defaultValue,
            boolean allowZero,
            String fieldLabel) {
        if (isEmpty(raw)) {
            return defaultValue;
        }

        String trimmedValue = raw.trim();
        if (!isHalfWidthNumber(trimmedValue, MAX_INTEGER_DIGITS)) {
            throw new AppException(Constants.ER018, List.of(fieldLabel));
        }

        int parsedValue = Integer.parseInt(trimmedValue);
        if (!allowZero && parsedValue == 0) {
            throw new AppException(Constants.ER018, List.of(fieldLabel));
        }
        return parsedValue;
    }
}
