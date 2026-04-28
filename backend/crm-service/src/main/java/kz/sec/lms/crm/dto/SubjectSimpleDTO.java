package kz.sec.lms.crm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class SubjectSimpleDTO {
    private Long id;
    private String name;
    private String syllabus;
    private Integer semester;
    private Integer ects;
}
