/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CommonValidatorTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.luvina.la.exception.AppException;

/**
 * Kiểm thử các phép validate dùng chung.
 *
 * @author thanhvinh
 */
class CommonValidatorTest {

    private final CommonValidator commonValidator = new CommonValidator();

    /**
     * Kiểm tra null, chuỗi rỗng và khoảng trắng được nhận diện là rỗng.
     */
    @Test
    void shouldIdentifyEmptyValues() {
        assertTrue(commonValidator.isEmpty(null));
        assertTrue(commonValidator.isEmpty(""));
        assertTrue(commonValidator.isEmpty("   "));
        assertFalse(commonValidator.isEmpty("1"));
    }

    /**
     * Kiểm tra số half-width theo độ dài tối đa.
     */
    @Test
    void shouldValidateHalfWidthNumber() {
        assertTrue(commonValidator.isHalfWidthNumber("123", 3));
        assertFalse(commonValidator.isHalfWidthNumber("1234", 3));
        assertFalse(commonValidator.isHalfWidthNumber("１", 3));
    }

    /**
     * Kiểm tra parse số nguyên và giá trị mặc định.
     */
    @Test
    void shouldParseUnsignedInteger() {
        assertEquals(20, commonValidator.parseUnsignedInt("", 20, false, "limit"));
        assertEquals(12, commonValidator.parseUnsignedInt("12", 20, false, "limit"));
        assertThrows(
                AppException.class,
                () -> commonValidator.parseUnsignedInt("-1", 20, false, "limit")
        );
    }
}
