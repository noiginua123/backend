/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * DefaultProfileUtil.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;

/**
 * Lớp tiện ích cấu hình Spring Profile mặc định khi khởi động ứng dụng.
 *
 * @author thanhvinh
 */
public final class DefaultProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    /**
     * Private constructor để ngăn chặn việc khởi tạo instance.
     */
    private DefaultProfileUtil() {
    }

    /**
     * Thiết lập profile mặc định cho ứng dụng Spring Boot khi chưa được chỉ định.
     *
     * @param app Đối tượng SpringApplication cần thiết lập profile
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put(SPRING_PROFILE_DEFAULT, Constants.SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }
}
