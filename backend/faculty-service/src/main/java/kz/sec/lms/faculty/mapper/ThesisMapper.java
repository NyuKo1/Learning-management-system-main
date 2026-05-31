package kz.sec.lms.faculty.mapper;

import kz.sec.lms.shared.mapper.BaseMapper;
import kz.sec.lms.faculty.dto.StudentDTO;
import kz.sec.lms.faculty.dto.ThesisDTO;
import kz.sec.lms.faculty.model.Student;
import kz.sec.lms.faculty.model.Teacher;
import kz.sec.lms.faculty.model.Thesis;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Mapper(componentModel = "spring")
public abstract class ThesisMapper implements BaseMapper<Thesis, ThesisDTO, Long> {

    @PersistenceContext
    protected EntityManager em;

    @Mapping(target = "thesis", ignore = true)
    @Mapping(target = "studyProgram", ignore = true)
    public abstract StudentDTO toDTO(Student student);

    @AfterMapping
    protected void fixReferences(@MappingTarget Thesis thesis, ThesisDTO dto) {
        if (dto.getMentor() != null && dto.getMentor().getId() != null) {
            thesis.setMentor(em.getReference(Teacher.class, dto.getMentor().getId()));
        }
        if (dto.getStudent() != null && dto.getStudent().getId() != null) {
            thesis.setStudent(em.getReference(Student.class, dto.getStudent().getId()));
        }
    }
}
