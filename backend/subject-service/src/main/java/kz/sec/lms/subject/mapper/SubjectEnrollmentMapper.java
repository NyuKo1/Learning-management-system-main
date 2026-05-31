package kz.sec.lms.subject.mapper;

import kz.sec.lms.shared.mapper.BaseMapper;
import kz.sec.lms.subject.dto.StudentDTO;
import kz.sec.lms.subject.dto.SubjectDTO;
import kz.sec.lms.subject.dto.SubjectEnrollmentDTO;
import kz.sec.lms.subject.dto.TeacherDTO;
import kz.sec.lms.subject.model.Subject;
import kz.sec.lms.subject.model.SubjectEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectEnrollmentMapper
        extends BaseMapper<SubjectEnrollment, SubjectEnrollmentDTO, Long> {
    @Mapping(source = "studentId", target = "student")
    SubjectEnrollmentDTO toDTO(SubjectEnrollment subjectEnrollment);

    @Mapping(source = "student.id", target = "studentId")
    SubjectEnrollment toModel(SubjectEnrollmentDTO subjectEnrollmentDTO);

    StudentDTO studentDTOFromId(Long id);

    @Mapping(target = "studyProgram", ignore = true)
    @Mapping(source = "professorId", target = "professor")
    @Mapping(source = "assistantId", target = "assistant")
    SubjectDTO toDTO(Subject subject);

    TeacherDTO teacherDTOFromId(Long id);
}
