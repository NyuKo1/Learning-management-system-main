package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.CountryDTO;
import kz.sec.lms.faculty.model.Country;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper extends BaseMapper<Country, CountryDTO, Long> {}
