package java_prac.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LessonStudentId implements Serializable {

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "student_id")
    private Long studentId;
}