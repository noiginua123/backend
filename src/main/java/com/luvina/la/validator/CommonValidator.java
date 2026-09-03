/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CommonValidator.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    /** Số chữ số tối đa của kiểu số nguyên thông thường. */
    private static final int MAX_INTEGER_DIGITS = 9;

    /** Biểu thức chính quy kiểm tra chuỗi chỉ chứa các chữ số. */
    private static final String REGEX_DIGITS = "^[0-9]+$";

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
     * Kiểm tra chuỗi vượt quá độ dài tối đa (đếm theo code point).
     *
     * @param value Chuỗi cần kiểm tra
     * @param maxLength Độ dài tối đa cho phép
     * @return true nếu vượt quá, false nếu hợp lệ hoặc null
     */
    public boolean isMaxLength(String value, int maxLength) {
        if (value == null) {
            return false;
        }
        return value.codePointCount(0, value.length()) > maxLength;
    }

    /**
     * Kiểm tra độ dài chuỗi nằm trong khoảng [min, max] (đếm theo code point).
     *
     * @param value Chuỗi cần kiểm tra
     * @param minLength Độ dài tối thiểu
     * @param maxLength Độ dài tối đa
     * @return true nếu nằm trong khoảng, false nếu null hoặc ngoài khoảng
     */
    public boolean isLengthInRange(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }
        int length = value.codePointCount(0, value.length());
        return length >= minLength && length <= maxLength;
    }

    /**
     * Kiểm tra chuỗi chỉ gồm ký tự katakana (chấp nhận cả dấu cách).
     *
     * @param value Chuỗi cần kiểm tra
     * @return true nếu hợp lệ
     */
    public boolean isKatakana(String value) {
        return matchesPattern(value, Constants.REGEX_KATAKANA);
    }

    /**
     * Kiểm tra chuỗi chỉ gồm ký tự 1 byte (half-size).
     *
     * @param value Chuỗi cần kiểm tra
     * @return true nếu hợp lệ
     */
    public boolean isHalfSize(String value) {
        return matchesPattern(value, Constants.REGEX_HALF_SIZE);
    }

    /**
     * Kiểm tra định dạng Login ID (a-z, A-Z, 0-9, _; ký tự đầu không phải số).
     *
     * @param value Chuỗi cần kiểm tra
     * @return true nếu hợp lệ
     */
    public boolean isValidLoginId(String value) {
        return matchesPattern(value, Constants.REGEX_LOGIN_ID);
    }

    /**
     * Kiểm tra định dạng email: có @ và dấu chấm, không đứng đầu, không đặt cạnh nhau.
     *
     * @param value Địa chỉ email
     * @return true nếu hợp lệ
     */
    public boolean isValidEmail(String value) {
        if (isEmpty(value)) {
            return false;
        }
        if (!value.contains("@") || !value.contains(".")) {
            return false;
        }
        if (value.startsWith("@") || value.startsWith(".")) {
            return false;
        }
        return !value.contains("@.") && !value.contains(".@");
    }

    /**
     * Kiểm tra chuỗi là ngày hợp lệ theo định dạng yyyy/MM/dd (không lenient).
     *
     * @param value Chuỗi ngày tháng
     * @return true nếu đúng định dạng và là ngày có thực
     */
    public boolean isValidDate(String value) {
        if (isEmpty(value)) {
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        formatter.setLenient(false);
        try {
            formatter.parse(value.trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Kiểm tra ngày hết hạn có trước ngày cấp hay không.
     *
     * @param startDate Ngày cấp (yyyy/MM/dd)
     * @param endDate Ngày hết hạn (yyyy/MM/dd)
     * @return true nếu endDate trước startDate
     */
    public boolean isEndDateBeforeStartDate(String startDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        formatter.setLenient(false);
        try {
            Date start = formatter.parse(startDate.trim());
            Date end = formatter.parse(endDate.trim());
            return end.before(start);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Kiểm tra chuỗi là số nguyên dương (half-width, > 0).
     *
     * @param value Chuỗi cần kiểm tra
     * @return true nếu là số nguyên dương
     */
    public boolean isPositiveNumber(String value) {
        if (isEmpty(value)) {
            return false;
        }
        String trimmed = value.trim();
        if (!matchesPattern(trimmed, REGEX_DIGITS)) {
            return false;
        }
        try {
            return Long.parseLong(trimmed) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Chuyển chuỗi số nguyên không âm sang int.
     *
     * @param raw Giá trị đầu vào
     * @param defaultValue Giá trị mặc định khi đầu vào rỗng
     * @param allowZero Có cho phép giá trị 0 hay không
     * @param fieldLabel Nhãn trường dùng trong thông báo lỗi
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
