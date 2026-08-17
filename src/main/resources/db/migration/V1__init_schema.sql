-- 1. departments
CREATE TABLE `departments` (
  `department_id`   BIGINT       NOT NULL AUTO_INCREMENT,
  `department_name` VARCHAR(50)  NOT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. certifications (level càng nhỏ => trình độ càng cao)
CREATE TABLE `certifications` (
  `certification_id`    BIGINT      NOT NULL AUTO_INCREMENT,
  `certification_name`  VARCHAR(50) NOT NULL,
  `certification_level` INT         NOT NULL COMMENT 'Giá trị càng nhỏ thì trình độ càng cao',
  PRIMARY KEY (`certification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. employees
CREATE TABLE `employees` (
  `employee_id`             BIGINT       NOT NULL AUTO_INCREMENT,
  `department_id`           BIGINT       NOT NULL,
  `employee_name`           VARCHAR(255) NOT NULL,
  `employee_name_kana`      VARCHAR(255) NULL,
  `employee_birth_date`     DATE         NULL,
  `employee_email`          VARCHAR(255) NOT NULL,
  `employee_telephone`      VARCHAR(50)  NULL,
  `employee_login_id`       VARCHAR(50)  NOT NULL,
  `employee_login_password` VARCHAR(100) NOT NULL,
  `role`                    TINYINT      NOT NULL DEFAULT 0 COMMENT '0: User, 1: Admin',
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `uq_employee_login_id` (`employee_login_id`),
  KEY `idx_employee_department` (`department_id`),
  CONSTRAINT `fk_employees_departments`
    FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. employees_certifications
CREATE TABLE `employees_certifications` (
  `employee_certification_id` BIGINT       NOT NULL AUTO_INCREMENT,
  `employee_id`               BIGINT       NOT NULL,
  `certification_id`          BIGINT       NOT NULL,
  `start_date`                DATE         NOT NULL,
  `end_date`                  DATE         NOT NULL,
  `score`                     DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`employee_certification_id`),
  KEY `idx_empcert_employee` (`employee_id`),
  KEY `idx_empcert_certification` (`certification_id`),
  CONSTRAINT `fk_empcert_employees`
    FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_empcert_certifications`
    FOREIGN KEY (`certification_id`) REFERENCES `certifications` (`certification_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
