/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * WebConfiguration.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config;

import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Lớp cấu hình Web Servlet Context khi ứng dụng khởi động.
 *
 * @author thanhvinh
 */
@Configuration
public class WebConfiguration implements ServletContextInitializer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Environment env;

    /**
     * Khởi tạo WebConfiguration với Environment được inject.
     *
     * @param env Đối tượng môi trường của Spring
     */
    public WebConfiguration(Environment env) {
        this.env = env;
    }

    /**
     * Thực thi khi Servlet Context khởi chạy.
     *
     * @param servletContext Context của Servlet
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        log.info("Web application fully configured");
    }
}
