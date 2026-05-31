package kz.sec.lms.exam.dto;

import kz.sec.lms.shared.dto.BaseDTO;
import kz.sec.lms.shared.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherDTO extends BaseDTO<Long> {
    private UserDTO user;
    private String firstName;
    private String lastName;
}
