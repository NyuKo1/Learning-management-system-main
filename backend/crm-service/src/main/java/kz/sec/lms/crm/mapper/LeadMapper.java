package kz.sec.lms.crm.mapper;

import kz.sec.lms.crm.dto.LeadDTO;
import kz.sec.lms.crm.model.Lead;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeadMapper extends BaseMapper<Lead, LeadDTO, Long> {
}
