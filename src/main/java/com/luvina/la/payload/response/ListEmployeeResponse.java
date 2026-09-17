/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * ListEmployeeResponse.java, 20/08/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luvina.la.dto.EmployeeListDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response cho API Get List Employees (ADM002).
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListEmployeeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã code, 200 khi thành công, 500 khi lỗi. */
    private Long code;

    /** Tổng số bản ghi thỏa mãn điều kiện tìm kiếm. */
    private Long totalRecords;

    /** Danh sách nhân viên. */
    private List<EmployeeListDTO> employees;

    /** Thông tin thông báo kết quả hoặc lỗi. */
    private MessageResponse message;

    /**
     * Constructor phục vụ trường hợp thành công (không có message).
     *
     * @param code Mã trạng thái (200)
     * @param totalRecords Tổng số bản ghi
     * @param employees Danh sách nhân viên
     */
    public ListEmployeeResponse(Long code, Long totalRecords, List<EmployeeListDTO> employees) {
        this.code = code;
        this.totalRecords = totalRecords;
        this.employees = employees;
    }

    /**
     * Constructor phục vụ trường hợp lỗi validate (chỉ có code và message).
     *
     * @param code Mã trạng thái lỗi (500)
     * @param message Thông tin lỗi trả về
     */
    public ListEmployeeResponse(Long code, MessageResponse message) {
        this.code = code;
        this.message = message;
    }
}
