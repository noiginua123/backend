/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * AuthUserDetails.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config.jwt;

import com.luvina.la.entity.Employee;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Lớp đóng gói thông tin UserDetails phục vụ xác thực người dùng trong Spring Security.
 *
 * @author thanhvinh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Employee employee;
    private Collection<GrantedAuthority> authorities;

    /**
     * Lấy danh sách quyền hạn (Authorities / Roles) của người dùng.
     *
     * @return Tập hợp các GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * Lấy mật khẩu mã hóa của người dùng.
     *
     * @return Chuỗi mật khẩu
     */
    @Override
    public String getPassword() {
        return employee.getEmployeeLoginPassword();
    }

    /**
     * Lấy tên đăng nhập của người dùng.
     *
     * @return Tên tài khoản đăng nhập
     */
    @Override
    public String getUsername() {
        return employee.getEmployeeLoginId();
    }

    /**
     * Kiểm tra tài khoản có hết hạn hay không.
     *
     * @return true nếu tài khoản còn hiệu lực
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Kiểm tra tài khoản có bị khóa hay không.
     *
     * @return true nếu tài khoản không bị khóa
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Kiểm tra thông tin xác thực (mật khẩu) có hết hạn hay không.
     *
     * @return true nếu thông tin còn hiệu lực
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Kiểm tra tài khoản có được kích hoạt hay không.
     *
     * @return true nếu tài khoản đang hoạt động
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
