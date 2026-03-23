package java_prac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonStudent {

    @EmbeddedId
    private LessonStudentId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("lessonId")
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}