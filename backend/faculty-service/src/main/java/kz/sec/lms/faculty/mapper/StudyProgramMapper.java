package kz.sec.lms.faculty.mapper;

import kz.sec.lms.shared.mapper.BaseMapper;
import kz.sec.lms.faculty.dto.FacultyDTO;
import kz.sec.lms.faculty.dto.StudyProgramDTO;
import kz.sec.lms.faculty.model.Faculty;
import kz.sec.lms.faculty.model.StudyProgram;
import kz.sec.lms.faculty.model.Teacher;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Mapper(componentModel = "spring")
public abstract class StudyProgramMapper implements BaseMapper<StudyProgram, StudyProgramDTO, Long> {

    @PersistenceContext
    protected EntityManager em;

    @Mapping(target = "dean", ignore = true)
    @Mapping(target = "address", ignore = true)
    public abstract FacultyDTO toDTO(Faculty faculty);

    @AfterMapping
    protected void fixReferences(@MappingTarget StudyProgram studyProgram, StudyProgramDTO dto) {
        if (dto.getFaculty() != null && dto.getFaculty().getId() != null) {
            studyProgram.setFaculty(em.getReference(Faculty.class, dto.getFaculty().getId()));
        }
        if (dto.getManager() != null && dto.getManager().getId() != null) {
            studyProgram.setManager(em.getReference(Teacher.class, dto.getManager().getId()));
        }
    }
}
