/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationDTO.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO chứng chỉ tiếng Nhật để đổ vào dropdown 資格 của ADM004.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** ID chứng chỉ (certifications.certification_id). */
    private Long certificationId;

    /** Tên chứng chỉ (certifications.certification_name). */
    private String certificationName;

    /** Cấp độ chứng chỉ, 1 = cao nhất (certifications.certification_level). */
    private Integer certificationLevel;
}
