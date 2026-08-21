/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeListItemProjection.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.math.BigDecimal;

/**
 * Projection hứng dữ liệu từ native query danh sách nhân viên.
 *
 * @author thanhvinh
 */
public interface EmployeeListItemProjection {

    /**
     * Lấy ID nhân viên.
     *
     * @return ID nhân viên
     */
    Long getEmployeeId();

    /**
     * Lấy tên nhân viên.
     *
     * @return Tên nhân viên
     */
    String getEmployeeName();

    /**
     * Lấy ngày sinh nhân viên định dạng yyyy/MM/dd.
     *
     * @return Ngày sinh nhân viên
     */
    String getEmployeeBirthDate();

    /**
     * Lấy tên phòng ban.
     *
     * @return Tên phòng ban
     */
    String getDepartmentName();

    /**
     * Lấy email nhân viên.
     *
     * @return Email nhân viên
     */
    String getEmployeeEmail();

    /**
     * Lấy số điện thoại nhân viên.
     *
     * @return Số điện thoại nhân viên
     */
    String getEmployeeTelephone();

    /**
     * Lấy tên chứng chỉ cao nhất.
     *
     * @return Tên chứng chỉ hoặc null
     */
    String getCertificationName();

    /**
     * Lấy ngày hết hạn chứng chỉ định dạng yyyy/MM/dd.
     *
     * @return Ngày hết hạn hoặc null
     */
    String getEndDate();

    /**
     * Lấy điểm số chứng chỉ.
     *
     * @return Điểm số hoặc null
     */
    BigDecimal getScore();

    /**
     * Lấy quyền của nhân viên.
     *
     * @return true nếu là admin, false nếu là user
     */
    Boolean getRole();
}
