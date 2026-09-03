/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationService.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.service;

import java.util.List;

import com.luvina.la.dto.CertificationDTO;

/**
 * Giao diện nghiệp vụ cho chức năng quản lý chứng chỉ.
 *
 * @author thanhvinh
 */
public interface CertificationService {

    /**
     * Lấy danh sách tất cả chứng chỉ (sắp xếp theo cấp độ tăng dần).
     *
     * @return Danh sách DTO chứng chỉ
     */
    List<CertificationDTO> getListCertifications();
}
