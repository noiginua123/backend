/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidatorTest.java, 31/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.MessageDTO;
import com.luvina.la.exception.AppException;
import com.luvina.la.payload.request.CertificationRequest;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeRepository;

/**
 * Kiểm thử EmployeeValidator: kiểm tra nhãn trường và logic validate thêm mới nhân viên.
 *
 * @author thanhvinh
 */
class EmployeeValidatorTest {

    private ResourceBundleMessageSource messageSource;
    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    private CertificationRepository certificationRepository;
    private EmployeeValidator employeeValidator;

    /**
     * Khởi tạo mock repository và message source trước mỗi test case.
     */
    @BeforeEach
    void setUp() {
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        employeeRepository = mock(EmployeeRepository.class);
        departmentRepository = mock(DepartmentRepository.class);
        certificationRepository = mock(CertificationRepository.class);

        employeeValidator = new EmployeeValidator(
                new CommonValidator(),
                messageSource,
                employeeRepository,
                departmentRepository,
                certificationRepository
        );

        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(certificationRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.existsByEmployeeLoginId("existing_user")).thenReturn(true);
    }

    /**
     * Kiểm tra nhãn departmentId trong tham số lỗi được đọc từ message source.
     */
    @Test
    void shouldResolveFieldLabelFromProperties() {
        AppException exception = assertThrows(
                AppException.class,
                () -> employeeValidator.parseDepartmentId("invalid")
        );

        assertEquals(
                messageSource.getMessage("field.departmentId", null, Locale.getDefault()),
                exception.getParams().get(0)
        );
    }

    /**
     * Kiểm tra khi account đã tồn tại thì trả về MessageDTO với mã ER003.
     */
    @Test
    void shouldReturnER003WhenDuplicateLoginId() {
        EmployeeRequest request = createValidRequest();
        request.setEmployeeLoginId("existing_user");

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER003, result.getCode());
        assertEquals(
                messageSource.getMessage("field.loginId", null, Locale.getDefault()),
                result.getParams().get(0)
        );
    }

    /**
     * Kiểm tra login ID được cắt khoảng trắng trước khi truy vấn trùng lặp.
     */
    @Test
    void shouldReturnER003WhenTrimmedLoginIdIsDuplicate() {
        EmployeeRequest request = createValidRequest();
        request.setEmployeeLoginId(" existing_user ");

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER003, result.getCode());
    }

    /**
     * Kiểm tra khi ngày sinh trống thì trả về MessageDTO với mã ER002.
     */
    @Test
    void shouldReturnER002WhenBirthDateIsEmpty() {
        EmployeeRequest request = createValidRequest();
        request.setEmployeeBirthDate("");

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER002, result.getCode());
    }

    /**
     * Kiểm tra khi ngày sinh không hợp lệ thì trả về MessageDTO với mã ER011.
     */
    @Test
    void shouldReturnER011WhenBirthDateIsInvalid() {
        EmployeeRequest request = createValidRequest();
        request.setEmployeeBirthDate("2024/02/30");

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER011, result.getCode());
    }

    /**
     * Kiểm tra khi ngày cấp chứng chỉ trống thì trả về MessageDTO với mã ER002.
     */
    @Test
    void shouldReturnER002WhenCertificationStartDateIsEmpty() {
        EmployeeRequest request = createValidRequest();
        CertificationRequest cert = createValidCertification();
        cert.setStartDate("");
        request.setCertifications(List.of(cert));

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER002, result.getCode());
    }

    /**
     * Kiểm tra khi ngày cấp chứng chỉ không hợp lệ thì trả về MessageDTO với mã ER011.
     */
    @Test
    void shouldReturnER011WhenCertificationStartDateIsInvalid() {
        EmployeeRequest request = createValidRequest();
        CertificationRequest cert = createValidCertification();
        cert.setStartDate("2023/13/01");
        request.setCertifications(List.of(cert));

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER011, result.getCode());
    }

    /**
     * Kiểm tra khi ngày hết hạn chứng chỉ trống thì trả về MessageDTO với mã ER002.
     */
    @Test
    void shouldReturnER002WhenCertificationEndDateIsEmpty() {
        EmployeeRequest request = createValidRequest();
        CertificationRequest cert = createValidCertification();
        cert.setEndDate("");
        request.setCertifications(List.of(cert));

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER002, result.getCode());
    }

    /**
     * Kiểm tra khi ngày hết hạn chứng chỉ không hợp lệ thì trả về MessageDTO với mã ER011.
     */
    @Test
    void shouldReturnER011WhenCertificationEndDateIsInvalid() {
        EmployeeRequest request = createValidRequest();
        CertificationRequest cert = createValidCertification();
        cert.setEndDate("invalid_date");
        request.setCertifications(List.of(cert));

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER011, result.getCode());
    }

    /**
     * Kiểm tra khi điểm số không phải số dương thì trả về MessageDTO với mã ER018.
     */
    @Test
    void shouldReturnER018WhenCertificationScoreIsNotPositive() {
        EmployeeRequest request = createValidRequest();
        CertificationRequest cert = createValidCertification();
        cert.setScore("abc");
        request.setCertifications(List.of(cert));

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNotNull(result);
        assertEquals(Constants.ER018, result.getCode());
    }

    /**
     * Kiểm tra khi dữ liệu hợp lệ thì validate thành công và trả về null.
     */
    @Test
    void shouldPassWhenValidRequest() {
        EmployeeRequest request = createValidRequest();
        request.setCertifications(List.of(createValidCertification()));

        MessageDTO result = employeeValidator.validateAddEditEmployee(request);

        assertNull(result);
    }

    /**
     * Tạo đối tượng EmployeeRequest với các dữ liệu hợp lệ cho test.
     *
     * @return EmployeeRequest hợp lệ
     */
    private EmployeeRequest createValidRequest() {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeLoginId("new_user");
        request.setDepartmentId("1");
        request.setEmployeeName("Nguyen Van A");
        request.setEmployeeNameKana("ｱｲｳｴｵ");
        request.setEmployeeBirthDate("2000/01/01");
        request.setEmployeeEmail("test@example.com");
        request.setEmployeeTelephone("0123456789");
        request.setEmployeeLoginPassword("password123");
        return request;
    }

    /**
     * Tạo đối tượng CertificationRequest hợp lệ cho test.
     *
     * @return CertificationRequest hợp lệ
     */
    private CertificationRequest createValidCertification() {
        CertificationRequest cert = new CertificationRequest();
        cert.setCertificationId("1");
        cert.setStartDate("2023/01/01");
        cert.setEndDate("2024/01/01");
        cert.setScore("900");
        return cert;
    }
}
