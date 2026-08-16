/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRepository.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import com.luvina.la.entity.Employee;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository thao tác truy vấn dữ liệu bảng `employees` trong Cơ sở dữ liệu.
 *
 * @author thanhvinh
 */
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    /**
     * Tìm kiếm nhân viên theo tên đăng nhập (login id).
     *
     * @param employeeLoginId Tên tài khoản đăng nhập
     * @return Optional chứa Employee nếu tìm thấy, hoặc empty nếu không tồn tại
     */
    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);

    /**
     * Tìm kiếm nhân viên theo mã ID nhân viên.
     *
     * @param employeeId Mã định danh nhân viên
     * @return Optional chứa Employee nếu tìm thấy, hoặc empty nếu không tồn tại
     */
    Optional<Employee> findByEmployeeId(Long employeeId);
}
