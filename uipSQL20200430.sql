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
    ("数学科学学院"),("声明科学学院"),("旅游管理学院"),("信息光电子学院"),("物理与电信工程学院"),("化学与环境学院");


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


#用户点赞举报记录
CREATE TABLE operetion_record(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    user_id BIGINT(20) UNSIGNED NOT NULL,# 操作者
    object_id BIGINT(20) UNSIGNED NOT NULL,# 帖子、评论的id
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


# 论坛帖子信息
CREATE TABLE forum_posts(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    title VARCHAR(20) NOT NULL, # 帖子标题，最长20个字符
    content TINYTEXT NOT NULL,# 帖子内容,
    pictures VARCHAR(516) DEFAULT NULL,   #帖子附带图片url，最多可传3张
    info_date DATETIME NOT NULL,# 帖子日期
    poster BIGINT(20)UNSIGNED NOT NULL,# 帖子发布人
    report_number INT DEFAULT 0,# 被举报次数
    like_number INT DEFAULT 0,# 点赞次数
    reply_number INT DEFAULT 0,# 评述数
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (poster) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (1, '测试标题', '测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 1, 0, 0, 0, '2020-04-24 11:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (2, '测试标题1', '测试内容1测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 1, 0, 0, 0, '2020-04-25 19:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (3, '测试标题2', '测试内容2测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 2, 0, 0, 0, '2020-04-25 23:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (4, '测试标题3', '测试内容3测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 3, 0, 0, 0, '2020-04-26 08:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (5, '测试标题4', '测试内容4测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 1, 10, 16, 0, '2020-04-27 10:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (6, '测试标题5', '测试内容5测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 2, 86, 64, 0, '2020-04-27 12:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (7, '测试标题6', '测试内容6测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 3, 70, 49, 0, '2020-04-27 15:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (8, '测试标题7', '测试内容7测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 1, 86, 32, 0, '2020-04-27 17:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (9, '测试标题8', '测试内容8测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 2, 5, 36, 0, '2020-04-27 20:32:48');
INSERT INTO `forum_posts`(`info_id`, `title`, `content`, `pictures`, `info_date`, `poster`, `reply_number`, `like_number`, `report_number`, `created`) VALUES (10, '测试标题9', '测试内容9测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容', NULL, '2020-04-27 23:59:59', 3, 3, 1, 0, '2020-04-27 23:32:48');

# 论坛：帖子的评论
CREATE TABLE forum_comments(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    post_id BIGINT(20) UNSIGNED NOT NULL, # 帖子id
    commentator_id BIGINT(20) UNSIGNED NOT NULL,# 评论人id
    content VARCHAR(516) DEFAULT NULL,   #评论内容，限制516个字符
    report_number INT DEFAULT 0,# 被举报次数
    like_number INT DEFAULT 0,# 点赞次数
    reply_number INT DEFAULT 0,# 回复数
    info_date DATETIME NOT NULL,# 评论日期 
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (post_id) REFERENCES forum_posts(info_id),
    FOREIGN KEY (commentator_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


# 论坛：评论的回复
CREATE TABLE comment_reply(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    comment_id BIGINT(20) UNSIGNED NOT NULL, # 原评论id
    commentator_id BIGINT(20) UNSIGNED NOT NULL, # 回复人id
    refer_user VARCHAR(20) DEFAULT NULL, # 回复给
    refer_content VARCHAR(516) DEFAULT NULL, # 引用内容
    content VARCHAR(516) DEFAULT NULL,   # 回复内容，限制516个字符
    report_number INT DEFAULT 0, # 被举报次数
    like_number INT DEFAULT 0, # 点赞次数
    info_date DATETIME NOT NULL, # 评论日期 
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (comment_id) REFERENCES forum_comments(info_id),
    FOREIGN KEY (commentator_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


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
CREATE TABLE resources(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    title VARCHAR(20) NOT NULL, # 资源标题，限制最长20个字符
    description TINYTEXT NOT NULL,# 资源描述
    address VARCHAR(516) DEFAULT NULL,   #资源地址
    info_date DATETIME NOT NULL , #资源发布日期
    user_id BIGINT(20) UNSIGNED NOT NULL,# 资源发布者id
    subject_id INT UNSIGNED NOT NULL,# 资源所属科目id
    type_id INT UNSIGNED NOT NULL,  #资源类型id
    report_number INT DEFAULT 0,# 资源被举报次数
    is_anonymous BOOLEAN DEFAULT FALSE, #是否匿名显示
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (subject_id) REFERENCES subject_info(info_id),
    FOREIGN KEY (type_id) REFERENCES resource_type(info_id),
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;



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
    average_score INT DEFAULT 0, #课程平均分
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (university_id) REFERENCES university(info_id),
    FOREIGN KEY (institute_id) REFERENCES institute(info_id),
    PRIMARY KEY(info_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


# 课程评论信息
CREATE TABLE course_evaluation(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    commentator_id BIGINT(20) UNSIGNED NOT NULL, # 评论人id
    course_id BIGINT(20) UNSIGNED NOT NULL,# 课程id
    content VARCHAR(516) DEFAULT NULL,   #评论内容，限制516个字符
    score INT DEFAULT 0,# 评分
    info_date DATETIME NOT NULL,# 评论日期 
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (course_id) REFERENCES courses(info_id),
    FOREIGN KEY (commentator_id) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


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
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    user_id BIGINT(20) UNSIGNED NOT NULL , #用户id
    stu_name VARCHAR(20) DEFAULT NULL,  #认证的学生姓名
    stu_number VARCHAR(25) DEFAULT NULL,  #认证的学生号
    institude_id INT(10) UNSIGNED DEFAULT NULL,  #申请认证的学院id
    university_id INT(10) UNSIGNED DEFAULT NULL,  #申请认证的大学id
    stu_card VARCHAR(255) DEFAULT NULL,   #学生证url
    stu_cer INT NOT NULL DEFAULT 0,  #学生认证申请的状态，0为待审核，1为审核通过，2为审核不通过   
    created TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,#记录生成日期
    PRIMARY KEY (info_id),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id),
    FOREIGN KEY (institude_id) REFERENCES institute(info_id),
    FOREIGN KEY (university_id) REFERENCES university(info_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;



# 管理员
DROP USER IF EXISTS 'admin'@'localhost';
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'zxcvbnm';
GRANT ALL ON uip.* TO 'admin'@'localhost';


INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (1, 1, 1, '好塞雷1', 0, 2, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:11');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (2, 2, 1, '好塞雷2', 0, 3, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:16');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (3, 3, 1, '好塞雷3', 0, 4, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (4, 4, 1, '好塞雷4', 0, 5, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:25');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (5, 5, 1, '好塞雷5', 0, 6, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (6, 6, 1, '好塞雷6', 0, 7, 3, '2020-04-28 19:35:55', '2020-04-28 22:23:44');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (7, 7, 1, '好塞雷7', 0, 8, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:48');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (8, 8, 1, '好塞雷8', 0, 9, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:52');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (9, 9, 1, '好塞雷9', 0, 10, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:55');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (10, 10, 1, '好塞雷10', 0, 11, 0, '2020-04-28 19:35:55', '2020-04-28 22:23:59');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (11, 1, 2, '好塞雷2', 0, 3, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:02');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (12, 2, 2, '好塞雷4', 0, 4, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:18');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (13, 3, 2, '好塞雷6', 0, 5, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:23');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (14, 4, 2, '好塞雷8', 0, 6, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:26');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (15, 5, 2, '好塞雷10', 0, 7, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:30');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (16, 6, 2, '好塞雷12', 0, 8, 3, '2020-04-28 19:35:55', '2020-04-28 22:24:33');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (17, 7, 2, '好塞雷14', 0, 9, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:37');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (18, 8, 2, '好塞雷16', 0, 10, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:41');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (19, 9, 2, '好塞雷18', 0, 11, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:45');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (20, 10, 2, '好塞雷20', 0, 12, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:48');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (21, 1, 3, '好塞雷3', 0, 4, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:52');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (22, 2, 3, '好塞雷6', 0, 5, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:55');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (23, 3, 3, '好塞雷9', 0, 6, 0, '2020-04-28 19:35:55', '2020-04-28 22:24:58');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (24, 4, 3, '好塞雷12', 0, 7, 0, '2020-04-28 19:35:55', '2020-04-28 22:25:03');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (25, 5, 3, '好塞雷15', 0, 8, 0, '2020-04-28 19:35:55', '2020-04-28 22:25:06');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (26, 6, 3, '好塞雷18', 0, 9, 3, '2020-04-28 19:35:55', '2020-04-28 22:25:09');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (27, 7, 3, '好塞雷21', 0, 10, 0, '2020-04-28 19:35:55', '2020-04-28 22:25:12');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (28, 8, 3, '好塞雷24', 0, 11, 0, '2020-04-28 19:35:55', '2020-04-28 22:25:15');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (29, 9, 3, '好塞雷27', 0, 12, 0, '2020-04-28 19:35:55', '2020-04-28 22:25:19');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (30, 10, 3, '好塞雷30', 0, 13, 0, '2020-04-28 19:35:55', '2020-04-28 22:25:22');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (31, 1, 1, '哈哈哈h1', 0, 2, 0, '2020-04-28 09:15:05', '2020-04-28 22:33:52');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (32, 2, 1, '哈哈哈h2', 0, 3, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (33, 3, 1, '哈哈哈h3', 0, 4, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (34, 4, 1, '哈哈哈h4', 0, 5, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (35, 5, 1, '哈哈哈h5', 0, 6, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (36, 6, 1, '哈哈哈h6', 0, 7, 3, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (37, 7, 1, '哈哈哈h7', 0, 8, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (38, 8, 1, '哈哈哈h8', 0, 9, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (39, 9, 1, '哈哈哈h9', 0, 10, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (40, 10, 1, '哈哈哈h10', 0, 11, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (41, 1, 2, '哈哈哈h2', 0, 3, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (42, 2, 2, '哈哈哈h4', 0, 4, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (43, 3, 2, '哈哈哈h6', 0, 5, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (44, 4, 2, '哈哈哈h8', 0, 6, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (45, 5, 2, '哈哈哈h10', 0, 7, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (46, 6, 2, '哈哈哈h12', 0, 8, 3, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (47, 7, 2, '哈哈哈h14', 0, 9, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (48, 8, 2, '哈哈哈h16', 0, 10, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (49, 9, 2, '哈哈哈h18', 0, 11, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (50, 10, 2, '哈哈哈h20', 0, 12, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (51, 1, 3, '哈哈哈h3', 0, 4, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (52, 2, 3, '哈哈哈h6', 0, 5, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (53, 3, 3, '哈哈哈h9', 0, 6, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (54, 4, 3, '哈哈哈h12', 0, 7, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (55, 5, 3, '哈哈哈h15', 0, 8, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (56, 6, 3, '哈哈哈h18', 0, 9, 3, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (57, 7, 3, '哈哈哈h21', 0, 10, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (58, 8, 3, '哈哈哈h24', 0, 11, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (59, 9, 3, '哈哈哈h27', 0, 12, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (60, 10, 3, '哈哈哈h30', 0, 13, 0, '2020-04-28 09:15:05', '2020-04-28 22:35:40');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (61, 1, 1, '疯了 抬走 h1', 0, 2, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (62, 2, 1, '疯了 抬走 h2', 0, 3, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (63, 3, 1, '疯了 抬走 h3', 0, 4, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (64, 4, 1, '疯了 抬走 h4', 0, 5, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (65, 5, 1, '疯了 抬走 h5', 0, 6, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (66, 6, 1, '疯了 抬走 h6', 0, 7, 3, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (67, 7, 1, '疯了 抬走 h7', 0, 8, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (68, 8, 1, '疯了 抬走 h8', 0, 9, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (69, 9, 1, '疯了 抬走 h9', 0, 10, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (70, 10, 1, '疯了 抬走 h10', 0, 11, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (71, 1, 2, '疯了 抬走 h2', 0, 3, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (72, 2, 2, '疯了 抬走 h4', 0, 4, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (73, 3, 2, '疯了 抬走 h6', 0, 5, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (74, 4, 2, '疯了 抬走 h8', 0, 6, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (75, 5, 2, '疯了 抬走 h10', 0, 7, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (76, 6, 2, '疯了 抬走 h12', 0, 8, 3, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (77, 7, 2, '疯了 抬走 h14', 0, 9, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (78, 8, 2, '疯了 抬走 h16', 0, 10, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (79, 9, 2, '疯了 抬走 h18', 0, 11, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (80, 10, 2, '疯了 抬走 h20', 0, 12, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (81, 1, 3, '疯了 抬走 h3', 0, 4, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (82, 2, 3, '疯了 抬走 h6', 0, 5, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (83, 3, 3, '疯了 抬走 h9', 0, 6, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (84, 4, 3, '疯了 抬走 h12', 0, 7, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (85, 5, 3, '疯了 抬走 h15', 0, 8, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (86, 6, 3, '疯了 抬走 h18', 0, 9, 3, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (87, 7, 3, '疯了 抬走 h21', 0, 10, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (88, 8, 3, '疯了 抬走 h24', 0, 11, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (89, 9, 3, '疯了 抬走 h27', 0, 12, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');
INSERT INTO `forum_comments`(`info_id`, `post_id`, `commentator_id`, `content`, `report_number`, `like_number`, `reply_number`, `info_date`, `created`) VALUES (90, 10, 3, '疯了 抬走 h30', 0, 13, 0, '2020-04-28 09:15:05', '2020-04-28 22:37:21');


INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (1, 10, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 10, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (2, 20, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 20, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (3, 30, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 30, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (4, 40, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 40, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (5, 50, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 50, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (6, 60, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 60, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (7, 70, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 70, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (8, 80, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 80, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (9, 90, 1, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 90, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (10, 10, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 20, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (11, 20, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 40, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (12, 30, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 60, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (13, 40, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 80, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (14, 50, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 100, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (15, 60, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 120, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (16, 70, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 140, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (17, 80, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 160, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (18, 90, 2, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 180, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (19, 10, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 30, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (20, 20, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 60, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (21, 30, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 90, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (22, 40, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 120, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (23, 50, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 150, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (24, 60, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 180, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (25, 70, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 210, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (26, 80, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 240, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (27, 90, 3, NULL, NULL, '<(︶︿︶)>_╭∩╮╭∩╮', 0, 270, '2020-04-28 09:15:05', '2020-04-30 10:10:53');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (28, 6, 1, NULL, NULL, '但凡有一粒花生', 0, 6, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (29, 16, 1, NULL, NULL, '但凡有一粒花生', 0, 16, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (30, 26, 1, NULL, NULL, '但凡有一粒花生', 0, 26, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (31, 36, 1, NULL, NULL, '但凡有一粒花生', 0, 36, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (32, 46, 1, NULL, NULL, '但凡有一粒花生', 0, 46, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (33, 56, 1, NULL, NULL, '但凡有一粒花生', 0, 56, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (34, 66, 1, NULL, NULL, '但凡有一粒花生', 0, 66, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (35, 76, 1, NULL, NULL, '但凡有一粒花生', 0, 76, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (36, 86, 1, NULL, NULL, '但凡有一粒花生', 0, 86, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (37, 6, 2, NULL, NULL, '但凡有一粒花生', 0, 12, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (38, 16, 2, NULL, NULL, '但凡有一粒花生', 0, 32, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (39, 26, 2, NULL, NULL, '但凡有一粒花生', 0, 52, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (40, 36, 2, NULL, NULL, '但凡有一粒花生', 0, 72, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (41, 46, 2, NULL, NULL, '但凡有一粒花生', 0, 92, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (42, 56, 2, NULL, NULL, '但凡有一粒花生', 0, 112, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (43, 66, 2, NULL, NULL, '但凡有一粒花生', 0, 132, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (44, 76, 2, NULL, NULL, '但凡有一粒花生', 0, 152, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (45, 86, 2, NULL, NULL, '但凡有一粒花生', 0, 172, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (46, 6, 3, NULL, NULL, '但凡有一粒花生', 0, 18, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (47, 16, 3, NULL, NULL, '但凡有一粒花生', 0, 48, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (48, 26, 3, NULL, NULL, '但凡有一粒花生', 0, 78, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (49, 36, 3, NULL, NULL, '但凡有一粒花生', 0, 108, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (50, 46, 3, NULL, NULL, '但凡有一粒花生', 0, 138, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (51, 56, 3, NULL, NULL, '但凡有一粒花生', 0, 168, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (52, 66, 3, NULL, NULL, '但凡有一粒花生', 0, 198, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (53, 76, 3, NULL, NULL, '但凡有一粒花生', 0, 228, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
INSERT INTO `comment_reply`(`info_id`, `comment_id`, `commentator_id`, `refer_user`, `refer_content`, `content`, `report_number`, `like_number`, `info_date`, `created`) VALUES (54, 86, 3, NULL, NULL, '但凡有一粒花生', 0, 258, '2020-04-28 09:15:05', '2020-04-30 15:37:07');
