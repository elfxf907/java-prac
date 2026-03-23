package java_prac.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "teacher")
    @Builder.Default
    private Set<CourseTeacher> courseTeachers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "teacher")
    @Builder.Default
    private Set<Lesson> lessons = new LinkedHashSet<>();
}