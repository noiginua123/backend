/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRepository.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luvina.la.dto.EmployeeListItemProjection;
import com.luvina.la.entity.EmployeeEntity;

/**
 * Repository thao tác truy vấn dữ liệu nhân viên.
 *
 * @author thanhvinh
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    /**
     * Tìm kiếm nhân viên theo tên đăng nhập.
     *
     * @param employeeLoginId Tên tài khoản đăng nhập
     * @return Nhân viên tương ứng hoặc Optional rỗng
     */
    Optional<EmployeeEntity> findByEmployeeLoginId(String employeeLoginId);

    /**
     * Tìm kiếm nhân viên theo ID.
     *
     * @param employeeId ID nhân viên
     * @return Nhân viên tương ứng hoặc Optional rỗng
     */
    Optional<EmployeeEntity> findByEmployeeId(Long employeeId);

    /**
     * Đếm tổng số nhân viên (loại trừ tài khoản admin) dùng cho phân trang của ADM002.
     *
     * Query áp dụng lần lượt các điều kiện:
     * 1. Loại trừ tài khoản quản trị viên (employee_login_id = 'admin').
     * 2. Lọc phòng ban khi departmentId khác null.
     * 3. Tìm gần đúng tên khi employeeName khác null. Giá trị employeeName đã
     *    được Validator escape và bao quanh bởi ký tự phần trăm.
     *
     * Các điều kiện này phải giống điều kiện WHERE của searchEmployees để
     * totalRecords không bị lệch với danh sách trả về.
     *
     * @param employeeName Mẫu LIKE đã được escape, hoặc null nếu không tìm theo tên
     * @param departmentId ID phòng ban, hoặc null nếu lấy tất cả phòng ban
     * @return Tổng số nhân viên thỏa mãn điều kiện tìm kiếm (không bao gồm admin)
     */
    @Query(value = """
            SELECT COUNT(e.employee_id)
            FROM employees e
            WHERE e.employee_login_id != 'admin'
              AND (:departmentId IS NULL OR e.department_id = :departmentId)
              AND (:employeeName IS NULL OR e.employee_name LIKE :employeeName ESCAPE '!')
            """, nativeQuery = true)
    long countEmployees(@Param("employeeName") String employeeName,
                        @Param("departmentId") Long departmentId);

    /**
     * Lấy danh sách nhân viên (loại trừ tài khoản admin) theo điều kiện tìm kiếm và phân trang.
     *
     * Luồng xử lý của native query:
     * 1. Lấy thông tin cơ bản từ employees và departments (loại bỏ admin).
     * 2. Subquery trong LEFT JOIN chỉ chọn một chứng chỉ cao nhất của mỗi nhân viên.
     *    Cấp có certification_level nhỏ hơn được ưu tiên; nếu cùng cấp thì chọn
     *    end_date mới hơn, sau đó chọn employee_certification_id lớn hơn.
     * 3. Áp dụng điều kiện phòng ban và tên giống query count.
     * 4. CASE WHEN chỉ kích hoạt cột sort được frontend truyền ASC hoặc DESC.
     * 5. employee_id ASC là điều kiện sort cuối để kết quả luôn ổn định.
     * 6. LIMIT/OFFSET lấy đúng số bản ghi của trang hiện tại.
     *
     * Mỗi alias trong SELECT phải khớp với getter của EmployeeListItemProjection.
     * Không join trực tiếp toàn bộ chứng chỉ vì một nhân viên có thể bị trả thành nhiều dòng.
     *
     * @param employeeName Mẫu LIKE đã được escape, hoặc null nếu không tìm theo tên
     * @param departmentId ID phòng ban, hoặc null nếu lấy tất cả phòng ban
     * @param ordEmployeeName ASC/DESC để sort tên, hoặc chuỗi rỗng nếu không sort
     * @param ordCertificationName ASC/DESC để sort chứng chỉ, hoặc chuỗi rỗng
     * @param ordEndDate ASC/DESC để sort ngày hết hạn, hoặc chuỗi rỗng
     * @param prioritySort Cột ưu tiên làm tiêu chí sort chính (employeeName / certificationName / endDate)
     * @param limit Số bản ghi tối đa cần lấy
     * @param offset Vị trí bản ghi bắt đầu lấy
     * @return Danh sách projection, mỗi phần tử tương ứng một nhân viên
     */
    @Query(value = """
            SELECT
                e.employee_id AS employeeId,
                e.employee_name AS employeeName,
                DATE_FORMAT(e.employee_birth_date, '%Y/%m/%d') AS employeeBirthDate,
                d.department_name AS departmentName,
                e.employee_email AS employeeEmail,
                e.employee_telephone AS employeeTelephone,
                c.certification_name AS certificationName,
                DATE_FORMAT(ec.end_date, '%Y/%m/%d') AS endDate,
                ec.score AS score,
                e.employee_role AS role
            FROM employees e
            INNER JOIN departments d
                ON d.department_id = e.department_id
            LEFT JOIN employees_certifications ec
                ON ec.employee_certification_id = (
                    SELECT ec2.employee_certification_id
                    FROM employees_certifications ec2
                    INNER JOIN certifications c2
                        ON c2.certification_id = ec2.certification_id
                    WHERE ec2.employee_id = e.employee_id
                    ORDER BY
                        c2.certification_level ASC,
                        ec2.end_date DESC,
                        ec2.employee_certification_id DESC
                    LIMIT 1
                )
            LEFT JOIN certifications c
                ON c.certification_id = ec.certification_id
            WHERE e.employee_login_id != 'admin'
              AND (:departmentId IS NULL OR e.department_id = :departmentId)
              AND (:employeeName IS NULL OR e.employee_name LIKE :employeeName ESCAPE '!')
            ORDER BY
                -- (a) Cột ưu tiên sort trước theo chiều được chọn
                CASE WHEN :prioritySort = 'employeeName'      AND :ordEmployeeName      = 'ASC'  THEN e.employee_name      END ASC,
                CASE WHEN :prioritySort = 'employeeName'      AND :ordEmployeeName      = 'DESC' THEN e.employee_name      END DESC,
                CASE WHEN :prioritySort = 'certificationName' AND :ordCertificationName = 'ASC'  THEN c.certification_name END ASC,
                CASE WHEN :prioritySort = 'certificationName' AND :ordCertificationName = 'DESC' THEN c.certification_name END DESC,
                CASE WHEN :prioritySort = 'endDate'           AND :ordEndDate           = 'ASC'  THEN ec.end_date          END ASC,
                CASE WHEN :prioritySort = 'endDate'           AND :ordEndDate           = 'DESC' THEN ec.end_date          END DESC,
                -- (b) 3 cột sort phụ theo THỨ TỰ cố định name -> cert -> endDate, theo CHIỀU đang lưu của từng cột
                CASE WHEN :ordEmployeeName      = 'ASC'  THEN e.employee_name      END ASC,
                CASE WHEN :ordEmployeeName      = 'DESC' THEN e.employee_name      END DESC,
                CASE WHEN :ordCertificationName = 'ASC'  THEN c.certification_name END ASC,
                CASE WHEN :ordCertificationName = 'DESC' THEN c.certification_name END DESC,
                CASE WHEN :ordEndDate           = 'ASC'  THEN ec.end_date          END ASC,
                CASE WHEN :ordEndDate           = 'DESC' THEN ec.end_date          END DESC,
                -- (c) Tie-breaker cuối
                e.employee_id ASC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<EmployeeListItemProjection> searchEmployees(
            @Param("employeeName") String employeeName,
            @Param("departmentId") Long departmentId,
            @Param("ordEmployeeName") String ordEmployeeName,
            @Param("ordCertificationName") String ordCertificationName,
            @Param("ordEndDate") String ordEndDate,
            @Param("prioritySort") String prioritySort,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
