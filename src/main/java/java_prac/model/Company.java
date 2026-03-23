package java_prac.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @OneToMany(mappedBy = "company")
    @Builder.Default
    private Set<Teacher> teachers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "company")
    @Builder.Default
    private Set<Course> courses = new LinkedHashSet<>();
}