package ca.utoronto.lms.crm.dto;

import ca.utoronto.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseDTO extends BaseDTO<Long> {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String instructor;

    @NotBlank
    private String category;

    @NotBlank
    private String level;

    @NotBlank
    private String duration;

    @NotNull
    private Integer lessons;

    @NotNull
    private BigDecimal price;

    private BigDecimal originalPrice;

    @NotNull
    private Double rating;

    @NotNull
    private Integer studentsCount;

    @NotBlank
    private String tags;

    @NotBlank
    private String color;

    @NotBlank
    private String icon;

    private Boolean available;
}
