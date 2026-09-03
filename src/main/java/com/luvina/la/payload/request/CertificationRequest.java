/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationRequest.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.payload.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Thông tin một chứng chỉ đính kèm khi thêm mới nhân viên (ADM004).
 *
 * <p>Tên trường đặt theo đúng kiểu dữ liệu EmployeeCertificationDTO bên Front-end:
 * certificationId, startDate, endDate, score. Tất cả là kiểu String để validate ở phía Server.</p>
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificationRequest implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** ID chứng chỉ (資格). */
    private String certificationId;

    /** Ngày cấp chứng chỉ theo định dạng yyyy/MM/dd (資格交付日). */
    private String startDate;

    /** Ngày hết hạn theo định dạng yyyy/MM/dd (失効日). */
    private String endDate;

    /** Điểm số chứng chỉ (点数). */
    private String score;
}
