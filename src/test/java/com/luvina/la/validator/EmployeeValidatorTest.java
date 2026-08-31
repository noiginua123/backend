/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidatorTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.luvina.la.exception.AppException;

/**
 * Kiểm thử EmployeeValidator lấy nhãn từ messages.properties.
 *
 * @author thanhvinh
 */
class EmployeeValidatorTest {

    /**
     * Kiểm tra nhãn departmentId trong tham số lỗi được đọc từ message source.
     */
    @Test
    void shouldResolveFieldLabelFromProperties() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        EmployeeValidator employeeValidator = new EmployeeValidator(
                new CommonValidator(),
                messageSource
        );

        AppException exception = assertThrows(
                AppException.class,
                () -> employeeValidator.parseDepartmentId("invalid")
        );

        assertEquals(
                messageSource.getMessage("field.departmentId", null, Locale.getDefault()),
                exception.getParams().get(0)
        );
    }
}
