/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertification.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity ánh xạ tới bảng `employees_certifications` lưu trữ quan hệ giữa nhân viên và chứng chỉ tiếng Nhật.
 *
 * @author thanhvinh
 */
@Entity
@Table(name = "employees_certifications")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCertificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_certification_id", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private Long employeeCertificationId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "certification_id", nullable = false)
    private Long certificationId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    @ToString.Exclude
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id", insertable = false, updatable = false)
    @ToString.Exclude
    private CertificationEntity certification;
}
