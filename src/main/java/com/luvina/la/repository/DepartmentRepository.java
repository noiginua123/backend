/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentRepository.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import com.luvina.la.entity.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository thao tác truy vấn dữ liệu bảng `departments` trong Cơ sở dữ liệu.
 *
 * @author thanhvinh
 */
@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {
}
