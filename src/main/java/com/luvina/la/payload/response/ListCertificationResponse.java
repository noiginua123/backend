/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * ListCertificationResponse.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;
import java.util.List;

import com.luvina.la.dto.CertificationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response cho API Get List Certifications (trường hợp thành công).
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListCertificationResponse implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** Mã code (200 khi thành công). */
    private Long code;

    /** Danh sách chứng chỉ. */
    private List<CertificationDTO> certifications;
}
