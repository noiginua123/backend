/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertificationRepository.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import com.luvina.la.entity.EmployeeCertificationEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository thao tác truy vấn dữ liệu bảng `employees_certifications` trong Cơ sở dữ liệu.
 *
 * @author thanhvinh
 */
@Repository
public interface EmployeeCertificationRepository extends CrudRepository<EmployeeCertificationEntity, Long> {

    /**
     * Tìm kiếm danh sách chứng chỉ theo mã ID của nhân viên.
     *
     * @param employeeId Mã định danh nhân viên
     * @return Danh sách chứng chỉ thuộc về nhân viên đó
     */
    List<EmployeeCertificationEntity> findByEmployeeId(Long employeeId);

    /**
     * Xóa toàn bộ chứng chỉ liên quan đến một nhân viên theo mã ID.
     *
     * @param employeeId Mã định danh nhân viên
     */
    void deleteByEmployeeId(Long employeeId);
}
