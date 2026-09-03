/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationServiceImpl.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.mapper.CertificationMapper;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.service.CertificationService;

/**
 * Hiện thực các nghiệp vụ liên quan đến quản lý chứng chỉ.
 *
 * @author thanhvinh
 */
@Service
@Transactional(readOnly = true)
public class CertificationServiceImpl implements CertificationService {

    /** Repository truy vấn dữ liệu chứng chỉ. */
    private final CertificationRepository certificationRepository;

    /** Mapper chuyển đổi thực thể chứng chỉ sang DTO. */
    private final CertificationMapper certificationMapper;

    /**
     * Khởi tạo service chứng chỉ.
     *
     * @param certificationRepository Repository truy vấn chứng chỉ
     * @param certificationMapper Mapper chuyển entity chứng chỉ sang DTO
     */
    public CertificationServiceImpl(CertificationRepository certificationRepository,
                                    CertificationMapper certificationMapper) {
        this.certificationRepository = certificationRepository;
        this.certificationMapper = certificationMapper;
    }

    /**
     * Lấy danh sách tất cả chứng chỉ, sắp xếp theo cấp độ tăng dần và chuyển sang DTO.
     *
     * @return Danh sách DTO chứng chỉ
     */
    @Override
    public List<CertificationDTO> getListCertifications() {
        List<CertificationEntity> entities = certificationRepository.findAllByOrderByCertificationLevelAsc();
        return entities.stream()
                .map(certificationMapper::toDto)
                .collect(Collectors.toList());
    }
}
