DROP DATABASE IF EXISTS uip;      #university information platform的缩写
CREATE DATABASE uip;
USE uip;


# 高校信息
CREATE TABLE university(
    info_id INT UNSIGNED NOT NULL AUTO_INCREMENT,#主键
    university_name VARCHAR(40) NOT NULL,   #高校名，限制最长40个字符
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO university(university_name)
VALUES("华南师范大学");



# 学院信息
CREATE TABLE institute(
    info_id INT UNSIGNED NOT NULL AUTO_INCREMENT,#主键
    institute_name VARCHAR(40) NOT NULL,   #学院名，限制最长40个字符
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO institute(institute_name)
VALUES("政治与行政学院"),("马克思主义学院"),("历史文化学院"),("外国语言文化学院"),("文学院"),("经济与管理学院"),
    ("法学院"),("公共管理学院"),("体育科学院"),("音乐学院"),("美术学院"),("计算机学院"),
    ("数学科学学院"),("生命科学学院"),("旅游管理学院"),("信息光电子学院"),("物理与电信工程学院"),("化学与环境学院");



# 所有人信息
CREATE TABLE user_info(
    user_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,#主键
    user_name VARCHAR(20) NOT NULL,   #用户名，唯一,30个字符长度
    pw VARCHAR(16) NOT NULL,     #登录密码,16个字符长度
    stu_number VARCHAR(25) DEFAULT NULL,#学生学号,25个字符长度
    stu_card VARCHAR(255) DEFAULT NULL,   #学生证url
    portrait VARCHAR(255) DEFAULT NULL,   #头像url
    university_id INT UNSIGNED DEFAULT NULL, #高校id
    institute_id INT UNSIGNED DEFAULT NULL,#学院id
    user_type INT UNSIGNED DEFAULT 0,   #用户身份类型，默认为0,0为普通用户，1为学生用户，2位高校管理员
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, user_name),
    FOREIGN KEY (university_id) REFERENCES university(info_id),    
    FOREIGN KEY (institute_id) REFERENCES institute(info_id),
    PRIMARY KEY(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `user_info`(`user_id`, `user_name`, `pw`, `stu_number`, `stu_card`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES (1, '张三', '123456', NULL, NULL, 'http://b-ssl.duitang.com/uploads/item/201511/21/20151121171107_zMZcy.jpeg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info`(`user_id`, `user_name`, `pw`, `stu_number`, `stu_card`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES (2, '李四', '123456', NULL, NULL, 'http://img2.imgtn.bdimg.com/it/u=1311392388,1324290206&fm=26&gp=0.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info`(`user_id`, `user_name`, `pw`, `stu_number`, `stu_card`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES (3, '王五', '123456', NULL, NULL, 'http://b-ssl.duitang.com/uploads/item/201803/30/20180330234706_stfoo.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info`(`user_id`, `user_name`, `pw`, `stu_number`, `stu_card`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES (4, '悟空', '123456', NULL, NULL, 'http://pic4.zhimg.com/50/v2-6ecab2cd6c1bbf9835030682db83543d_hd.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info`(`user_id`, `user_name`, `pw`, `stu_number`, `stu_card`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES (5, '大熊', '123456', NULL, NULL, 'http://img1.imgtn.bdimg.com/it/u=2156833431,1671740038&fm=26&gp=0.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');
INSERT INTO `user_info`(`user_id`, `user_name`, `pw`, `stu_number`, `stu_card`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES (6, '丸子', '123456', NULL, NULL, 'http://img2.imgtn.bdimg.com/it/u=2421351453,1852348871&fm=26&gp=0.jpg', NULL, NULL, 0, '2020-04-27 23:26:37');


#学生用户信息
INSERT INTO `uip`.`user_info` (`user_name`, `pw`, `stu_number`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES ('李玉', '123456', '', 'http://b-ssl.duitang.com/uploads/item/201803/30/20180330234706_stfoo.jpg', '1', '12', '1', '2020-04-28 20:27:37');
UPDATE `uip`.`user_info` SET `stu_number`='' WHERE `user_id`='1';

INSERT INTO `uip`.`user_info` (`user_name`, `pw`, `stu_number`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES ('李周', '123456', '', 'http://img1.imgtn.bdimg.com/it/u=2156833431,1671740038&fm=26&gp=0.jpg', '1', '10', '1', '2020-04-28 20:27:39');
UPDATE `uip`.`user_info` SET `stu_number`='' WHERE `user_id`='1';

INSERT INTO `uip`.`user_info` (`user_name`, `pw`, `stu_number`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES ('王余', '123456', '', 'http://b-ssl.duitang.com/uploads/item/201803/30/20180330234706_stfoo.jpg', '1', '10', '1', '2020-04-28 20:27:39');
UPDATE `uip`.`user_info` SET `stu_number`='' WHERE `user_id`='1';

INSERT INTO `uip`.`user_info` (`user_name`, `pw`, `stu_number`, `portrait`, `university_id`, `institute_id`, `user_type`, `created`) VALUES ('王诗', '123456', '', 'http://img2.imgtn.bdimg.com/it/u=1311392388,1324290206&fm=26&gp=0.jpg', '1', '10', '1', '2020-04-28 20:27:39');
UPDATE `uip`.`user_info` SET `stu_number`='' WHERE `user_id`='1';



#论坛帖子
CREATE TABLE `forum_posts` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` tinytext NOT NULL,
  `content` text NOT NULL,
  `pictures` varchar(10240) DEFAULT NULL,
  `info_date` datetime DEFAULT NULL,
  `poster` bigint(20) unsigned NOT NULL,
  `report_number` int(11) DEFAULT '0',
  `like_number` int(11) DEFAULT '0',
  `reply_number` int(11) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `forum_posts_ibfk_1` (`poster`),
  CONSTRAINT `forum_posts_ibfk_1` FOREIGN KEY (`poster`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#帖子评论
CREATE TABLE `forum_comments` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) unsigned NOT NULL,
  `commentator_id` bigint(20) unsigned NOT NULL,
  `content` text,
  `report_number` int(11) DEFAULT '0',
  `like_number` int(11) DEFAULT '0',
  `reply_number` int(11) DEFAULT '0',
  `info_date` datetime DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `forum_comments_ibfk_1` (`post_id`),
  KEY `forum_comments_ibfk_2` (`commentator_id`),
  CONSTRAINT `forum_comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `forum_posts` (`info_id`) ON DELETE CASCADE,
  CONSTRAINT `forum_comments_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE DEFINER=`root`@`localhost` TRIGGER `increase_comment` AFTER INSERT ON `forum_comments` 
    FOR EACH ROW 
begin
    update forum_posts
    set reply_number = reply_number + 1
    where info_id = new.post_id;
end;

CREATE DEFINER=`root`@`localhost` TRIGGER `decrease_comment` BEFORE DELETE ON `forum_comments` 
    FOR EACH ROW 
begin
    update forum_posts
    set reply_number = reply_number - 1
    where info_id = old.post_id;
end;



#评论回复
CREATE TABLE `comment_reply` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `comment_id` bigint(20) unsigned NOT NULL,
  `commentator_id` bigint(20) unsigned NOT NULL,
  `refer_user` varchar(20) DEFAULT NULL,
  `refer_content` varchar(512) DEFAULT NULL,
  `content` text,
  `report_number` int(11) DEFAULT '0',
  `like_number` int(11) DEFAULT '0',
  `info_date` datetime DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `comment_reply_ibfk_1` (`comment_id`),
  KEY `comment_reply_ibfk_2` (`commentator_id`),
  CONSTRAINT `comment_reply_ibfk_1` FOREIGN KEY (`comment_id`) REFERENCES `forum_comments` (`info_id`) ON DELETE CASCADE,
  CONSTRAINT `comment_reply_ibfk_2` FOREIGN KEY (`commentator_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE DEFINER=`root`@`localhost` TRIGGER `increase_reply` AFTER INSERT ON `comment_reply` 
    FOR EACH ROW begin
    update forum_comments
    set reply_number = reply_number + 1
    where info_id = new.comment_id;
end;

CREATE DEFINER=`root`@`localhost` TRIGGER `decrease_reply` BEFORE DELETE ON `comment_reply` 
    FOR EACH ROW begin
    update forum_comments
    set reply_number = reply_number - 1
    where info_id = old.comment_id;
end;



#用户点赞举报记录
CREATE TABLE operation_record(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    user_id BIGINT(20) UNSIGNED NOT NULL,# 操作者
    object_id BIGINT(20) UNSIGNED NOT NULL,# 帖子、评论的id
    tag int(2) UNSIGNED NOT NULL,# 1: 点赞，2: 举报
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE DEFINER=`root`@`localhost` TRIGGER `increase_like` AFTER INSERT ON `operation_record` 
    FOR EACH ROW 
begin
    update forum_posts 
    set like_number = like_number + 1 where info_id = new.object_id;
    update forum_comments
    set like_number = like_number + 1 where info_id = new.object_id;
    update comment_reply
    set like_number = like_number + 1 where info_id = new.object_id;
    update forum_posts 
    set report_number = report_number + 1 where info_id = new.object_id;
    update forum_comments
    set report_number = report_number + 1 where info_id = new.object_id;
    update comment_reply
    set report_number = report_number + 1 where info_id = new.object_id;
end;

CREATE DEFINER=`root`@`localhost` TRIGGER `decrease_like` BEFORE DELETE ON `operation_record` 
    FOR EACH ROW 
begin
    update forum_posts 
    set like_number = like_number - 1 where info_id = old.object_id;
    update forum_comments
    set like_number = like_number - 1 where info_id = old.object_id;
    update comment_reply
    set like_number = like_number - 1 where info_id = old.object_id;
end;



# 平台所有学科信息
CREATE TABLE subject_info(
    info_id INT UNSIGNED NOT NULL AUTO_INCREMENT,#主键
    subject_name VARCHAR(40) NOT NULL,   #学科名，限制最长40个字符
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO subject_info(subject_name)
VALUES("哲学"),("经济学"),("法学"),("教育学"),("文学"),("历史学"),("理学"),("工学"),("农学"),("医学"),("军事学"),("管理学"),("艺术学");



# 资源类型信息
CREATE TABLE resource_type(
    info_id INT UNSIGNED NOT NULL AUTO_INCREMENT,#主键
    type_name VARCHAR(20) NOT NULL,   #资源类型名，限制最长20个字符
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO resource_type(type_name)
VALUES("论文、报告模板"),("试题"),("电子书"),("视频课程"),("其他");



# 资源信息
CREATE TABLE `resource` (
  `info_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `description` text ,
  `address` varchar(516) NOT NULL,
  `user_id` bigint unsigned NOT NULL,
  `subject_id` int unsigned NOT NULL,
  `type_id` int unsigned NOT NULL,
  `like_number` int NOT NULL,
  `report_number` int NOT NULL DEFAULT '0',
  `is_anonymous` bit(1) NOT NULL DEFAULT b'0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `user_id` (`user_id`),
  KEY `subject_id` (`subject_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `resource_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `resource_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subject_info` (`info_id`),
  CONSTRAINT `resource_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `resource_type` (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `resource` VALUES (1, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (2, '扯淡', '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈', 'www.baidu.com', 4, 1, 5, 2, 0, b'1', '2020-05-11 21:44:58');
INSERT INTO `resource` VALUES (3, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (4, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (5, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (6, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (7, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (8, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (9, '百度云盘破解版', '自己到百度上面找', 'www.baidu.com', 1, 1, 1, 5, 0, b'0', '2020-05-11 18:58:17');
INSERT INTO `resource` VALUES (10, '搞笑', '啦啦啦啦啦啦啦啦啦', 'www.baidu.com', 4, 1, 5, 0, 0, b'0', '2020-05-11 23:24:12');
INSERT INTO `resource` VALUES (11, '测试', '', 'www.baidu.com', 4, 1, 1, 0, 0, b'1', '2020-05-11 23:25:30');



# 组队信息
CREATE TABLE recruit(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    title VARCHAR(20) NOT NULL, # 组队信息标题
    content TINYTEXT NOT NULL,# 组队信息描述内容
    pictures VARCHAR(516) DEFAULT NULL,   #信息图片
    info_date DATETIME NOT NULL , #组队信息发布日期
    user_id BIGINT(20) UNSIGNED NOT NULL,# 发布者id
    institute_id INT UNSIGNED NOT NULL,# 组队所属学院id
    university_id INT UNSIGNED NOT NULL,  #组队所属学校id
    report_number INT DEFAULT 0,# 信息被举报次数
    contact VARCHAR(40) NOT NULL, #联系方式
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (institute_id) REFERENCES institute(info_id),
    FOREIGN KEY (university_id) REFERENCES university(info_id),
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;



# 课程信息
CREATE TABLE courses(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    user_id BIGINT(20) UNSIGNED NOT NULL, # 发布者id
    university_id INT UNSIGNED NOT NULL,# 课程所属高校id
    institute_id INT UNSIGNED NOT NULL,   #课程所属学院id
    course_name VARCHAR(30) NOT NULL , #课程名，限制最长30个字符
    teacher_name VARCHAR(20) NOT NULL,# 教师名，限制最长20个字符
    course_picture VARCHAR(255) DEFAULT NULL,# 课程封面图片url
    info_date DATETIME NOT NULL,  #课程发布日期
    description TINYTEXT DEFAULT NULL,# 课程描述
    average_score DECIMAL(2,1) DEFAULT 0, #课程平均分
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (university_id) REFERENCES university(info_id),
    FOREIGN KEY (institute_id) REFERENCES institute(info_id),
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `uip`.`courses` (`user_id`, `university_id`, `institute_id`, `course_name`, `teacher_name`, `course_picture`, `info_date`, `description`, `average_score`) VALUES ('1', '1', '12', '计算机网络', '唐华', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-4-28 12:12:23', '打开互联网的世界', '0');
INSERT INTO `uip`.`courses` (`user_id`, `university_id`, `institute_id`, `course_name`, `teacher_name`, `course_picture`, `info_date`, `description`, `average_score`) VALUES ('1', '1', '12', '操作系统', '李丁丁', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-4-28 12:12:24', '熟悉操作系统基本原理', '0');
INSERT INTO `uip`.`courses` (`user_id`, `university_id`, `institute_id`, `course_name`, `teacher_name`, `course_picture`, `info_date`, `description`, `average_score`) VALUES ('1', '1', '12', '数据结构', '黄煜廉', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-4-28 12:12:25', '数据结构基础', '0');
INSERT INTO `uip`.`courses` (`user_id`, `university_id`, `institute_id`, `course_name`, `teacher_name`, `course_picture`, `info_date`, `description`, `average_score`) VALUES ('1', '1', '12', '编译原理', '黄煜廉', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-4-28 12:12:25', '编译原理基础', '0');
INSERT INTO `uip`.`courses` (`user_id`, `university_id`, `institute_id`, `course_name`, `teacher_name`, `course_picture`, `info_date`, `description`, `average_score`) VALUES ('1', '1', '12', 'Linux', '李丁丁', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-4-28 12:12:25', 'Linux实战', '0');
INSERT INTO `uip`.`courses` (`user_id`, `university_id`, `institute_id`, `course_name`, `teacher_name`, `course_picture`, `info_date`, `description`, `average_score`) VALUES ('1', '1', '12', '数据结构', '李晶晶', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg', '2020-4-28 12:12:25', '数据结构基础', '0');



# 课程评论信息
CREATE TABLE course_evaluation(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    commentator_id BIGINT(20) UNSIGNED NOT NULL, # 评论人id
    course_id BIGINT(20) UNSIGNED NOT NULL,# 课程id
    content VARCHAR(516) DEFAULT NULL,   #评论内容，限制516个字符
    score DECIMAL(2,1) DEFAULT 0,# 评分
    report_number INT DEFAULT 0,# 被举报次数
    like_number INT DEFAULT 0,# 点赞次数
    info_date DATETIME NOT NULL,# 评论日期 
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (course_id) REFERENCES courses(info_id),
    FOREIGN KEY (commentator_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `uip`.`course_evaluation` (`commentator_id`, `course_id`, `content`, `score`, `info_date`) VALUES ('7', '2', '咕噜咕噜', '4', '2020-04-29 09:07:00');
INSERT INTO `uip`.`course_evaluation` (`commentator_id`, `course_id`, `content`, `score`, `info_date`) VALUES ('8', '1', '可以', '5', '2020-04-29 21:07:04');
INSERT INTO `uip`.`course_evaluation` (`commentator_id`, `course_id`, `content`, `score`, `info_date`) VALUES ('9', '2', '啦啦啦', '4', '2020-04-29 21:07:04');
INSERT INTO `uip`.`course_evaluation` (`commentator_id`, `course_id`, `content`, `score`, `info_date`) VALUES ('10', '3', '略略略', '4', '2020-04-29 21:07:04');



# 个人发布记录
CREATE TABLE release_record(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    user_id BIGINT(20) UNSIGNED NOT NULL, # 用户id
    record_id BIGINT(20) NOT NULL,# 指向的记录id（相当于外键）
    record_type INT NOT NULL,   #0,1,2对应为帖子、资源、组队类型
    record_date DATETIME NOT NULL,   #记录生成日期
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;



#学生身份认证
CREATE TABLE `stu_certification` (
  `info_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `stu_name` varchar(20) DEFAULT NULL,
  `stu_number` varchar(25) DEFAULT NULL,
  `institude_id` int(10) unsigned DEFAULT NULL,
  `university_id` int(10) unsigned DEFAULT NULL,
  `stu_card` varchar(1024) DEFAULT NULL,
  `stu_cer` int(11) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`info_id`),
  KEY `user_id` (`user_id`),
  KEY `institude_id` (`institude_id`),
  KEY `university_id` (`university_id`),
  CONSTRAINT `stu_certification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`),
  CONSTRAINT `stu_certification_ibfk_2` FOREIGN KEY (`institude_id`) REFERENCES `institute` (`info_id`),
  CONSTRAINT `stu_certification_ibfk_3` FOREIGN KEY (`university_id`) REFERENCES `university` (`info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# 管理员
DROP USER IF EXISTS 'admin'@'localhost';
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'zxcvbnm';
GRANT ALL ON uip.* TO 'admin'@'localhost';
