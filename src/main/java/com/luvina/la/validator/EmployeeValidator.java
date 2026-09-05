/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidator.java, 21/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.luvina.la.constant.Constants;
import com.luvina.la.constant.SortOrder;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.exception.AppException;
import com.luvina.la.payload.request.CertificationRequest;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeRepository;

/**
 * Kiểm tra dữ liệu đầu vào của chức năng danh sách (ADM002) và thêm mới (ADM004) nhân viên.
 *
 * @author thanhvinh
 */
@Component
public class EmployeeValidator {

    /** Số chữ số tối đa của ID phòng ban. */
    private static final int MAX_DEPARTMENT_ID_DIGITS = 18;

    /** Khóa nhãn trường phòng ban trong messages.properties. */
    private static final String FIELD_DEPARTMENT_ID_KEY = "field.departmentId";

    /** Khóa nhãn trường họ tên trong messages.properties. */
    private static final String FIELD_FULLNAME_KEY = "field.fullname";

    /** Khóa nhãn trường vị trí bắt đầu (offset) trong messages.properties. */
    private static final String FIELD_OFFSET_KEY = "field.offset";

    /** Khóa nhãn trường số bản ghi mỗi trang (limit) trong messages.properties. */
    private static final String FIELD_LIMIT_KEY = "field.limit";

    /** Khóa nhãn trường tên đăng nhập trong messages.properties. */
    private static final String FIELD_LOGIN_ID_KEY = "field.loginId";

    /** Khóa nhãn trường nhóm/phòng ban trong messages.properties. */
    private static final String FIELD_GROUP_KEY = "field.group";

    /** Khóa nhãn trường họ tên Kana trong messages.properties. */
    private static final String FIELD_FULLNAME_KANA_KEY = "field.fullnameKana";

    /** Khóa nhãn trường ngày sinh trong messages.properties. */
    private static final String FIELD_BIRTH_DATE_KEY = "field.birthDate";

    /** Khóa nhãn trường email trong messages.properties. */
    private static final String FIELD_EMAIL_KEY = "field.email";

    /** Khóa nhãn trường số điện thoại trong messages.properties. */
    private static final String FIELD_TELEPHONE_KEY = "field.telephone";

    /** Khóa nhãn trường mật khẩu trong messages.properties. */
    private static final String FIELD_PASSWORD_KEY = "field.password";

    /** Khóa nhãn trường chứng chỉ trong messages.properties. */
    private static final String FIELD_CERTIFICATION_KEY = "field.certification";

    /** Khóa nhãn trường ngày cấp chứng chỉ trong messages.properties. */
    private static final String FIELD_START_DATE_KEY = "field.startDate";

    /** Khóa nhãn trường ngày hết hạn chứng chỉ trong messages.properties. */
    private static final String FIELD_END_DATE_KEY = "field.endDate";

    /** Khóa nhãn trường điểm số chứng chỉ trong messages.properties. */
    private static final String FIELD_SCORE_KEY = "field.score";

    /** Token định dạng email phục vụ hiển thị thông báo lỗi định dạng. */
    private static final String EMAIL_FORMAT_TOKEN = "email";

    /** Validator dùng chung các thao tác kiểm tra chuỗi và số. */
    private final CommonValidator commonValidator;

    /** Nguồn nạp thông điệp và nhãn trường đa ngôn ngữ. */
    private final MessageSource messageSource;

    /** Repository nhân viên phục vụ kiểm tra trùng lặp mã đăng nhập. */
    private final EmployeeRepository employeeRepository;

    /** Repository phòng ban phục vụ kiểm tra tồn tại phòng ban. */
    private final DepartmentRepository departmentRepository;

    /** Repository chứng chỉ phục vụ kiểm tra tồn tại chứng chỉ. */
    private final CertificationRepository certificationRepository;

    /**
     * Khởi tạo validator nhân viên.
     *
     * @param commonValidator Validator dùng chung
     * @param messageSource Nguồn message và nhãn trường
     * @param employeeRepository Repository nhân viên (kiểm tra trùng login id)
     * @param departmentRepository Repository phòng ban (kiểm tra tồn tại)
     * @param certificationRepository Repository chứng chỉ (kiểm tra tồn tại)
     */
    public EmployeeValidator(
            CommonValidator commonValidator,
            MessageSource messageSource,
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            CertificationRepository certificationRepository) {
        this.commonValidator = commonValidator;
        this.messageSource = messageSource;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.certificationRepository = certificationRepository;
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
     * Kiểm tra toàn bộ dữ liệu thêm mới nhân viên (ADM004).
     *
     * @param request Dữ liệu nhân viên
     * @throws AppException Khi có bất kỳ trường nào không hợp lệ
     */
    public void validateForCreate(EmployeeRequest request) {
        validateLoginId(request.getEmployeeLoginId());
        validateDepartment(request.getDepartmentId());
        validateFullName(request.getEmployeeName());
        validateNameKana(request.getEmployeeNameKana());
        validateBirthDate(request.getEmployeeBirthDate());
        validateEmail(request.getEmployeeEmail());
        validateTelephone(request.getEmployeeTelephone());
        validatePassword(request.getEmployeeLoginPassword());
        validateCertifications(request.getCertifications());
    }

    /**
     * Kiểm tra login id: bắt buộc, tối đa 50, đúng định dạng, chưa tồn tại.
     *
     * @param loginId Login id
     */
    private void validateLoginId(String loginId) {
        String label = getLabel(FIELD_LOGIN_ID_KEY);
        if (commonValidator.isEmpty(loginId)) {
            throw new AppException(Constants.ER001, List.of(label));
        }
        String value = loginId.trim();
        if (commonValidator.isMaxLength(value, Constants.MAX_LENGTH_50)) {
            throw new AppException(Constants.ER006, List.of(Constants.MAX_LENGTH_50, label));
        }
        if (!commonValidator.isValidLoginId(value)) {
            throw new AppException(Constants.ER019);
        }
        Optional<EmployeeEntity> existing = employeeRepository.findByEmployeeLoginId(value);
        if (existing.isPresent()) {
            throw new AppException(Constants.ER003, List.of(label));
        }
    }

    /**
     * Kiểm tra phòng ban: bắt buộc chọn và phải tồn tại.
     *
     * @param departmentId ID phòng ban dạng chuỗi
     */
    private void validateDepartment(String departmentId) {
        String label = getLabel(FIELD_GROUP_KEY);
        if (commonValidator.isEmpty(departmentId)) {
            throw new AppException(Constants.ER002, List.of(label));
        }
        Long id;
        try {
            id = Long.valueOf(departmentId.trim());
        } catch (NumberFormatException e) {
            throw new AppException(Constants.ER004, List.of(label));
        }
        if (!departmentRepository.existsById(id)) {
            throw new AppException(Constants.ER004, List.of(label));
        }
    }

    /**
     * Kiểm tra họ tên: bắt buộc, tối đa 125 ký tự.
     *
     * @param fullName Họ tên
     */
    private void validateFullName(String fullName) {
        String label = getLabel(FIELD_FULLNAME_KEY);
        if (commonValidator.isEmpty(fullName)) {
            throw new AppException(Constants.ER001, List.of(label));
        }
        if (commonValidator.isMaxLength(fullName.trim(), Constants.MAX_LENGTH_125)) {
            throw new AppException(Constants.ER006, List.of(Constants.MAX_LENGTH_125, label));
        }
    }

    /**
     * Kiểm tra tên kana: bắt buộc, tối đa 125, đúng katakana.
     *
     * @param nameKana Tên kana
     */
    private void validateNameKana(String nameKana) {
        String label = getLabel(FIELD_FULLNAME_KANA_KEY);
        if (commonValidator.isEmpty(nameKana)) {
            throw new AppException(Constants.ER001, List.of(label));
        }
        String value = nameKana.trim();
        if (commonValidator.isMaxLength(value, Constants.MAX_LENGTH_125)) {
            throw new AppException(Constants.ER006, List.of(Constants.MAX_LENGTH_125, label));
        }
        if (!commonValidator.isKatakana(value)) {
            throw new AppException(Constants.ER009, List.of(label));
        }
    }

    /**
     * Kiểm tra ngày sinh: bắt buộc chọn, đúng định dạng yyyy/MM/dd và ngày hợp lệ.
     *
     * @param birthDate Ngày sinh
     */
    private void validateBirthDate(String birthDate) {
        String label = getLabel(FIELD_BIRTH_DATE_KEY);
        if (commonValidator.isEmpty(birthDate)) {
            throw new AppException(Constants.ER002, List.of(label));
        }
        if (!commonValidator.isValidDate(birthDate.trim())) {
            throw new AppException(Constants.ER011, List.of(label));
        }
    }

    /**
     * Kiểm tra email: bắt buộc, tối đa 125, đúng định dạng email.
     *
     * @param email Email
     */
    private void validateEmail(String email) {
        String label = getLabel(FIELD_EMAIL_KEY);
        if (commonValidator.isEmpty(email)) {
            throw new AppException(Constants.ER001, List.of(label));
        }
        String value = email.trim();
        if (commonValidator.isMaxLength(value, Constants.MAX_LENGTH_125)) {
            throw new AppException(Constants.ER006, List.of(Constants.MAX_LENGTH_125, label));
        }
        if (!commonValidator.isValidEmail(value)) {
            throw new AppException(Constants.ER005, List.of(label, EMAIL_FORMAT_TOKEN));
        }
    }

    /**
     * Kiểm tra số điện thoại: bắt buộc, tối đa 50, chỉ ký tự 1 byte.
     *
     * @param telephone Số điện thoại
     */
    private void validateTelephone(String telephone) {
        String label = getLabel(FIELD_TELEPHONE_KEY);
        if (commonValidator.isEmpty(telephone)) {
            throw new AppException(Constants.ER001, List.of(label));
        }
        String value = telephone.trim();
        if (commonValidator.isMaxLength(value, Constants.MAX_LENGTH_50)) {
            throw new AppException(Constants.ER006, List.of(Constants.MAX_LENGTH_50, label));
        }
        if (!commonValidator.isHalfSize(value)) {
            throw new AppException(Constants.ER008, List.of(label));
        }
    }

    /**
     * Kiểm tra mật khẩu: bắt buộc, độ dài 8 - 50.
     *
     * @param password Mật khẩu
     */
    private void validatePassword(String password) {
        String label = getLabel(FIELD_PASSWORD_KEY);
        if (commonValidator.isEmpty(password)) {
            throw new AppException(Constants.ER001, List.of(label));
        }
        if (!commonValidator.isLengthInRange(password, Constants.PASSWORD_MIN_LENGTH, Constants.PASSWORD_MAX_LENGTH)) {
            throw new AppException(
                    Constants.ER007,
                    List.of(label, Constants.PASSWORD_MIN_LENGTH, Constants.PASSWORD_MAX_LENGTH)
            );
        }
    }

    /**
     * Kiểm tra danh sách chứng chỉ (0 hoặc 1 phần tử ở ADM004).
     *
     * @param certifications Danh sách chứng chỉ
     */
    private void validateCertifications(List<CertificationRequest> certifications) {
        if (certifications == null || certifications.isEmpty()) {
            return;
        }
        for (CertificationRequest certification : certifications) {
            validateCertification(certification);
        }
    }

    /**
     * Kiểm tra một chứng chỉ: bỏ qua nếu chưa chọn; nếu đã chọn thì kiểm tra đầy đủ.
     *
     * @param certification Thông tin chứng chỉ
     */
    private void validateCertification(CertificationRequest certification) {
        if (certification == null || commonValidator.isEmpty(certification.getCertificationId())) {
            return;
        }
        String certLabel = getLabel(FIELD_CERTIFICATION_KEY);
        String startLabel = getLabel(FIELD_START_DATE_KEY);
        String endLabel = getLabel(FIELD_END_DATE_KEY);
        String scoreLabel = getLabel(FIELD_SCORE_KEY);

        Long certificationId;
        try {
            certificationId = Long.valueOf(certification.getCertificationId().trim());
        } catch (NumberFormatException e) {
            throw new AppException(Constants.ER004, List.of(certLabel));
        }
        if (!certificationRepository.existsById(certificationId)) {
            throw new AppException(Constants.ER004, List.of(certLabel));
        }

        if (commonValidator.isEmpty(certification.getStartDate())) {
            throw new AppException(Constants.ER002, List.of(startLabel));
        }
        if (!commonValidator.isValidDate(certification.getStartDate().trim())) {
            throw new AppException(Constants.ER011, List.of(startLabel));
        }

        if (commonValidator.isEmpty(certification.getEndDate())) {
            throw new AppException(Constants.ER002, List.of(endLabel));
        }
        if (!commonValidator.isValidDate(certification.getEndDate().trim())) {
            throw new AppException(Constants.ER011, List.of(endLabel));
        }
        if (commonValidator.isEndDateBeforeStartDate(
                certification.getStartDate().trim(),
                certification.getEndDate().trim())) {
            throw new AppException(Constants.ER012);
        }

        if (commonValidator.isEmpty(certification.getScore())) {
            throw new AppException(Constants.ER001, List.of(scoreLabel));
        }
        String score = certification.getScore().trim();
        if (!commonValidator.isPositiveNumber(score)) {
            throw new AppException(Constants.ER018, List.of(scoreLabel));
        }
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
