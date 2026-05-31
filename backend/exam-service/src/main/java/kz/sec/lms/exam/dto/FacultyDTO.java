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
public class FacultyDTO extends BaseDTO<Long> {
    private String name;
    private String description;
    private String email;
}
