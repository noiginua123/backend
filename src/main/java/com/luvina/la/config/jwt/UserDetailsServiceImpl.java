/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * UserDetailsServiceImpl.java, 16/08/2026 thanhvinh
 */
package com.luvina.la.config.jwt;

import com.luvina.la.entity.Employee;
import com.luvina.la.repository.EmployeeRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service tải thông tin chi tiết người dùng và phân quyền từ Database cho Spring Security.
 *
 * @author thanhvinh
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository userRepository;

    /**
     * Khởi tạo UserDetailsServiceImpl với EmployeeRepository.
     *
     * @param userRepository Repository truy vấn thông tin nhân viên
     */
    public UserDetailsServiceImpl(EmployeeRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Tải thông tin người dùng theo tên đăng nhập (login id) và cấp quyền tương ứng.
     *
     * @param username Tên đăng nhập của người dùng
     * @return Đối tượng UserDetails chứa thông tin và quyền hạn người dùng
     * @throws UsernameNotFoundException Ngoại lệ nếu không tìm thấy người dùng
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> entity = this.userRepository.findByEmployeeLoginId(username);

        if (entity.isPresent()) {
            Employee employee = entity.get();
            // Role: 1 = Admin, 0 = User
            String roleName = (employee.getRole() != null && employee.getRole() == 1) ? "ROLE_ADMIN" : "ROLE_USER";
            Collection<GrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority(roleName));
            return new AuthUserDetails(employee, roles);
        } else {
            throw new UsernameNotFoundException("Employee not found with username: " + username);
        }
    }
}
