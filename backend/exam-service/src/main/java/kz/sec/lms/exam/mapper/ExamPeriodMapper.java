package kz.sec.lms.exam.mapper;

import kz.sec.lms.exam.dto.ExamPeriodDTO;
import kz.sec.lms.exam.dto.FacultyDTO;
import kz.sec.lms.exam.model.ExamPeriod;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamPeriodMapper extends BaseMapper<ExamPeriod, ExamPeriodDTO, Long> {
    @Mapping(source = "facultyId", target = "faculty")
    ExamPeriodDTO toDTO(ExamPeriod examPeriod);

    @Mapping(source = "faculty.id", target = "facultyId")
    ExamPeriod toModel(ExamPeriodDTO examPeriodDTO);

    FacultyDTO facultyDTOFromId(Long id);
}
