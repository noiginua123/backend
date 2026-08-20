/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeListItemProjection.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.math.BigDecimal;

/**
 * Interface projection hứng dữ liệu kết quả từ câu truy vấn native query danh sách nhân viên.
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
     * Lấy ngày sinh nhân viên đã định dạng chuỗi (yyyy/MM/dd).
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
     * Lấy địa chỉ email nhân viên.
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
     * @return Tên chứng chỉ hoặc null nếu chưa có
     */
    String getCertificationName();

    /**
     * Lấy ngày kết thúc chứng chỉ đã định dạng chuỗi (yyyy/MM/dd).
     *
     * @return Ngày kết thúc hoặc null nếu chưa có
     */
    String getEndDate();

    /**
     * Lấy điểm số chứng chỉ.
     *
     * @return Điểm số hoặc null nếu chưa có
     */
    BigDecimal getScore();
}
