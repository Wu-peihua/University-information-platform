package com.example.uipservice;

import org.junit.jupiter.api.Test;
import com.example.uipservice.dao.CourseEvaluationMapper;
import com.example.uipservice.entity.CourseEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

@SpringBootTest
public class CourseEvaluationDaoTest {

    @Autowired
    CourseEvaluationMapper courseEvaluationMapper;

    @Test
    public void insertCourseEvaluation(){
        CourseEvaluation courseEval = new CourseEvaluation();

        //courseEval.setInfoId((long)2);
        courseEval.setCommentatorId((long)4);
        courseEval.setContent("难度大！");
        courseEval.setCourseId((long)4);
        courseEval.setScore(3);
        courseEval.setInfoDate(new Date());

        int effectedNum = courseEvaluationMapper.insert(courseEval);
        assertEquals(1,effectedNum);

    }

}
