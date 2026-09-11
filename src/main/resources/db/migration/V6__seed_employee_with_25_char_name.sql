-- -- ============================================================
-- -- V6__seed_employee_with_25_char_name.sql
-- -- Thêm nhân viên mẫu có tên dài đúng 25 ký tự để test hiển thị rút gọn (trên 22 ký tự theo ADM002)
-- -- Tên: "Nguyễn Thị Mai Phương Anh" (25 ký tự)
-- -- ============================================================

-- INSERT INTO `employees` (`employee_id`, `department_id`, `employee_name`, `employee_name_kana`, `employee_birth_date`, `employee_email`, `employee_telephone`, `employee_login_id`, `employee_login_password`, `employee_role`) VALUES
-- (142, 1, 'Nguyễn Thị Mai Phương Anh', 'グエン ティ マイ フオン アイン', '1995-05-15', 'anhntmp@luvina.net', '0912345678', 'anhntmp', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0)
-- ON DUPLICATE KEY UPDATE 
--     `employee_name` = VALUES(`employee_name`),
--     `employee_name_kana` = VALUES(`employee_name_kana`),
--     `employee_email` = VALUES(`employee_email`);

-- INSERT INTO `employees_certifications` (`employee_certification_id`, `employee_id`, `certification_id`, `start_date`, `end_date`, `score`) VALUES
-- (157, 142, 1, '2023-06-01', '2027-06-01', 180.0)
-- ON DUPLICATE KEY UPDATE `score` = VALUES(`score`);
