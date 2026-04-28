package kz.sec.lms.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterStudentDTO {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;
}
