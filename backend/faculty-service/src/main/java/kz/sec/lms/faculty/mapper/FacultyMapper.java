package kz.sec.lms.faculty.mapper;

import ca.utoronto.lms.shared.mapper.BaseMapper;
import kz.sec.lms.faculty.dto.FacultyDTO;
import kz.sec.lms.faculty.model.Address;
import kz.sec.lms.faculty.model.Faculty;
import kz.sec.lms.faculty.model.Teacher;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Mapper(componentModel = "spring")
public abstract class FacultyMapper implements BaseMapper<Faculty, FacultyDTO, Long> {

    @PersistenceContext
    protected EntityManager em;

    @AfterMapping
    protected void fixReferences(@MappingTarget Faculty faculty, FacultyDTO dto) {
        if (dto.getDean() != null && dto.getDean().getId() != null) {
            faculty.setDean(em.getReference(Teacher.class, dto.getDean().getId()));
        }
        if (dto.getAddress() != null && dto.getAddress().getId() != null) {
            faculty.setAddress(em.getReference(Address.class, dto.getAddress().getId()));
        }
    }
}
