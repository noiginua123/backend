-- Đảm bảo tính duy nhất của tên đăng nhập ở cấp cơ sở dữ liệu.
-- Validation tại ứng dụng cung cấp ER003; unique constraint bảo vệ cả khi có request đồng thời.
ALTER TABLE `employees`
    ADD CONSTRAINT `UK_employees_employee_login_id`
    UNIQUE (`employee_login_id`);
