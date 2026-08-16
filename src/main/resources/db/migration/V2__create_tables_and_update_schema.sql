-- 1. Create table departments
CREATE TABLE IF NOT EXISTS `departments` (
    `department_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `department_name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert sample departments
INSERT INTO `departments` (`department_id`, `department_name`) VALUES 
(1, 'Phòng Phát triển'),
(2, 'Phòng Kiểm thử'),
(3, 'Phòng Kinh doanh'),
(4, 'Phòng Nhân sự')
ON DUPLICATE KEY UPDATE `department_name` = VALUES(`department_name`);

-- 2. Create table certifications
CREATE TABLE IF NOT EXISTS `certifications` (
    `certification_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `certification_name` VARCHAR(50) NOT NULL,
    `certification_level` INT NOT NULL COMMENT 'Giá trị càng nhỏ thì trình độ càng cao',
    PRIMARY KEY (`certification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert 5 Japanese certification levels
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES
(1, 'Trình độ tiếng nhật cấp 1', 1),
(2, 'Trình độ tiếng nhật cấp 2', 2),
(3, 'Trình độ tiếng nhật cấp 3', 3),
(4, 'Trình độ tiếng nhật cấp 4', 4),
(5, 'Trình độ tiếng nhật cấp 5', 5)
ON DUPLICATE KEY UPDATE `certification_name` = VALUES(`certification_name`), `certification_level` = VALUES(`certification_level`);

-- 3. Update table employees (add missing fields, role and modify length)
ALTER TABLE `employees` 
    MODIFY COLUMN `employee_name` VARCHAR(255) NOT NULL,
    MODIFY COLUMN `employee_email` VARCHAR(255) NOT NULL,
    ADD COLUMN IF NOT EXISTS `employee_name_kana` VARCHAR(255) NULL AFTER `employee_name`,
    ADD COLUMN IF NOT EXISTS `employee_birth_date` DATE NULL AFTER `employee_name_kana`,
    ADD COLUMN IF NOT EXISTS `employee_telephone` VARCHAR(50) NULL AFTER `employee_email`,
    ADD COLUMN IF NOT EXISTS `role` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0: User, 1: Admin';

-- Set admin account role to 1 (Admin)
UPDATE `employees` SET `role` = 1 WHERE `employee_login_id` = 'admin';

-- Add foreign key constraint for employees.department_id -> departments.department_id
ALTER TABLE `employees`
    ADD CONSTRAINT `fk_employees_departments`
    FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE;

-- 4. Create table employees_certifications
CREATE TABLE IF NOT EXISTS `employees_certifications` (
    `employee_certification_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `employee_id` BIGINT(20) NOT NULL,
    `certification_id` BIGINT(20) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `score` DECIMAL(5,2) NOT NULL,
    PRIMARY KEY (`employee_certification_id`),
    CONSTRAINT `fk_emp_cert_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_emp_cert_certifications` FOREIGN KEY (`certification_id`) REFERENCES `certifications` (`certification_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
