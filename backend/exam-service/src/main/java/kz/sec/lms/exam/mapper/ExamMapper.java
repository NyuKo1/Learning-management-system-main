package kz.sec.lms.exam.mapper;

import kz.sec.lms.exam.dto.ExamDTO;
import kz.sec.lms.exam.dto.SubjectDTO;
import kz.sec.lms.exam.model.Exam;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamMapper extends BaseMapper<Exam, ExamDTO, Long> {
    @Mapping(source = "subjectId", target = "subject")
    ExamDTO toDTO(Exam exam);

    @Mapping(source = "subject.id", target = "subjectId")
    Exam toModel(ExamDTO examDTO);

    SubjectDTO subjectDTOFromId(Long id);
}
