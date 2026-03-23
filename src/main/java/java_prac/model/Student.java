package java_prac.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private Set<StudentCourse> studentCourses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    private Set<LessonStudent> lessonStudents = new LinkedHashSet<>();
}