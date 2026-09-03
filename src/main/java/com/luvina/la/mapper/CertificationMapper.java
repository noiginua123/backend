/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationMapper.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.mapper;

import org.springframework.stereotype.Component;

import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.entity.CertificationEntity;

/**
 * Chuyển đổi giữa CertificationEntity sang CertificationDTO.
 *
 * @author thanhvinh
 */
@Component
public class CertificationMapper {

    /**
     * Chuyển đổi một thực thể chứng chỉ sang đối tượng DTO.
     *
     * @param entity Thực thể chứng chỉ nguồn
     * @return CertificationDTO tương ứng hoặc null nếu entity là null
     */
    public CertificationDTO toDto(CertificationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CertificationDTO(
                entity.getCertificationId(),
                entity.getCertificationName(),
                entity.getCertificationLevel()
        );
    }
}
