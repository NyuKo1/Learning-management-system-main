package kz.sec.lms.crm.dto;

import ca.utoronto.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LeadDTO extends BaseDTO<Long> {

    @NotBlank
    private String fullName;

    private String phone;
    private String email;
    private String source;

    @NotBlank
    private String status;

    private String notes;
    private Long interestedCourseId;
    private String interestedCourseTitle;
}
