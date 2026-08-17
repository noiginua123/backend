INSERT INTO `departments` (`department_name`) VALUES
  ('Phòng DEV1'),
  ('Phòng DEV2'),
  ('Phòng DEV3'),
  ('Phòng DEV4'),
  ('Phòng DEV5'),
  ('Phòng DEV6'),
  ('Phòng DEV7'),
  ('Phòng DEV8'),
  ('Phòng DEV9'),
  ('Phòng DEV10'),
  ('Phòng DEV11');

-- 2. certifications (5 cấp tiếng Nhật)
INSERT INTO `certifications` (`certification_name`, `certification_level`) VALUES
  ('Trình độ tiếng nhật cấp 1', 1),
  ('Trình độ tiếng nhật cấp 2', 2),
  ('Trình độ tiếng nhật cấp 3', 3),
  ('Trình độ tiếng nhật cấp 4', 4),
  ('Trình độ tiếng nhật cấp 5', 5);

-- 3. employees thường (role = 0) - nhóm trùng tên để test sort
INSERT INTO `employees`
  (`department_id`, `employee_name`, `employee_name_kana`, `employee_birth_date`,
   `employee_email`, `employee_telephone`, `employee_login_id`, `employee_login_password`, `role`)
VALUES
  (2, 'An',    'アン',   '1990-05-05', 'an1@example.com',   '0911111111', 'user01', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
  (3, 'An',    'アン',   '1992-01-01', 'an2@example.com',   '0911111112', 'user02', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
  (4, 'An',    'アン',   '1995-10-10', 'an3@example.com',   '0911111113', 'user03', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
  (2, 'Bình',  'ビン',   '1988-03-03', 'binh1@example.com', '0922222221', 'user04', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
  (5, 'Bình',  'ビン',   '1993-07-07', 'binh2@example.com', '0922222222', 'user05', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
  (6, 'Cường', 'クオン', '1994-08-25', 'cuong@example.com', '0933333333', 'user06', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0);

-- 4. Tài khoản quản trị (role = 1)
INSERT INTO `employees`
  (`department_id`, `employee_name`, `employee_email`, `employee_login_id`, `employee_login_password`, `role`)
VALUES
  (1, 'Administrator', 'la@luvina.net', 'admin', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 1);

-- 5. employees_certifications (test sort Name -> Level -> End Date)
-- user01: 'An', N1 (level 1), hết hạn 2030
INSERT INTO `employees_certifications` (`employee_id`, `certification_id`, `start_date`, `end_date`, `score`)
SELECT `employee_id`, 1, '2020-01-01', '2030-01-01', 170 FROM `employees` WHERE `employee_login_id` = 'user01';

-- user02: 'An', N1 (level 1), hết hạn 2025 (test ưu tiên End Date ASC)
INSERT INTO `employees_certifications` (`employee_id`, `certification_id`, `start_date`, `end_date`, `score`)
SELECT `employee_id`, 1, '2020-01-01', '2025-01-01', 150 FROM `employees` WHERE `employee_login_id` = 'user02';

-- user03: 'An', N3 (level 3) (test ưu tiên Level ASC)
INSERT INTO `employees_certifications` (`employee_id`, `certification_id`, `start_date`, `end_date`, `score`)
SELECT `employee_id`, 3, '2020-01-01', '2028-01-01', 120 FROM `employees` WHERE `employee_login_id` = 'user03';

-- user04: 'Bình', N2 (level 2)
INSERT INTO `employees_certifications` (`employee_id`, `certification_id`, `start_date`, `end_date`, `score`)
SELECT `employee_id`, 2, '2021-01-01', '2029-01-01', 140 FROM `employees` WHERE `employee_login_id` = 'user04';
