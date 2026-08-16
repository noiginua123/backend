/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationRepository.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import com.luvina.la.entity.Certification;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository thao tác truy vấn dữ liệu bảng `certifications` trong Cơ sở dữ liệu.
 *
 * @author thanhvinh
 */
@Repository
public interface CertificationRepository extends CrudRepository<Certification, Long> {

    /**
     * Lấy toàn bộ danh sách chứng chỉ tiếng Nhật sắp xếp tăng dần theo cấp độ (N1 -> N5).
     *
     * @return Danh sách chứng chỉ đã được sắp xếp
     */
    List<Certification> findAllByOrderByCertificationLevelAsc();
}
