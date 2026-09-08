/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertificationDTO.java, 08/09/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO chứa thông tin chi tiết một chứng chỉ tiếng Nhật của nhân viên.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCertificationDTO implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** ID chứng chỉ. */
    private Long certificationId;

    /** Tên chứng chỉ tiếng Nhật. */
    private String certificationName;

    /** Ngày cấp chứng chỉ định dạng yyyy/MM/dd. */
    private String startDate;

    /** Ngày hết hạn chứng chỉ định dạng yyyy/MM/dd. */
    private String endDate;

    /** Điểm thi chứng chỉ. */
    private BigDecimal score;
}
