/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * MessageResponse.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.payload.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object mô tả nội dung lỗi: mã lỗi và tham số trả về trong response.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã lỗi, ví dụ ER023. */
    private String code;

    /** Danh sách tham số thay thế trong nội dung message (mặc định rỗng). */
    private List<Object> params;
}
