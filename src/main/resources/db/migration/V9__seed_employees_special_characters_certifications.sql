-- ============================================================
-- V9__seed_employees_special_characters_certifications.sql
-- Thêm chứng chỉ (employees_certifications) cho 6 nhân viên test ký tự đặc biệt (id 143 -> 148)
-- ============================================================

INSERT INTO `employees_certifications` (
    `employee_certification_id`,
    `employee_id`,
    `certification_id`,
    `start_date`,
    `end_date`,
    `score`
) VALUES
(158, 143, 1, '2023-01-01', '2027-01-01', 175.0),
(159, 144, 2, '2023-02-01', '2027-02-01', 160.0),
(160, 145, 3, '2023-03-01', '2027-03-01', 150.0),
(161, 146, 1, '2023-04-01', '2027-04-01', 170.0),
(162, 147, 2, '2023-05-01', '2027-05-01', 165.0),
(163, 148, 3, '2023-06-01', '2027-06-01', 155.0)
ON DUPLICATE KEY UPDATE
    `certification_id` = VALUES(`certification_id`),
    `start_date` = VALUES(`start_date`),
    `end_date` = VALUES(`end_date`),
    `score` = VALUES(`score`);
