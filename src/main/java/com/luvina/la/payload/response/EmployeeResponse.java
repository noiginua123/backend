/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeResponse.java, 03/09/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response cho API validate / thêm mới nhân viên (ADM004/ADM005).
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse implements Serializable {

    /** Mã định danh tuần tự hóa. */
    private static final long serialVersionUID = 1L;

    /** Mã code (200 khi thành công). */
    private Long code;

    /** ID nhân viên vừa tạo (null khi chỉ validate). */
    private Long employeeId;

    /** Thông báo thành công, ví dụ MSG001 (null khi chỉ validate). */
    private MessageResponse message;
}
