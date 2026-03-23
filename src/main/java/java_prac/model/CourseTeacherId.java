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
public class CourseTeacherId implements Serializable {

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "teacher_id")
    private Long teacherId;
}