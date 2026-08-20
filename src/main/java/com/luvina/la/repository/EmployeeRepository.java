/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRepository.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luvina.la.dto.EmployeeListItemProjection;
import com.luvina.la.entity.EmployeeEntity;

/**
 * Repository thao tác truy vấn dữ liệu bảng `employees` trong Cơ sở dữ liệu.
 *
 * @author thanhvinh
 */
@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {

    /**
     * Tìm kiếm nhân viên theo tên đăng nhập (login id).
     *
     * @param employeeLoginId Tên tài khoản đăng nhập
     * @return Optional chứa Employee nếu tìm thấy, hoặc empty nếu không tồn tại
     */
    Optional<EmployeeEntity> findByEmployeeLoginId(String employeeLoginId);

    /**
     * Tìm kiếm nhân viên theo mã ID nhân viên.
     *
     * @param employeeId Mã định danh nhân viên
     * @return Optional chứa Employee nếu tìm thấy, hoặc empty nếu không tồn tại
     */
    Optional<EmployeeEntity> findByEmployeeId(Long employeeId);

    /**
     * Đếm tổng số lượng nhân viên thỏa mãn điều kiện tìm kiếm (loại trừ tài khoản admin).
     *
     * @param employeeName Tên nhân viên cần tìm kiếm (đã escape và bọc %...%, hoặc null)
     * @param departmentId ID phòng ban cần lọc (hoặc null nếu không lọc)
     * @return Tổng số nhân viên thỏa mãn
     */
    @Query(value = "SELECT COUNT(e.employee_id) "
            + "FROM employees e "
            + "JOIN departments d ON d.department_id = e.department_id "
            + "WHERE e.role <> 1 "
            + "  AND (:departmentId IS NULL OR e.department_id = :departmentId) "
            + "  AND (:employeeName IS NULL OR e.employee_name LIKE :employeeName ESCAPE '!')",
            nativeQuery = true)
    long countEmployees(@Param("employeeName") String employeeName,
                        @Param("departmentId") Long departmentId);

    /**
     * Tìm kiếm và phân trang danh sách nhân viên kèm chứng chỉ cao nhất (loại trừ tài khoản admin).
     *
     * @param employeeName Tên nhân viên cần tìm kiếm (đã escape và bọc %...%, hoặc null)
     * @param departmentId ID phòng ban cần lọc (hoặc null nếu không lọc)
     * @param ordEmployeeName Hướng sắp xếp theo tên nhân viên ("ASC", "DESC" hoặc "")
     * @param ordCertificationName Hướng sắp xếp theo tên chứng chỉ ("ASC", "DESC" hoặc "")
     * @param ordEndDate Hướng sắp xếp theo ngày hết hạn chứng chỉ ("ASC", "DESC" hoặc "")
     * @param limit Số lượng bản ghi tối đa lấy ra
     * @param offset Vị trí bắt đầu lấy bản ghi
     * @return Danh sách projection thông tin nhân viên
     */
    @Query(value = "SELECT "
            + "  e.employee_id AS employeeId, "
            + "  e.employee_name AS employeeName, "
            + "  DATE_FORMAT(e.employee_birth_date, '%Y/%m/%d') AS employeeBirthDate, "
            + "  d.department_name AS departmentName, "
            + "  e.employee_email AS employeeEmail, "
            + "  e.employee_telephone AS employeeTelephone, "
            + "  c.certification_name AS certificationName, "
            + "  DATE_FORMAT(ec.end_date, '%Y/%m/%d') AS endDate, "
            + "  ec.score AS score "
            + "FROM employees e "
            + "JOIN departments d ON d.department_id = e.department_id "
            + "LEFT JOIN employees_certifications ec ON ec.employee_certification_id = ( "
            + "  SELECT ec2.employee_certification_id "
            + "  FROM employees_certifications ec2 "
            + "  JOIN certifications c2 ON c2.certification_id = ec2.certification_id "
            + "  WHERE ec2.employee_id = e.employee_id "
            + "  ORDER BY c2.certification_level ASC, ec2.end_date DESC, ec2.employee_certification_id DESC "
            + "  LIMIT 1 "
            + ") "
            + "LEFT JOIN certifications c ON c.certification_id = ec.certification_id "
            + "WHERE e.role <> 1 "
            + "  AND (:departmentId IS NULL OR e.department_id = :departmentId) "
            + "  AND (:employeeName IS NULL OR e.employee_name LIKE :employeeName ESCAPE '!') "
            + "ORDER BY "
            + "  CASE WHEN :ordEmployeeName = 'ASC'  THEN e.employee_name END ASC, "
            + "  CASE WHEN :ordEmployeeName = 'DESC' THEN e.employee_name END DESC, "
            + "  CASE WHEN :ordCertificationName = 'ASC'  THEN c.certification_name END ASC, "
            + "  CASE WHEN :ordCertificationName = 'DESC' THEN c.certification_name END DESC, "
            + "  CASE WHEN :ordEndDate = 'ASC'  THEN ec.end_date END ASC, "
            + "  CASE WHEN :ordEndDate = 'DESC' THEN ec.end_date END DESC, "
            + "  e.employee_id ASC "
            + "LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<EmployeeListItemProjection> searchEmployees(@Param("employeeName") String employeeName,
                                                    @Param("departmentId") Long departmentId,
                                                    @Param("ordEmployeeName") String ordEmployeeName,
                                                    @Param("ordCertificationName") String ordCertificationName,
                                                    @Param("ordEndDate") String ordEndDate,
                                                    @Param("limit") int limit,
                                                    @Param("offset") int offset);
}
