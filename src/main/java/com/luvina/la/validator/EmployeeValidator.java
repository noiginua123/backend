/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidator.java, 21/08/2026 thanhvinh
 */
package com.luvina.la.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.luvina.la.constant.Constants;
import com.luvina.la.constant.SortOrder;
import com.luvina.la.dto.MessageDTO;
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

    /** Khóa nhãn trường ID trong messages.properties. */
    private static final String FIELD_ID_KEY = "field.id";

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
     * Kiểm tra toàn bộ dữ liệu thêm mới (ADM004) hoặc chỉnh sửa nhân viên (mặc định thêm mới).
     *
     * @param request Dữ liệu nhân viên
     * @return MessageDTO chứa thông tin lỗi nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateAddEditEmployee(EmployeeRequest request) {
        return validateAddEditEmployee(request, false);
    }

    /**
     * Kiểm tra toàn bộ dữ liệu thêm mới (ADM004) hoặc chỉnh sửa nhân viên.
     *
     * @param request Dữ liệu nhân viên
     * @param isEdit  true nếu là thao tác chỉnh sửa (Edit), false nếu là thêm mới (Add)
     * @return MessageDTO chứa thông tin lỗi nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateAddEditEmployee(EmployeeRequest request, boolean isEdit) {
        MessageDTO messageDto = null;
        Long employeeId = null;

        // Nếu là luồng Edit, bắt buộc kiểm tra employeeId trước tiên
        if (isEdit) {
            messageDto = validateEmployeeId(request.getEmployeeId());
            if (messageDto != null) {
                return messageDto;
            }
            employeeId = request.getEmployeeIdAsLong();
        }

        if (messageDto == null) {
            messageDto = validateLoginId(request.getEmployeeLoginId(), isEdit, employeeId);
        }
        if (messageDto == null) {
            messageDto = validateDepartment(request.getDepartmentId());
        }
        if (messageDto == null) {
            messageDto = validateFullName(request.getEmployeeName());
        }
        if (messageDto == null) {
            messageDto = validateNameKana(request.getEmployeeNameKana());
        }
        if (messageDto == null) {
            messageDto = validateBirthDate(request.getEmployeeBirthDate());
        }
        if (messageDto == null) {
            messageDto = validateEmail(request.getEmployeeEmail());
        }
        if (messageDto == null) {
            messageDto = validateTelephone(request.getEmployeeTelephone());
        }
        if (messageDto == null) {
            messageDto = validatePassword(request.getEmployeeLoginPassword(), isEdit);
        }
        if (messageDto == null) {
            messageDto = validateCertifications(request.getCertifications());
        }

        return messageDto;
    }

    /**
     * Kiểm tra tính hợp lệ của employeeId cho chức năng chỉnh sửa nhân viên (ADM004).
     *
     * @param employeeId ID nhân viên dạng chuỗi
     * @return đối tượng MessageDTO nếu có lỗi (ER001 nếu rỗng, ER013 nếu không tồn tại), ngược lại trả về null
     */
    private MessageDTO validateEmployeeId(String employeeId) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_ID_KEY);
        if (commonValidator.isEmpty(employeeId)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (!isPositiveLong(employeeId)
                || !employeeRepository.existsById(Long.valueOf(employeeId.trim()))) {
            messageDto = buildMessage(Constants.ER013, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra login id: bắt buộc, tối đa 50, đúng định dạng, chưa tồn tại (loại trừ chính mình nếu đang edit).
     *
     * @param loginId    Login id
     * @param isEdit     Cờ xác định chế độ edit
     * @param employeeId ID nhân viên hiện tại nếu đang edit
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateLoginId(String loginId, boolean isEdit, Long employeeId) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_LOGIN_ID_KEY);
        if (commonValidator.isEmpty(loginId)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(loginId.trim(), Constants.MAX_LENGTH_50)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_50, label);
        } else if (!commonValidator.isValidLoginId(loginId.trim())) {
            messageDto = buildMessage(Constants.ER019);
        } else if (isEdit && employeeId != null) {
            if (employeeRepository.existsByEmployeeLoginIdAndEmployeeIdNot(loginId.trim(), employeeId)) {
                messageDto = buildMessage(Constants.ER003, label);
            }
        } else if (employeeRepository.existsByEmployeeLoginId(loginId.trim())) {
            messageDto = buildMessage(Constants.ER003, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra phòng ban: bắt buộc chọn, số nguyên dương và phải tồn tại.
     *
     * @param departmentId ID phòng ban dạng chuỗi
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateDepartment(String departmentId) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_GROUP_KEY);
        if (commonValidator.isEmpty(departmentId)) {
            messageDto = buildMessage(Constants.ER002, label);
        } else if (!isPositiveLong(departmentId)) {
            messageDto = buildMessage(Constants.ER018, label);
        } else if (!isExistingDepartment(departmentId)) {
            messageDto = buildMessage(Constants.ER004, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra họ tên: bắt buộc, tối đa 125 ký tự.
     *
     * @param fullName Họ tên
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateFullName(String fullName) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_FULLNAME_KEY);
        if (commonValidator.isEmpty(fullName)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(fullName.trim(), Constants.MAX_LENGTH_125)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_125, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra tên kana: bắt buộc, tối đa 125, đúng katakana.
     *
     * @param nameKana Tên kana
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateNameKana(String nameKana) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_FULLNAME_KANA_KEY);
        if (commonValidator.isEmpty(nameKana)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(nameKana.trim(), Constants.MAX_LENGTH_125)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_125, label);
        } else if (!commonValidator.isKatakana(nameKana.trim())) {
            messageDto = buildMessage(Constants.ER009, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra ngày sinh: bắt buộc chọn, đúng định dạng yyyy/MM/dd và ngày hợp lệ.
     *
     * @param birthDate Ngày sinh
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateBirthDate(String birthDate) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_BIRTH_DATE_KEY);
        if (commonValidator.isEmpty(birthDate)) {
            messageDto = buildMessage(Constants.ER002, label);
        } else if (!commonValidator.isValidDate(birthDate.trim())) {
            messageDto = buildMessage(Constants.ER011, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra email: bắt buộc, tối đa 125, đúng định dạng email.
     *
     * @param email Email
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateEmail(String email) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_EMAIL_KEY);
        if (commonValidator.isEmpty(email)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(email.trim(), Constants.MAX_LENGTH_125)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_125, label);
        } else if (!commonValidator.isValidEmail(email.trim())) {
            messageDto = buildMessage(Constants.ER005, label, EMAIL_FORMAT_TOKEN);
        }
        return messageDto;
    }

    /**
     * Kiểm tra số điện thoại: bắt buộc, tối đa 50, chỉ ký tự 1 byte.
     *
     * @param telephone Số điện thoại
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateTelephone(String telephone) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_TELEPHONE_KEY);
        if (commonValidator.isEmpty(telephone)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(telephone.trim(), Constants.MAX_LENGTH_50)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_50, label);
        } else if (!commonValidator.isHalfSize(telephone.trim())) {
            messageDto = buildMessage(Constants.ER008, label);
        }
        return messageDto;
    }

    /**
     * Kiểm tra mật khẩu: bắt buộc (với Add), độ dài 8 - 50. Với Edit cho phép để trống.
     *
     * @param password Mật khẩu
     * @param isEdit   true nếu là luồng chỉnh sửa (cho phép để trống để giữ mật khẩu cũ)
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validatePassword(String password, boolean isEdit) {
        MessageDTO messageDto = null;
        String label = getLabel(FIELD_PASSWORD_KEY);
        if (commonValidator.isEmpty(password)) {
            if (!isEdit) {
                messageDto = buildMessage(Constants.ER001, label);
            }
        } else if (!commonValidator.isLengthInRange(password, Constants.PASSWORD_MIN_LENGTH, Constants.PASSWORD_MAX_LENGTH)) {
            messageDto = buildMessage(
                    Constants.ER007,
                    label,
                    Constants.PASSWORD_MIN_LENGTH,
                    Constants.PASSWORD_MAX_LENGTH
            );
        }
        return messageDto;
    }

    /**
     * Kiểm tra danh sách chứng chỉ (0 hoặc 1 phần tử ở ADM004).
     *
     * @param certifications Danh sách chứng chỉ
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateCertifications(List<CertificationRequest> certifications) {
        MessageDTO messageDto = null;
        if (certifications != null && !certifications.isEmpty()) {
            for (CertificationRequest certification : certifications) {
                messageDto = validateCertification(certification);
                if (messageDto != null) {
                    break;
                }
            }
        }
        return messageDto;
    }

    /**
     * Kiểm tra một chứng chỉ: bỏ qua nếu chưa chọn; nếu đã chọn thì kiểm tra đầy đủ.
     *
     * @param certification Thông tin chứng chỉ
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateCertification(CertificationRequest certification) {
        if (certification == null || commonValidator.isEmpty(certification.getCertificationId())) {
            return null;
        }

        MessageDTO messageDto = validateCertificationId(certification.getCertificationId());
        if (messageDto == null) {
            messageDto = validateStartDate(certification.getStartDate());
        }
        if (messageDto == null) {
            messageDto = validateEndDate(certification.getStartDate(), certification.getEndDate());
        }
        if (messageDto == null) {
            messageDto = validateScore(certification.getScore());
        }
        return messageDto;
    }

    /**
     * Kiểm tra ID chứng chỉ: bắt buộc chọn, số nguyên dương, tồn tại trong DB.
     *
     * @param certificationId ID chứng chỉ cần kiểm tra
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateCertificationId(String certificationId) {
        MessageDTO messageDto = null;
        String certLabel = getLabel(FIELD_CERTIFICATION_KEY);

        if (commonValidator.isEmpty(certificationId)) {
            messageDto = buildMessage(Constants.ER001, certLabel);
        } else if (!isPositiveLong(certificationId)) {
            messageDto = buildMessage(Constants.ER018, certLabel);
        } else if (!certificationRepository.existsById(Long.valueOf(certificationId.trim()))) {
            messageDto = buildMessage(Constants.ER004, certLabel);
        }
        return messageDto;
    }

    /**
     * Kiểm tra ngày cấp chứng chỉ: bắt buộc chọn, đúng định dạng yyyy/MM/dd và hợp lệ.
     *
     * @param startDate Ngày cấp chứng chỉ
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateStartDate(String startDate) {
        MessageDTO messageDto = null;
        String startLabel = getLabel(FIELD_START_DATE_KEY);

        if (commonValidator.isEmpty(startDate)) {
            messageDto = buildMessage(Constants.ER002, startLabel);
        } else if (!commonValidator.isValidDate(startDate.trim())) {
            messageDto = buildMessage(Constants.ER011, startLabel);
        }
        return messageDto;
    }

    /**
     * Kiểm tra ngày hết hạn chứng chỉ: bắt buộc chọn, đúng định dạng yyyy/MM/dd, hợp lệ và sau ngày cấp.
     *
     * @param startDate Ngày cấp chứng chỉ
     * @param endDate Ngày hết hạn chứng chỉ
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateEndDate(String startDate, String endDate) {
        MessageDTO messageDto = null;
        String endLabel = getLabel(FIELD_END_DATE_KEY);

        if (commonValidator.isEmpty(endDate)) {
            messageDto = buildMessage(Constants.ER002, endLabel);
        } else if (!commonValidator.isValidDate(endDate.trim())) {
            messageDto = buildMessage(Constants.ER011, endLabel);
        } else if (commonValidator.isValidDate(startDate != null ? startDate.trim() : "")
                && commonValidator.isEndDateBeforeStartDate(startDate.trim(), endDate.trim())) {
            messageDto = buildMessage(Constants.ER012);
        }
        return messageDto;
    }

    /**
     * Kiểm tra điểm số chứng chỉ: bắt buộc, số dương.
     *
     * @param score Điểm số chứng chỉ
     * @return đối tượng MessageDTO nếu có lỗi, ngược lại trả về null
     */
    private MessageDTO validateScore(String score) {
        MessageDTO messageDto = null;
        String scoreLabel = getLabel(FIELD_SCORE_KEY);

        if (commonValidator.isEmpty(score)) {
            messageDto = buildMessage(Constants.ER001, scoreLabel);
        } else if (!commonValidator.isPositiveNumber(score.trim())) {
            messageDto = buildMessage(Constants.ER018, scoreLabel);
        }
        return messageDto;
    }

    /**
     * Kiểm tra tính hợp lệ của employeeId cho chức năng lấy chi tiết nhân viên (ADM003 / ADM004).
     *
     * @param employeeId ID của nhân viên cần lấy chi tiết
     * @throws AppException ER001 khi employeeId null, ER013 khi nhân viên không tồn tại
     */
    public void validateGetEmployeeDetail(Long employeeId) {
        MessageDTO messageDto = null;
        String idLabel = getLabel(FIELD_ID_KEY);
        if (employeeId == null) {
            messageDto = buildMessage(Constants.ER001, idLabel);
        } else if (!employeeRepository.existsById(employeeId)) {
            messageDto = buildMessage(Constants.ER013, idLabel);
        }
        if (messageDto != null) {
            throw new AppException(messageDto.getCode(), messageDto.getParams());
        }
    }

    /**
     * Tạo đối tượng MessageDTO từ mã lỗi và danh sách tham số.
     *
     * @param code Mã lỗi (ví dụ: ER001, ER006)
     * @param params Danh sách tham số thay thế trong thông báo lỗi
     * @return Đối tượng MessageDTO chứa mã lỗi và tham số
     */
    private MessageDTO buildMessage(String code, Object... params) {
        List<Object> paramList = new ArrayList<>();
        if (params != null) {
            for (Object param : params) {
                if (param != null) {
                    paramList.add(param);
                }
            }
        }
        return new MessageDTO(code, paramList);
    }

    /**
     * Kiểm tra một chuỗi có phải là số nguyên dương hợp lệ (dạng bán giác).
     *
     * @param value Chuỗi cần kiểm tra
     * @return true nếu là số nguyên dương hợp lệ, false nếu không
     */
    private boolean isPositiveLong(String value) {
        if (commonValidator.isEmpty(value)) {
            return false;
        }
        String trimmed = value.trim();
        if (!commonValidator.isHalfWidthNumber(trimmed, MAX_DEPARTMENT_ID_DIGITS)) {
            return false;
        }
        try {
            long number = Long.parseLong(trimmed);
            return number > 0L;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra phòng ban có tồn tại trong cơ sở dữ liệu hay không.
     *
     * @param departmentId ID phòng ban dạng chuỗi
     * @return true nếu phòng ban tồn tại, ngược lại false
     */
    private boolean isExistingDepartment(String departmentId) {
        try {
            Long id = Long.valueOf(departmentId.trim());
            return departmentRepository.existsById(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra tính hợp lệ của employeeId cho chức năng xóa nhân viên (ADM003).
     *
     * @param employeeId ID của nhân viên cần xóa
     * @return EmployeeEntity nếu thông tin hợp lệ
     * @throws AppException ER001 khi employeeId null, ER014 khi không tìm thấy, ER020 khi xóa tài khoản admin
     */
    public EmployeeEntity validateDeleteEmployee(Long employeeId) {
        String idLabel = getLabel(FIELD_ID_KEY);
        if (employeeId == null) {
            throw new AppException(Constants.ER001, List.of(idLabel));
        }
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(Constants.ER014, List.of(idLabel)));

        if (Constants.ROLE_ADMIN == employee.getRole()
                || Constants.ADMIN_LOGIN_ID.equals(employee.getEmployeeLoginId())) {
            throw new AppException(Constants.ER020);
        }
        return employee;
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
