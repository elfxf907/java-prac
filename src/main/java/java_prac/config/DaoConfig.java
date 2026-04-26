package java_prac.config;

import java_prac.dao.CompanyDao;
import java_prac.dao.CourseDao;
import java_prac.dao.LessonDao;
import java_prac.dao.StudentDao;
import java_prac.dao.TeacherDao;
import java_prac.dao.impl.CompanyDaoImpl;
import java_prac.dao.impl.CourseDaoImpl;
import java_prac.dao.impl.LessonDaoImpl;
import java_prac.dao.impl.StudentDaoImpl;
import java_prac.dao.impl.TeacherDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

    @Bean
    public CompanyDao companyDao() {
        return new CompanyDaoImpl();
    }

    @Bean
    public StudentDao studentDao() {
        return new StudentDaoImpl();
    }

    @Bean
    public TeacherDao teacherDao() {
        return new TeacherDaoImpl();
    }

    @Bean
    public CourseDao courseDao() {
        return new CourseDaoImpl();
    }

    @Bean
    public LessonDao lessonDao() {
        return new LessonDaoImpl();
    }
}