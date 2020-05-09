package com.example.uipservice;

import org.junit.jupiter.api.Test;
import com.example.uipservice.dao.CoursesMapper;
import com.example.uipservice.entity.Courses;
import com.example.uipservice.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;
import java.util.Date;

@SpringBootTest
public class CoursesDaoTest {

    @Autowired
    CoursesMapper coursesMapper;

    @Test
    public void insertCourses(){
        Courses courses = new Courses();
        courses.setCourseName("计算机网络");
        courses.setAverageScore((float)5.0);
        courses.setCoursePicture("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg");
        courses.setDescription("互联网的世界");
        courses.setTeacherName("唐华");
        courses.setUniversityId(1);
        courses.setInstituteId(1);
        courses.setInfoDate(new Date());
        courses.setUserId((long) 1);

        int effectedNum = coursesMapper.insert(courses);
        assertEquals(1,effectedNum);

        /*
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("coco");
        userInfo.setPw("123456");
        userInfo.setPortrait("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg");
        userInfo.setInstituteId(1);
        userInfo.setUniversityId(1);
        userInfo.setUserType(1);
        int effectedNum = userInfoMapper.insert(userInfo);
        assertEquals(1,effectedNum);
        */
    }

}
