/*
SQLyog  v12.2.6 (64 bit)
MySQL - 5.5.53 : Database - uip
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`uip` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `uip`;

/*Table structure for table `course_evaluation` */

DROP TABLE IF EXISTS `course_evaluation`;

CREATE TABLE `course_evaluation` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `commentator_id` bigint(20) unsigned NOT NULL,
  `course_id` bigint(20) unsigned NOT NULL,
  `content` varchar(516) DEFAULT NULL,
  `score` int(11) DEFAULT '0',
  `info_date` datetime NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `course_id` (`course_id`),
  KEY `commentator_id` (`commentator_id`),
  CONSTRAINT `course_evaluation_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`info_id`),
  CONSTRAINT `course_evaluation_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `course_evaluation` */

/*Table structure for table `courses` */

DROP TABLE IF EXISTS `courses`;

CREATE TABLE `courses` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `university_id` int(10) unsigned NOT NULL,
  `institute_id` int(10) unsigned NOT NULL,
  `course_name` varchar(30) NOT NULL,
  `teacher_name` varchar(20) NOT NULL,
  `course_picture` varchar(255) DEFAULT NULL,
  `info_date` datetime NOT NULL,
  `description` tinytext,
  `average_score` int(11) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `user_id` (`user_id`),
  KEY `university_id` (`university_id`),
  KEY `institute_id` (`institute_id`),
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `courses_ibfk_2` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`),
  CONSTRAINT `courses_ibfk_3` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `courses` */

/*Table structure for table `forum_comments` */

DROP TABLE IF EXISTS `forum_comments`;

CREATE TABLE `forum_comments` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) unsigned NOT NULL,
  `commentator_id` bigint(20) unsigned NOT NULL,
  `content` varchar(516) DEFAULT NULL,
  `report_number` int(11) DEFAULT '0',
  `like_number` int(11) DEFAULT '0',
  `info_date` datetime NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `post_id` (`post_id`),
  KEY `commentator_id` (`commentator_id`),
  CONSTRAINT `forum_comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `forum_posts` (`info_id`),
  CONSTRAINT `forum_comments_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `forum_comments` */

/*Table structure for table `forum_posts` */

DROP TABLE IF EXISTS `forum_posts`;

CREATE TABLE `forum_posts` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `content` tinytext NOT NULL,
  `pictures` varchar(516) DEFAULT NULL,
  `info_date` datetime NOT NULL,
  `poster` bigint(20) unsigned NOT NULL,
  `report_number` int(11) DEFAULT '0',
  `like_number` int(11) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `poster` (`poster`),
  CONSTRAINT `forum_posts_ibfk_1` FOREIGN KEY (`poster`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `forum_posts` */

/*Table structure for table `institute` */

DROP TABLE IF EXISTS `institute`;

CREATE TABLE `institute` (
  `info_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `institute_name` varchar(40) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `institute` */

insert  into `institute`(`info_id`,`institute_name`,`created`) values 
(1,'政治与行政学院','2020-03-22 14:01:23'),
(2,'马克思主义学院','2020-03-22 14:01:23'),
(3,'历史文化学院','2020-03-22 14:01:23'),
(4,'外国语言文化学院','2020-03-22 14:01:23'),
(5,'文学院','2020-03-22 14:01:23'),
(6,'经济与管理学院','2020-03-22 14:01:23'),
(7,'法学院','2020-03-22 14:01:23'),
(8,'公共管理学院','2020-03-22 14:01:23'),
(9,'体育科学院','2020-03-22 14:01:23'),
(10,'音乐学院','2020-03-22 14:01:23'),
(11,'美术学院','2020-03-22 14:01:23'),
(12,'计算机学院','2020-03-22 14:01:23'),
(13,'数学科学学院','2020-03-22 14:01:23'),
(14,'声明科学学院','2020-03-22 14:01:23'),
(15,'旅游管理学院','2020-03-22 14:01:23'),
(16,'信息光电子学院','2020-03-22 14:01:23'),
(17,'物理与电信工程学院','2020-03-22 14:01:23'),
(18,'化学与环境学院','2020-03-22 14:01:23');

/*Table structure for table `recruit` */

DROP TABLE IF EXISTS `recruit`;

CREATE TABLE `recruit` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `content` tinytext NOT NULL,
  `pictures` varchar(516) DEFAULT NULL,
  `info_date` datetime NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `subject_id` int(10) unsigned NOT NULL,
  `university_id` int(10) unsigned NOT NULL,
  `report_number` int(11) DEFAULT '0',
  `contact` tinyint(1) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `user_id` (`user_id`),
  KEY `subject_id` (`subject_id`),
  KEY `university_id` (`university_id`),
  CONSTRAINT `recruit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `recruit_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subject_info` (`info_id`),
  CONSTRAINT `recruit_ibfk_3` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `recruit` */

/*Table structure for table `release_record` */

DROP TABLE IF EXISTS `release_record`;

CREATE TABLE `release_record` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `record_id` bigint(20) NOT NULL,
  `record_type` int(11) NOT NULL,
  `record_date` datetime NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `release_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `release_record` */

/*Table structure for table `resource_type` */

DROP TABLE IF EXISTS `resource_type`;

CREATE TABLE `resource_type` (
  `info_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type_name` varchar(20) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `resource_type` */

insert  into `resource_type`(`info_id`,`type_name`,`created`) values 
(1,'论文、报告模板','2020-03-22 14:01:29'),
(2,'试题','2020-03-22 14:01:29'),
(3,'电子书','2020-03-22 14:01:29'),
(4,'视频课程','2020-03-22 14:01:29'),
(5,'其他','2020-03-22 14:01:29');

/*Table structure for table `resources` */

DROP TABLE IF EXISTS `resources`;

CREATE TABLE `resources` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `description` tinytext NOT NULL,
  `address` varchar(516) DEFAULT NULL,
  `info_date` datetime NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `subject_id` int(10) unsigned NOT NULL,
  `type_id` int(10) unsigned NOT NULL,
  `report_number` int(11) DEFAULT '0',
  `is_anonymous` tinyint(1) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `user_id` (`user_id`),
  KEY `subject_id` (`subject_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `resources_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `resources_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subject_info` (`info_id`),
  CONSTRAINT `resources_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `resource_type` (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `resources` */

/*Table structure for table `stu_certification` */

DROP TABLE IF EXISTS `stu_certification`;

CREATE TABLE `stu_certification` (
  `user_id` bigint(20) NOT NULL,
  `stu_card` varchar(255) DEFAULT NULL,
  `stu_cer` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `stu_certification` */

/*Table structure for table `subject_info` */

DROP TABLE IF EXISTS `subject_info`;

CREATE TABLE `subject_info` (
  `info_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject_name` varchar(40) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `subject_info` */

insert  into `subject_info`(`info_id`,`subject_name`,`created`) values 
(1,'哲学','2020-03-22 14:01:28'),
(2,'经济学','2020-03-22 14:01:28'),
(3,'法学','2020-03-22 14:01:28'),
(4,'教育学','2020-03-22 14:01:28'),
(5,'文学','2020-03-22 14:01:28'),
(6,'历史学','2020-03-22 14:01:28'),
(7,'理学','2020-03-22 14:01:28'),
(8,'工学','2020-03-22 14:01:28'),
(9,'农学','2020-03-22 14:01:28'),
(10,'医学','2020-03-22 14:01:28'),
(11,'军事学','2020-03-22 14:01:28'),
(12,'管理学','2020-03-22 14:01:28'),
(13,'艺术学','2020-03-22 14:01:28');

/*Table structure for table `university` */

DROP TABLE IF EXISTS `university`;

CREATE TABLE `university` (
  `info_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `university_name` varchar(40) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `university` */

insert  into `university`(`info_id`,`university_name`,`created`) values 
(1,'华南师范大学','2020-03-22 14:01:22');

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL,
  `pw` varchar(16) NOT NULL,
  `stu_number` varchar(25) DEFAULT NULL,
  `stu_card` varchar(255) DEFAULT NULL,
  `portrait` varchar(255) DEFAULT NULL,
  `university_id` int(10) unsigned DEFAULT NULL,
  `institute_id` int(10) unsigned DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`,`user_name`),
  KEY `university_id` (`university_id`),
  KEY `institute_id` (`institute_id`),
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`),
  CONSTRAINT `user_info_ibfk_2` FOREIGN KEY (`institute_id`) REFERENCES `institute` (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_info` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
