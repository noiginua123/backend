SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `department_id` bigint NOT NULL AUTO_INCREMENT,
  `department_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` (`department_id`, `department_name`) VALUES
(1, 'DEV1'),
(2, 'DEV2'),
(3, 'DEV3'),
(4, 'DEV4'),
(5, 'DEV5');

-- ----------------------------
-- Table structure for certifications
-- ----------------------------
DROP TABLE IF EXISTS `certifications`;
CREATE TABLE `certifications`  (
  `certification_id` bigint NOT NULL AUTO_INCREMENT,
  `certification_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `certification_level` int NOT NULL,
  PRIMARY KEY (`certification_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of certifications
-- ----------------------------
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES
(1, 'Trình độ tiếng nhật cấp 1', 1),
(2, 'Trình độ tiếng nhật cấp 2', 2),
(3, 'Trình độ tiếng nhật cấp 3', 3),
(4, 'Trình độ tiếng nhật cấp 4', 4),
(5, 'Trình độ tiếng nhật cấp 5', 5);

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`  (
  `employee_id` bigint NOT NULL AUTO_INCREMENT,
  `department_id` bigint NOT NULL,
  `employee_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `employee_name_kana` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `employee_birth_date` date NULL DEFAULT NULL,
  `employee_email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `employee_telephone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `employee_login_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `employee_login_password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `employee_role` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: user, 1: admin',
  PRIMARY KEY (`employee_id`) USING BTREE,
  UNIQUE INDEX `UK_employees_employee_login_id`(`employee_login_id` ASC) USING BTREE,
  INDEX `FK_employees_departments`(`department_id` ASC) USING BTREE,
  CONSTRAINT `FK_employees_departments` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employees
-- ----------------------------
INSERT INTO `employees` (`employee_id`, `department_id`, `employee_name`, `employee_name_kana`, `employee_birth_date`, `employee_email`, `employee_telephone`, `employee_login_id`, `employee_login_password`, `employee_role`)
VALUES (1, 1, 'Administrator', NULL, NULL, 'la@luvina.net', NULL, 'admin', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 1);

-- ----------------------------
-- Table structure for employees_certifications
-- ----------------------------
DROP TABLE IF EXISTS `employees_certifications`;
CREATE TABLE `employees_certifications`  (
  `employee_certification_id` bigint NOT NULL AUTO_INCREMENT,
  `employee_id` bigint NOT NULL,
  `certification_id` bigint NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `score` decimal(4, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`employee_certification_id`) USING BTREE,
  INDEX `FK_employees_certifications_employees`(`employee_id` ASC) USING BTREE,
  INDEX `FK_employees_certifications_certifications`(`certification_id` ASC) USING BTREE,
  CONSTRAINT `FK_employees_certifications_certifications` FOREIGN KEY (`certification_id`) REFERENCES `certifications` (`certification_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_employees_certifications_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 166 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employees_certifications
-- ----------------------------
INSERT INTO `employees_certifications` (`employee_certification_id`, `employee_id`, `certification_id`, `start_date`, `end_date`, `score`) VALUES
(1, 1, 1, '2023-01-01', '2025-01-01', 170.0);

SET FOREIGN_KEY_CHECKS = 1;