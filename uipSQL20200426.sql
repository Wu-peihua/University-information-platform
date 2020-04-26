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
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (poster) REFERENCES user_info(user_id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

# 论坛帖子评论信息
CREATE TABLE forum_comments(
    info_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,# 主键
    post_id BIGINT(20) UNSIGNED NOT NULL, # 帖子id
    commentator_id BIGINT(20) UNSIGNED NOT NULL,# 评论人id
    content VARCHAR(516) DEFAULT NULL,   #评论内容，限制516个字符
    report_number INT DEFAULT 0,# 被举报次数
    like_number INT DEFAULT 0,# 点赞次数
    info_date DATETIME NOT NULL,# 评论日期 
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(info_id),
    FOREIGN KEY (post_id) REFERENCES forum_posts(info_id),
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

