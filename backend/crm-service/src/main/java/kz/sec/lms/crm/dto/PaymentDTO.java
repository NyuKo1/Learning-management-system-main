package kz.sec.lms.crm.dto;

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
public class PaymentDTO extends BaseDTO<Long> {

    @NotNull
    private Long courseId;

    @NotBlank
    private String customerName;

    @NotBlank
    private String email;

    private String cardNumber;

    private String cardLastFour;

    @NotNull
    private BigDecimal amount;

    private String currency;

    private String status;

    private String userId;

    private CourseDTO course;
}
