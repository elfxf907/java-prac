package java_prac.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration_type", nullable = false, length = 50)
    private CourseDurationType durationType;

    @Column(name = "hours_per_day", nullable = false)
    private Integer hoursPerDay;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "course")
    @Builder.Default
    private Set<CourseTeacher> courseTeachers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    @Builder.Default
    private Set<StudentCourse> studentCourses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    @Builder.Default
    private Set<Lesson> lessons = new LinkedHashSet<>();
}