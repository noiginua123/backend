/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * MessageDTO.java, 09/09/2026 thanhvinh
 */
package com.luvina.la.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object mô tả thông tin thông báo/lỗi (mã lỗi và tham số) dùng giữa các tầng nội bộ.
 *
 * @author thanhvinh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Mã lỗi hoặc thông báo (ví dụ: ER001, ER003, MSG001). */
    private String code;

    /** Danh sách tham số thay thế trong nội dung thông điệp. */
    private List<Object> params;
}
