package kz.sec.lms.crm.mapper;

import kz.sec.lms.crm.dto.ClientDTO;
import kz.sec.lms.crm.model.Client;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends BaseMapper<Client, ClientDTO, Long> {
}
