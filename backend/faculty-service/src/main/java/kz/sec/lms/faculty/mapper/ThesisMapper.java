package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.StudentDTO;
import kz.sec.lms.faculty.dto.ThesisDTO;
import kz.sec.lms.faculty.model.Student;
import kz.sec.lms.faculty.model.Thesis;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ThesisMapper extends BaseMapper<Thesis, ThesisDTO, Long> {
    @Mapping(target = "thesis", ignore = true)
    @Mapping(target = "studyProgram", ignore = true)
    StudentDTO toDTO(Student student);
}
