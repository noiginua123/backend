/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * Certification.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity ánh xạ tới bảng `certifications` lưu trữ thông tin các loại chứng chỉ tiếng Nhật.
 *
 * @author thanhvinh
 */
@Entity
@Table(name = "certifications")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class CertificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private Long certificationId;

    @Column(name = "certification_name", nullable = false, length = 50)
    private String certificationName;

    @Column(name = "certification_level", nullable = false)
    private Integer certificationLevel;

    /**
     * Quan hệ 1 - n: Một chứng chỉ được cấp cho danh sách nhiều nhân viên.
     */
    @OneToMany(mappedBy = "certification", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<EmployeeCertificationEntity> employeeCertifications;
}
