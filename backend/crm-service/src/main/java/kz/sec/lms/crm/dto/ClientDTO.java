package kz.sec.lms.crm.dto;

import ca.utoronto.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO extends BaseDTO<Long> {

    @NotBlank
    private String fullName;

    private String phone;
    private String email;
    private Integer totalPurchases;
    private BigDecimal totalSpent;
    private Boolean hasLmsAccount;
    private Long lmsUserId;
    private String notes;
}
