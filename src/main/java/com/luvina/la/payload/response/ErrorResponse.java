/**
 * Copyright(C) 2026  Luvina Software Company
 *
 * ErrorResponse.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response trả về khi API xảy ra lỗi.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã code (500 khi lỗi hệ thống). */
    private Long code;

    /** Nội dung lỗi. */
    private Message message;
}
