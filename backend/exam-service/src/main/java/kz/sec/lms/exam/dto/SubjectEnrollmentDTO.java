package kz.sec.lms.exam.dto;

import kz.sec.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectEnrollmentDTO extends BaseDTO<Long> {
    private StudentDTO student;
    private SubjectDTO subject;
    private Integer extraPoints;
    private Integer grade;
}
