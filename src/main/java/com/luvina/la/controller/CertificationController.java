/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationController.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luvina.la.constant.Constants;
import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.payload.response.ListCertificationResponse;
import com.luvina.la.service.CertificationService;

/**
 * Controller cho chức năng chứng chỉ.
 *
 * @author thanhvinh
 */
@RestController
@RequestMapping("/certification")
public class CertificationController {

    /** Service xử lý nghiệp vụ chứng chỉ. */
    private final CertificationService certificationService;

    /**
     * Khởi tạo controller với service chứng chỉ.
     *
     * @param certificationService Service xử lý nghiệp vụ chứng chỉ
     */
    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    /**
     * API lấy danh sách tất cả chứng chỉ.
     *
     * @return ResponseEntity chứa ListCertificationResponse
     */
    @GetMapping
    public ResponseEntity<ListCertificationResponse> getListCertifications() {
        List<CertificationDTO> certifications = certificationService.getListCertifications();
        ListCertificationResponse response = new ListCertificationResponse(Constants.CODE_SUCCESS, certifications);
        return ResponseEntity.ok(response);
    }
}
