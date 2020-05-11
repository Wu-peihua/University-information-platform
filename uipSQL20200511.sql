/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : uip

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 11/05/2020 23:33:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `comment_reply`;
CREATE TABLE `comment_reply`  (
  `info_id` bigint unsigned NOT NULL,
  `comment_id` bigint unsigned NOT NULL,
  `commentator_id` bigint unsigned NOT NULL,
  `refer_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `refer_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `report_number` int(0) NULL DEFAULT 0,
  `like_number` int(0) NULL DEFAULT 0,
  `info_date` datetime(0) NULL DEFAULT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `comment_reply_ibfk_1`(`comment_id`) USING BTREE,
  INDEX `comment_reply_ibfk_2`(`commentator_id`) USING BTREE,
  CONSTRAINT `comment_reply_ibfk_1` FOREIGN KEY (`comment_id`) REFERENCES `forum_comments` (`info_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comment_reply_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `course_evaluation`;
CREATE TABLE `course_evaluation`  (
  `info_id` bigint unsigned NOT NULL,
  `commentator_id` bigint unsigned NOT NULL,
  `course_id` bigint unsigned NOT NULL,
  `content` varchar(516) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `score` decimal(2, 1) NULL DEFAULT 0.0,
  `report_number` int(0) NULL DEFAULT 0,
  `like_number` int(0) NULL DEFAULT 0,
  `info_date` datetime(0) NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE,
  INDEX `commentator_id`(`commentator_id`) USING BTREE,
  CONSTRAINT `course_evaluation_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `course_evaluation_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_evaluation
-- ----------------------------
INSERT INTO `course_evaluation` VALUES (1, 7, 2, '咕噜咕噜', 4.0, 0, 0, '2020-04-29 09:07:00', '2020-05-10 19:09:05');
INSERT INTO `course_evaluation` VALUES (2, 8, 1, '可以', 5.0, 0, 0, '2020-04-29 21:07:04', '2020-05-10 19:09:05');
INSERT INTO `course_evaluation` VALUES (3, 9, 2, '啦啦啦', 4.0, 0, 0, '2020-04-29 21:07:04', '2020-05-10 19:09:05');
INSERT INTO `course_evaluation` VALUES (4, 10, 3, '略略略', 4.0, 0, 0, '2020-04-29 21:07:04', '2020-05-10 19:09:05');

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `info_id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `university_id` int unsigned NOT NULL,
  `institute_id` int unsigned NOT NULL,
  `course_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `teacher_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `course_picture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `info_date` datetime(0) NOT NULL,
  `description` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `average_score` decimal(2, 1) NULL DEFAULT 0.0,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `university_id`(`university_id`) USING BTREE,
  INDEX `institute_id`(`institute_id`) USING BTREE,
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `courses_ibfk_2` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `courses_ibfk_3` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, 1, 1, 12, '计算机网络', '唐华', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-04-28 12:12:23', '打开互联网的世界', 0.0, '2020-05-10 19:09:05');
INSERT INTO `courses` VALUES (2, 1, 1, 12, '操作系统', '李丁丁', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-04-28 12:12:24', '熟悉操作系统基本原理', 0.0, '2020-05-10 19:09:05');
INSERT INTO `courses` VALUES (3, 1, 1, 12, '数据结构', '黄煜廉', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-04-28 12:12:25', '数据结构基础', 0.0, '2020-05-10 19:09:05');
INSERT INTO `courses` VALUES (4, 1, 1, 12, '编译原理', '黄煜廉', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-04-28 12:12:25', '编译原理基础', 0.0, '2020-05-10 19:09:05');
INSERT INTO `courses` VALUES (5, 1, 1, 12, 'Linux', '李丁丁', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-04-28 12:12:25', 'Linux实战', 0.0, '2020-05-10 19:09:05');
INSERT INTO `courses` VALUES (6, 1, 1, 12, '数据结构', '李晶晶', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-04-28 12:12:25', '数据结构基础', 0.0, '2020-05-10 19:09:05');

-- ----------------------------
-- Table structure for forum_comments
-- ----------------------------
DROP TABLE IF EXISTS `forum_comments`;
CREATE TABLE `forum_comments`  (
  `info_id` bigint unsigned NOT NULL,
  `post_id` bigint unsigned NOT NULL,
  `commentator_id` bigint unsigned NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `report_number` int(0) NULL DEFAULT 0,
  `like_number` int(0) NULL DEFAULT 0,
  `reply_number` int(0) NULL DEFAULT 0,
  `info_date` datetime(0) NULL DEFAULT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `forum_comments_ibfk_1`(`post_id`) USING BTREE,
  INDEX `forum_comments_ibfk_2`(`commentator_id`) USING BTREE,
  CONSTRAINT `forum_comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `forum_posts` (`info_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `forum_comments_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for forum_posts
-- ----------------------------
DROP TABLE IF EXISTS `forum_posts`;
CREATE TABLE `forum_posts`  (
  `info_id` bigint unsigned NOT NULL,
  `title` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pictures` varchar(10240) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `info_date` datetime(0) NULL DEFAULT NULL,
  `poster` bigint unsigned NOT NULL,
  `report_number` int(0) NULL DEFAULT 0,
  `like_number` int(0) NULL DEFAULT 0,
  `reply_number` int(0) NULL DEFAULT 0,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `forum_posts_ibfk_1`(`poster`) USING BTREE,
  CONSTRAINT `forum_posts_ibfk_1` FOREIGN KEY (`poster`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for institute
-- ----------------------------
DROP TABLE IF EXISTS `institute`;
CREATE TABLE `institute`  (
  `info_id` int unsigned NOT NULL,
  `institute_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of institute
-- ----------------------------
INSERT INTO `institute` VALUES (1, '政治与行政学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (2, '马克思主义学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (3, '历史文化学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (4, '外国语言文化学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (5, '文学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (6, '经济与管理学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (7, '法学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (8, '公共管理学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (9, '体育科学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (10, '音乐学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (11, '美术学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (12, '计算机学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (13, '数学科学学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (14, '生命科学学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (15, '旅游管理学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (16, '信息光电子学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (17, '物理与电信工程学院', '2020-05-10 19:09:03');
INSERT INTO `institute` VALUES (18, '化学与环境学院', '2020-05-10 19:09:03');

-- ----------------------------
-- Table structure for operetion_record
-- ----------------------------
DROP TABLE IF EXISTS `operetion_record`;
CREATE TABLE `operetion_record`  (
  `info_id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `object_id` bigint unsigned NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `operetion_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for recruit
-- ----------------------------
DROP TABLE IF EXISTS `recruit`;
CREATE TABLE `recruit`  (
  `info_id` bigint unsigned NOT NULL,
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pictures` varchar(516) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `info_date` datetime(0) NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `institute_id` int unsigned NOT NULL,
  `university_id` int unsigned NOT NULL,
  `report_number` int(0) NULL DEFAULT 0,
  `contact` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `institute_id`(`institute_id`) USING BTREE,
  INDEX `university_id`(`university_id`) USING BTREE,
  CONSTRAINT `recruit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `recruit_ibfk_2` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `recruit_ibfk_3` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for release_record
-- ----------------------------
DROP TABLE IF EXISTS `release_record`;
CREATE TABLE `release_record`  (
  `info_id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `record_id` bigint(0) NOT NULL,
  `record_type` int(0) NOT NULL,
  `record_date` datetime(0) NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `release_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `info_id` bigint unsigned NOT NULL,
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `address` varchar(516) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `subject_id` int unsigned NOT NULL,
  `type_id` int unsigned NOT NULL,
  `like_number` int(0) NOT NULL,
  `report_number` int(0) NOT NULL DEFAULT 0,
  `is_anonymous` bit(1) NOT NULL DEFAULT b'0',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE,
  INDEX `type_id`(`type_id`) USING BTREE,
  CONSTRAINT `resource_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `resource_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subject_info` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `resource_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `resource_type` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (3, '扯淡', '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈', 'www.baidu.com', 4, 1, 5, 2, 0, b'1', '2020-05-11 21:44:58');
INSERT INTO `resource` VALUES (4, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (65, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (66, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (67, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (68, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (69, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (70, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (76, '搞笑', '啦啦啦啦啦啦啦啦啦', 'www.baidu.com', 4, 1, 5, 0, 0, b'0', '2020-05-11 23:24:12');
INSERT INTO `resource` VALUES (77, '测试', '', 'www.baidu.com', 4, 1, 1, 0, 0, b'1', '2020-05-11 23:25:30');

-- ----------------------------
-- Table structure for resource_type
-- ----------------------------
DROP TABLE IF EXISTS `resource_type`;
CREATE TABLE `resource_type`  (
  `info_id` int unsigned NOT NULL,
  `type_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource_type
-- ----------------------------
INSERT INTO `resource_type` VALUES (1, '论文、报告模板', '2020-05-10 19:09:05');
INSERT INTO `resource_type` VALUES (2, '试题', '2020-05-10 19:09:05');
INSERT INTO `resource_type` VALUES (3, '电子书', '2020-05-10 19:09:05');
INSERT INTO `resource_type` VALUES (4, '视频课程', '2020-05-10 19:09:05');
INSERT INTO `resource_type` VALUES (5, '其他', '2020-05-10 19:09:05');

-- ----------------------------
-- Table structure for stu_certification
-- ----------------------------
DROP TABLE IF EXISTS `stu_certification`;
CREATE TABLE `stu_certification`  (
  `info_id` bigint unsigned NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `stu_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stu_number` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `institude_id` int unsigned NULL,
  `university_id` int unsigned NULL,
  `stu_card` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stu_cer` int(0) NOT NULL DEFAULT 0,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `institude_id`(`institude_id`) USING BTREE,
  INDEX `university_id`(`university_id`) USING BTREE,
  CONSTRAINT `stu_certification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `stu_certification_ibfk_2` FOREIGN KEY (`institude_id`) REFERENCES `institute` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `stu_certification_ibfk_3` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for subject_info
-- ----------------------------
DROP TABLE IF EXISTS `subject_info`;
CREATE TABLE `subject_info`  (
  `info_id` int unsigned NOT NULL,
  `subject_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject_info
-- ----------------------------
INSERT INTO `subject_info` VALUES (1, '哲学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (2, '经济学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (3, '法学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (4, '教育学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (5, '文学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (6, '历史学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (7, '理学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (8, '工学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (9, '农学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (10, '医学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (11, '军事学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (12, '管理学', '2020-05-10 19:09:05');
INSERT INTO `subject_info` VALUES (13, '艺术学', '2020-05-10 19:09:05');

-- ----------------------------
-- Table structure for university
-- ----------------------------
DROP TABLE IF EXISTS `university`;
CREATE TABLE `university`  (
  `info_id` int unsigned NOT NULL,
  `university_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of university
-- ----------------------------
INSERT INTO `university` VALUES (1, '华南师范大学', '2020-05-10 19:09:03');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` bigint unsigned NOT NULL,
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pw` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `stu_number` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stu_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `portrait` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `university_id` int unsigned NULL,
  `institute_id` int unsigned NULL,
  `user_type` int unsigned NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`, `user_name`) USING BTREE,
  INDEX `university_id`(`university_id`) USING BTREE,
  INDEX `institute_id`(`institute_id`) USING BTREE,
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_info_ibfk_2` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`info_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, '张三', '123456', '', NULL, 'http://b-ssl.duitang.com/uploads/item/201511/21/20151121171107_zMZcy.jpeg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info` VALUES (2, '李四', '123456', NULL, NULL, 'http://img2.imgtn.bdimg.com/it/u=1311392388,1324290206&fm=26&gp=0.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info` VALUES (3, '王五', '123456', NULL, NULL, 'http://b-ssl.duitang.com/uploads/item/201803/30/20180330234706_stfoo.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info` VALUES (4, '悟空', '123456', NULL, NULL, 'http://pic4.zhimg.com/50/v2-6ecab2cd6c1bbf9835030682db83543d_hd.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info` VALUES (5, '大熊', '123456', NULL, NULL, 'http://img1.imgtn.bdimg.com/it/u=2156833431,1671740038&fm=26&gp=0.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info` VALUES (6, '丸子', '123456', NULL, NULL, 'http://img2.imgtn.bdimg.com/it/u=2421351453,1852348871&fm=26&gp=0.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info` VALUES (7, '李玉', '123456', '', NULL, 'http://b-ssl.duitang.com/uploads/item/201803/30/20180330234706_stfoo.jpg', 1, 12, 1, '2020-04-28 20:27:37');
INSERT INTO `user_info` VALUES (8, '李周', '123456', '', NULL, 'http://img1.imgtn.bdimg.com/it/u=2156833431,1671740038&fm=26&gp=0.jpg', 1, 10, 1, '2020-04-28 20:27:39');
INSERT INTO `user_info` VALUES (9, '王余', '123456', '', NULL, 'http://b-ssl.duitang.com/uploads/item/201803/30/20180330234706_stfoo.jpg', 1, 10, 1, '2020-04-28 20:27:39');
INSERT INTO `user_info` VALUES (10, '王诗', '123456', '', NULL, 'http://img2.imgtn.bdimg.com/it/u=1311392388,1324290206&fm=26&gp=0.jpg', 1, 10, 1, '2020-04-28 20:27:39');

SET FOREIGN_KEY_CHECKS = 1;
