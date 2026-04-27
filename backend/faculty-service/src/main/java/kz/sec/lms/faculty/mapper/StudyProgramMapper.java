package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.FacultyDTO;
import kz.sec.lms.faculty.dto.StudyProgramDTO;
import kz.sec.lms.faculty.model.Faculty;
import kz.sec.lms.faculty.model.StudyProgram;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyProgramMapper extends BaseMapper<StudyProgram, StudyProgramDTO, Long> {
    @Mapping(target = "dean", ignore = true)
    @Mapping(target = "address", ignore = true)
    FacultyDTO toDTO(Faculty faculty);
}
