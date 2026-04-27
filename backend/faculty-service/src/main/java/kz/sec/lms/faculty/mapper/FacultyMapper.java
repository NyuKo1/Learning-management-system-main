package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.FacultyDTO;
import kz.sec.lms.faculty.model.Faculty;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultyMapper extends BaseMapper<Faculty, FacultyDTO, Long> {}
