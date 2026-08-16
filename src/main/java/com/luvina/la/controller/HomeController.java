/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * HomeController.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý các yêu cầu trang chủ mặc định của ứng dụng.
 *
 * @author thanhvinh
 */
@RestController
public class HomeController {

    /**
     * Endpoint gốc kiểm tra trạng thái hoạt động của Service.
     *
     * @return Chuỗi thông báo chào mừng
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Employee service";
    }
}
