/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * DepartmentRepository.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luvina.la.entity.DepartmentEntity;

/**
 * Repository thao tác với bảng departments.
 *
 * @author thanhvinh
 */
@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    /**
     * Lấy danh sách tất cả phòng ban, sắp xếp theo ID tăng dần.
     *
     * @return Danh sách các phòng ban
     */
    List<DepartmentEntity> findAllByOrderByDepartmentIdAsc();
}
