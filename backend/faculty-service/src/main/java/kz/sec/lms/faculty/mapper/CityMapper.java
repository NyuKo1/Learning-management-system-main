package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.CityDTO;
import kz.sec.lms.faculty.model.City;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper extends BaseMapper<City, CityDTO, Long> {}
