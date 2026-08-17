/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * Employee.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity ánh xạ tới bảng `employees` lưu trữ thông tin nhân viên trong hệ thống.
 *
 * @author thanhvinh
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 5771173953267484096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private Long employeeId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "employee_name", nullable = false, length = 255)
    private String employeeName;

    @Column(name = "employee_name_kana", length = 255)
    private String employeeNameKana;

    @Column(name = "employee_birth_date")
    private LocalDate employeeBirthDate;

    @Column(name = "employee_email", nullable = false, length = 255)
    private String employeeEmail;

    @Column(name = "employee_telephone", length = 50)
    private String employeeTelephone;

    @Column(name = "employee_login_id", unique = true, nullable = false, length = 50)
    private String employeeLoginId;

    @Column(name = "employee_login_password", nullable = false, length = 100)
    private String employeeLoginPassword;

    /**
     * Quyền hạn người dùng: 0 = User, 1 = Admin.
     */
    @Column(name = "role", nullable = false)
    private Integer role = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    @ToString.Exclude
    private DepartmentEntity department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<EmployeeCertificationEntity> employeeCertifications;
}
