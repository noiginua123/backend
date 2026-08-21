-- ============================================================
-- SEED DATA FOR TESTING ADM002 & PROJECT MODULES
-- ============================================================

-- 1. THÊM DANH SÁCH NHÂN VIÊN MẪU (employees)
INSERT INTO `employees` (`employee_id`, `department_id`, `employee_name`, `employee_name_kana`, `employee_birth_date`, `employee_email`, `employee_telephone`, `employee_login_id`, `employee_login_password`, `employee_role`) VALUES
(2, 1, 'Nguyễn Thị Mai Hương', 'グエン ティ マイ フオン', '1983-07-08', 'ntmhuong@luvina.net', '0914326386', 'huongntm', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(3, 1, 'Lê Thị Xoa', 'レ ティ ソア', '1983-07-08', 'xoalt@luvina.net', '1234567894', 'xoalt', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(4, 3, 'Đặng Thị Hân', 'ダン ティ ハン', '1983-07-08', 'handt@luvina.net', '0914326386', 'handt', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(5, 1, 'Lê Nghiêm Thủy', 'レ ギエム トゥイ', '1983-07-08', 'thuyln@luvina.net', '1234567894', 'thuyln', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(6, 1, 'Lê Phương Anh', 'レ フオン アイン', '1983-07-08', 'anhlp@luvina.net', '1234567894', 'anhlp', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(7, 2, 'Trần Văn Bình', 'チャン ヴァン ビン', '1990-02-14', 'binhtv@luvina.net', '0901234567', 'binhtv', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(8, 2, 'Hoàng Thị Dung', 'ホアン ティ ズン', '1995-11-20', 'dunghth@luvina.net', '0988776655', 'dunghth', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(9, 3, 'Phạm Minh Đức', 'ファム ミン ドゥック', '1992-05-18', 'ducpm@luvina.net', '0977112233', 'ducpm', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(10, 1, 'Vũ Hải Yến', 'ヴー ハイ イエン', '1997-09-09', 'yenvh@luvina.net', '0933445566', 'yenvh', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(11, 2, 'Đỗ Quốc Tuấn', 'ドー クオック トゥアン', '1991-12-25', 'tuandq@luvina.net', '0966554433', 'tuandq', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0)
ON DUPLICATE KEY UPDATE `employee_name` = VALUES(`employee_name`);

-- 2. THÊM CHỨNG CHỈ CHO NHÂN VIÊN (employees_certifications)
INSERT INTO `employees_certifications` (`employee_certification_id`, `employee_id`, `certification_id`, `start_date`, `end_date`, `score`) VALUES
(2, 2, 4, '2010-07-08', '2011-07-08', 290.0),
(3, 3, 4, '2010-07-08', '2011-07-08', 290.0),
(4, 4, 4, '2010-07-08', '2011-07-08', 290.0),
(5, 5, 4, '2010-07-08', '2011-07-08', 290.0),
(6, 6, 4, '2010-07-08', '2011-07-08', 290.0),
(7, 7, 2, '2020-01-01', '2024-01-01', 160.0),
(8, 8, 3, '2021-05-15', '2025-05-15', 145.0),
(9, 10, 1, '2022-07-01', '2026-07-01', 175.0),
(10, 11, 2, '2019-12-01', '2023-12-01', 155.0)
ON DUPLICATE KEY UPDATE `score` = VALUES(`score`);
