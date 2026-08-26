-- ============================================================
-- V8__seed_employees_special_characters.sql
-- Thêm dữ liệu nhân viên với các ký tự đặc biệt (/, %, _, ;, ,) để test tìm kiếm theo tên
-- ============================================================

INSERT INTO `employees` (
    `employee_id`,
    `department_id`,
    `employee_name`,
    `employee_name_kana`,
    `employee_birth_date`,
    `employee_email`,
    `employee_telephone`,
    `employee_login_id`,
    `employee_login_password`,
    `employee_role`
) VALUES
(143, 1, 'Pham Thi Thanh Nga', 'グループ', '2002-02-02', 'ngantt@luvina.net', '778520123', 'ngaptt265', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(144, 2, 'QuỳnhNga/', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt266', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(145, 3, 'Quỳnh%Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt267', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(146, 3, 'Quỳnh_Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt268', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(147, 3, 'Quỳnh;Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt269', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(148, 4, 'Quỳnh,Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt269', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0)
ON DUPLICATE KEY UPDATE
    `department_id` = VALUES(`department_id`),
    `employee_name` = VALUES(`employee_name`),
    `employee_name_kana` = VALUES(`employee_name_kana`),
    `employee_birth_date` = VALUES(`employee_birth_date`),
    `employee_email` = VALUES(`employee_email`),
    `employee_telephone` = VALUES(`employee_telephone`),
    `employee_login_id` = VALUES(`employee_login_id`),
    `employee_login_password` = VALUES(`employee_login_password`),
    `employee_role` = VALUES(`employee_role`);
