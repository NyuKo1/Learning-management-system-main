package kz.sec.lms.exam.mapper;

import kz.sec.lms.exam.dto.ExamDTO;
import kz.sec.lms.exam.dto.ExamPeriodDTO;
import kz.sec.lms.exam.dto.ExamTermDTO;
import kz.sec.lms.exam.dto.SubjectDTO;
import kz.sec.lms.exam.model.Exam;
import kz.sec.lms.exam.model.ExamPeriod;
import kz.sec.lms.exam.model.ExamTerm;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamTermMapper extends BaseMapper<ExamTerm, ExamTermDTO, Long> {
    @Mapping(source = "subjectId", target = "subject")
    @Mapping(target = "examType", ignore = true)
    ExamDTO toDTO(Exam exam);

    @Mapping(source = "subject.id", target = "subjectId")
    Exam toModel(ExamDTO examDTO);

    SubjectDTO subjectDTOFromId(Long id);

    @Mapping(target = "faculty", ignore = true)
    ExamPeriodDTO toDTO(ExamPeriod examPeriod);
}
