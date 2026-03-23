package java_prac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseTeacher {

    @EmbeddedId
    private CourseTeacherId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;
}