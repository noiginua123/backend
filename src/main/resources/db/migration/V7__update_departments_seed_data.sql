-- -- ============================================================
-- -- V7__update_departments_seed_data.sql
-- -- Cập nhật lại seed data danh sách phòng ban: DEV1, DEV2, DEV3, DEV4, DEV5
-- -- ============================================================

-- -- 1. Chuyển bất kỳ nhân viên nào thuộc phòng ban > 5 về phòng ban 5 (DEV5) để tránh lỗi ràng buộc khóa ngoại
-- UPDATE `employees` SET `department_id` = 5 WHERE `department_id` > 5;

-- -- 2. Xóa các phòng ban có ID > 5 nếu có trong cơ sở dữ liệu
-- DELETE FROM `departments` WHERE `department_id` > 5;

-- -- 3. Cập nhật và thêm mới 5 phòng ban DEV1 -> DEV5
-- INSERT INTO `departments` (`department_id`, `department_name`) VALUES
-- (1, 'DEV1'),
-- (2, 'DEV2'),
-- (3, 'DEV3'),
-- (4, 'DEV4'),
-- (5, 'DEV5')
-- ON DUPLICATE KEY UPDATE `department_name` = VALUES(`department_name`);
