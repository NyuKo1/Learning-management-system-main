package ca.utoronto.lms.crm.mapper;

import ca.utoronto.lms.crm.dto.PaymentDTO;
import ca.utoronto.lms.crm.model.Payment;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends BaseMapper<Payment, PaymentDTO, Long> {

    @Mapping(target = "cardNumber", ignore = true)
    @Mapping(target = "course", ignore = true)
    PaymentDTO toDTO(Payment payment);

    @Mapping(target = "deleted", ignore = true)
    Payment toModel(PaymentDTO dto);
}
