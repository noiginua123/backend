/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeDTO.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) chứa thông tin của nhân viên truyền tải giữa các tầng và Client.
 *
 * @author thanhvinh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 6868189362900231672L;

    private Long employeeId;
    private Long departmentId;
    private String departmentName;
    private String employeeName;
    private String employeeNameKana;
    private LocalDate employeeBirthDate;
    private String employeeEmail;
    private String employeeTelephone;
    private String employeeLoginId;
    private Integer role;
    private List<EmployeeCertificationDTO> certifications;
}
