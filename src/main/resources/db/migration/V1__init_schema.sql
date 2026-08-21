-- ============================================================
-- 1. BẢNG PHÒNG BAN (departments)
-- ============================================================
CREATE TABLE IF NOT EXISTS `departments` (
    `department_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `department_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`department_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `departments` (`department_id`, `department_name`) VALUES
(1, 'Phòng phát triển số 1'),
(2, 'Phòng phát triển số 2'),
(3, 'Phòng QA');

-- ============================================================
-- 2. BẢNG NHÂN VIÊN (employees)
-- ============================================================
CREATE TABLE IF NOT EXISTS `employees` (
    `employee_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `department_id` BIGINT(20) NOT NULL,
    `employee_name` VARCHAR(255) NOT NULL,
    `employee_name_kana` VARCHAR(255) DEFAULT NULL,
    `employee_birth_date` DATE DEFAULT NULL,
    `employee_email` VARCHAR(255) NOT NULL,
    `employee_telephone` VARCHAR(50) DEFAULT NULL,
    `employee_login_id` VARCHAR(50) NOT NULL,
    `employee_login_password` VARCHAR(100) DEFAULT NULL,
    `employee_role` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0: user, 1: admin',

    PRIMARY KEY (`employee_id`) USING BTREE,

    KEY `FK_employees_departments` (`department_id`),

    CONSTRAINT `FK_employees_departments`
        FOREIGN KEY (`department_id`)
        REFERENCES `departments` (`department_id`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8;

INSERT INTO `employees` (`employee_id`, `department_id`, `employee_name`, `employee_name_kana`, `employee_birth_date`, `employee_email`, `employee_telephone`, `employee_login_id`, `employee_login_password`, `employee_role`)
VALUES (1, 1, 'Administrator', NULL, NULL, 'la@luvina.net', NULL, 'admin', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 1);

-- ============================================================
-- 3. BẢNG DANH MỤC CHỨNG CHỈ (certifications)
-- ============================================================
CREATE TABLE IF NOT EXISTS `certifications` (
    `certification_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `certification_name` VARCHAR(50) NOT NULL,
    `certification_level` INT NOT NULL,
    PRIMARY KEY (`certification_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES
(1, 'Trình độ tiếng nhật cấp 1', 1),
(2, 'Trình độ tiếng nhật cấp 2', 2),
(3, 'Trình độ tiếng nhật cấp 3', 3),
(4, 'Trình độ tiếng nhật cấp 4', 4),
(5, 'Trình độ tiếng nhật cấp 5', 5);

-- ============================================================
-- 4. BẢNG CHỨNG CHỈ NHÂN VIÊN (employees_certifications)
-- ============================================================
CREATE TABLE IF NOT EXISTS `employees_certifications` (
    `employee_certification_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `employee_id` BIGINT(20) NOT NULL,
    `certification_id` BIGINT(20) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `score` DECIMAL(4, 1) DEFAULT NULL,
    PRIMARY KEY (`employee_certification_id`) USING BTREE,
    KEY `FK_employees_certifications_employees` (`employee_id`),
    KEY `FK_employees_certifications_certifications` (`certification_id`),
    CONSTRAINT `FK_employees_certifications_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`) ON DELETE CASCADE,
    CONSTRAINT `FK_employees_certifications_certifications` FOREIGN KEY (`certification_id`) REFERENCES `certifications` (`certification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `employees_certifications` (`employee_certification_id`, `employee_id`, `certification_id`, `start_date`, `end_date`, `score`) VALUES
(1, 1, 1, '2023-01-01', '2025-01-01', 170.0);