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
import com.luvina.la.payload.request.CertificationRequest;
import com.luvina.la.payload.request.EmployeeRequest;
import com.luvina.la.payload.request.EmployeeSearchRequest;
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
     * Kiểm tra tính hợp lệ của các tham số tìm kiếm danh sách nhân viên (ADM002).
     * Thứ tự kiểm tra quyết định mã lỗi trả về trước tiên theo đặc tả:
     * 1. Hướng sắp xếp tên nhân viên (ordEmployeeName) -> ER021
     * 2. Hướng sắp xếp tên chứng chỉ (ordCertificationName) -> ER021
     * 3. Hướng sắp xếp ngày hết hạn (ordEndDate) -> ER021
     * 4. Độ dài tên nhân viên (tối đa 125 ký tự) -> ER006
     * 5. ID phòng ban (số nguyên dương) -> ER018
     * 6. Vị trí bắt đầu offset (số nguyên không âm) -> ER018
     * 7. Số bản ghi mỗi trang limit (số nguyên dương) -> ER018
     *
     * @param request Đối tượng chứa các tham số tìm kiếm
     * @return MessageDTO chứa thông tin lỗi nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateSearchEmployees(EmployeeSearchRequest request) {
        if (request == null) {
            return null;
        }

        // 1. Kiểm tra các tham số sắp xếp (chỉ chấp nhận rỗng, ASC hoặc DESC)
        MessageDTO messageDto = validateSortOrder(request.getOrdEmployeeName());
        if (messageDto != null) {
            return messageDto;
        }
        messageDto = validateSortOrder(request.getOrdCertificationName());
        if (messageDto != null) {
            return messageDto;
        }
        messageDto = validateSortOrder(request.getOrdEndDate());
        if (messageDto != null) {
            return messageDto;
        }

        // 2. Kiểm tra độ dài tên nhân viên (tối đa 125 ký tự)
        messageDto = validateSearchEmployeeName(request.getEmployeeName());
        if (messageDto != null) {
            return messageDto;
        }

        // 3. Kiểm tra ID phòng ban (nếu có thì phải là số nguyên dương)
        messageDto = validateSearchDepartmentId(request.getDepartmentId());
        if (messageDto != null) {
            return messageDto;
        }

        // 4. Kiểm tra offset (số nguyên không âm)
        messageDto = validateSearchOffset(request.getOffset());
        if (messageDto != null) {
            return messageDto;
        }

        // 5. Kiểm tra limit (số nguyên dương)
        messageDto = validateSearchLimit(request.getLimit());
        if (messageDto != null) {
            return messageDto;
        }

        return null;
    }

    /**
     * Kiểm tra hướng sắp xếp, chỉ chấp nhận rỗng, ASC hoặc DESC.
     *
     * @param order Hướng sắp xếp
     * @return MessageDTO chứa ER021 nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateSortOrder(String order) {
        if (!commonValidator.isEmpty(order)) {
            String trimmedOrder = order.trim();
            if (!SortOrder.isSupported(trimmedOrder)) {
                return buildMessage(Constants.ER021);
            }
        }
        return null;
    }

    /**
     * Kiểm tra độ dài tên nhân viên tìm kiếm không vượt quá 125 ký tự.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @return MessageDTO chứa ER006 nếu vượt quá 125 ký tự, hoặc null nếu hợp lệ
     */
    public MessageDTO validateSearchEmployeeName(String employeeName) {
        if (employeeName != null) {
            int characterCount = employeeName.codePointCount(0, employeeName.length());
            if (characterCount > Constants.EMPLOYEE_NAME_MAX_LENGTH) {
                return buildMessage(
                        Constants.ER006,
                        Constants.EMPLOYEE_NAME_MAX_LENGTH,
                        getLabel(Constants.FIELD_FULLNAME)
                );
            }
        }
        return null;
    }

    /**
     * Kiểm tra ID phòng ban tìm kiếm (nếu có thì phải là số nguyên dương).
     *
     * @param departmentId ID phòng ban dạng chuỗi
     * @return MessageDTO chứa ER018 nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateSearchDepartmentId(String departmentId) {
        if (!commonValidator.isEmpty(departmentId)) {
            String trimmedDepartmentId = departmentId.trim();
            if (!commonValidator.isHalfWidthNumber(trimmedDepartmentId, Constants.MAX_DEPARTMENT_ID_DIGITS)
                    || "0".equals(trimmedDepartmentId)) {
                return buildMessage(
                        Constants.ER018,
                        getLabel(Constants.FIELD_DEPARTMENT_ID)
                );
            }
        }
        return null;
    }

    /**
     * Kiểm tra tham số offset tìm kiếm (nếu có thì phải là số nguyên không âm).
     *
     * @param offset Offset dạng chuỗi
     * @return MessageDTO chứa ER018 nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateSearchOffset(String offset) {
        if (!commonValidator.isEmpty(offset)) {
            String trimmedOffset = offset.trim();
            if (!commonValidator.isHalfWidthNumber(trimmedOffset, 9)) {
                return buildMessage(Constants.ER018, getLabel(Constants.FIELD_OFFSET));
            }
        }
        return null;
    }

    /**
     * Kiểm tra tham số limit tìm kiếm (nếu có thì phải là số nguyên dương).
     *
     * @param limit Limit dạng chuỗi
     * @return MessageDTO chứa ER018 nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateSearchLimit(String limit) {
        if (!commonValidator.isEmpty(limit)) {
            String trimmedLimit = limit.trim();
            if (!commonValidator.isHalfWidthNumber(trimmedLimit, 9) || "0".equals(trimmedLimit)) {
                return buildMessage(Constants.ER018, getLabel(Constants.FIELD_LIMIT));
            }
        }
        return null;
    }

    /**
     * Chuẩn hóa và escape tên nhân viên cho điều kiện LIKE sau khi đã validate.
     *
     * @param employeeName Tên nhân viên cần tìm kiếm
     * @return Mẫu LIKE đã escape hoặc null nếu rỗng
     */
    public String escapeEmployeeName(String employeeName) {
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
     * Parse ID phòng ban sang Long sau khi đã qua validate.
     *
     * @param departmentId ID phòng ban dạng chuỗi
     * @return ID phòng ban dạng Long hoặc null nếu rỗng
     */
    public Long parseDepartmentId(String departmentId) {
        if (commonValidator.isEmpty(departmentId)) {
            return null;
        }
        return Long.valueOf(departmentId.trim());
    }

    /**
     * Parse offset sang số nguyên sau khi đã qua validate.
     *
     * @param offset Offset dạng chuỗi
     * @return Offset nguyên hoặc giá trị mặc định nếu rỗng
     */
    public int parseOffset(String offset) {
        if (commonValidator.isEmpty(offset)) {
            return Constants.DEFAULT_EMPLOYEE_OFFSET;
        }
        return Integer.parseInt(offset.trim());
    }

    /**
     * Parse limit sang số nguyên sau khi đã qua validate.
     *
     * @param limit Limit dạng chuỗi
     * @return Limit nguyên hoặc giá trị mặc định nếu rỗng
     */
    public int parseLimit(String limit) {
        if (commonValidator.isEmpty(limit)) {
            return Constants.DEFAULT_EMPLOYEE_PAGE_SIZE;
        }
        return Integer.parseInt(limit.trim());
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
        String label = getLabel(Constants.FIELD_ID);
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
        String label = getLabel(Constants.FIELD_LOGIN_ID);
        if (isEdit) {
            // Ở chế độ Edit: không check bắt buộc, độ dài hay định dạng
            if (!commonValidator.isEmpty(loginId) && employeeId != null) {
                if (employeeRepository.existsByEmployeeLoginIdAndEmployeeIdNot(loginId.trim(), employeeId)) {
                    messageDto = buildMessage(Constants.ER003, label);
                }
            }
            return messageDto;
        }

        if (commonValidator.isEmpty(loginId)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(loginId.trim(), Constants.MAX_LENGTH_50)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_50, label);
        } else if (!commonValidator.isValidLoginId(loginId.trim())) {
            messageDto = buildMessage(Constants.ER019);
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
        String label = getLabel(Constants.FIELD_GROUP);
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
        String label = getLabel(Constants.FIELD_FULLNAME);
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
        String label = getLabel(Constants.FIELD_FULLNAME_KANA);
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
        String label = getLabel(Constants.FIELD_BIRTH_DATE);
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
        String label = getLabel(Constants.FIELD_EMAIL);
        if (commonValidator.isEmpty(email)) {
            messageDto = buildMessage(Constants.ER001, label);
        } else if (commonValidator.isMaxLength(email.trim(), Constants.MAX_LENGTH_125)) {
            messageDto = buildMessage(Constants.ER006, Constants.MAX_LENGTH_125, label);
        } else if (!commonValidator.isValidEmail(email.trim())) {
            messageDto = buildMessage(Constants.ER005, label, Constants.EMAIL_FORMAT_TOKEN);
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
        String label = getLabel(Constants.FIELD_TELEPHONE);
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
        String label = getLabel(Constants.FIELD_PASSWORD);
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
        String certLabel = getLabel(Constants.FIELD_CERTIFICATION);

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
        String startLabel = getLabel(Constants.FIELD_START_DATE);

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
        String endLabel = getLabel(Constants.FIELD_END_DATE);

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
        String scoreLabel = getLabel(Constants.FIELD_SCORE);

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
     * @return MessageDTO chứa thông tin lỗi nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateGetEmployeeDetail(Long employeeId) {
        String idLabel = getLabel(Constants.FIELD_ID);
        if (employeeId == null) {
            return buildMessage(Constants.ER001, idLabel);
        }
        EmployeeEntity employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null || Constants.ROLE_ADMIN == employee.getRole()) {
            return buildMessage(Constants.ER013, idLabel);
        }
        return null;
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
        if (!commonValidator.isHalfWidthNumber(trimmed, Constants.MAX_DEPARTMENT_ID_DIGITS)) {
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
     * @return MessageDTO chứa thông tin lỗi nếu không hợp lệ, hoặc null nếu hợp lệ
     */
    public MessageDTO validateDeleteEmployee(Long employeeId) {
        String idLabel = getLabel(Constants.FIELD_ID);
        if (employeeId == null) {
            return buildMessage(Constants.ER001, idLabel);
        }
        EmployeeEntity employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return buildMessage(Constants.ER014, idLabel);
        }

        if (Constants.ROLE_ADMIN == employee.getRole()
                || Constants.ADMIN_LOGIN_ID.equals(employee.getEmployeeLoginId())) {
            return buildMessage(Constants.ER020);
        }
        return null;
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
