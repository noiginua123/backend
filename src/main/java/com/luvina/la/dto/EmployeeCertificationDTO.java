/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertificationDTO.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) chứa thông tin chứng chỉ được cấp cho nhân viên.
 *
 * @author thanhvinh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCertificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long employeeCertificationId;
    private Long employeeId;
    private Long certificationId;
    private String certificationName;
    private Integer certificationLevel;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal score;
}
