package kz.sec.lms.faculty.model;

import kz.sec.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Faculty extends BaseEntity<Long> {
    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String email;

    @ManyToOne(optional = false)
    private Teacher dean;

    @ManyToOne
    private Address address;

    @OneToMany(mappedBy = "faculty")
    private Set<StudyProgram> studyPrograms = new HashSet<>();
}
