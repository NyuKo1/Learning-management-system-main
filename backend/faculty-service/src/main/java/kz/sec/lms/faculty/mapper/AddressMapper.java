package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.AddressDTO;
import kz.sec.lms.faculty.model.Address;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends BaseMapper<Address, AddressDTO, Long> {}
