package kz.sec.lms.crm.dto;

import kz.sec.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseLessonDTO extends BaseDTO<Long> {

    @NotNull
    private Long courseId;

    @NotBlank
    private String title;

    private String description;
    private String videoUrl;
    private String content;

    @NotNull
    private Integer orderIndex;

    private String duration;
}
