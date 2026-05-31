package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.StudentDTO;
import kz.sec.lms.faculty.dto.StudyProgramDTO;
import kz.sec.lms.faculty.dto.ThesisDTO;
import kz.sec.lms.faculty.client.SubjectFeignClient;
import kz.sec.lms.faculty.model.Student;
import kz.sec.lms.faculty.model.StudyProgram;
import kz.sec.lms.faculty.model.Thesis;
import kz.sec.lms.shared.dto.UserDTO;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class StudentMapper implements BaseMapper<Student, StudentDTO, Long> {

    @PersistenceContext
    protected EntityManager em;

    private SubjectFeignClient subjectFeignClient;

    @Autowired
    public void setSubjectFeignClient(SubjectFeignClient subjectFeignClient) {
        this.subjectFeignClient = subjectFeignClient;
    }

    @AfterMapping
    protected void fixReferences(@MappingTarget Student student, StudentDTO dto) {
        if (dto.getStudyProgram() != null && dto.getStudyProgram().getId() != null) {
            student.setStudyProgram(em.getReference(StudyProgram.class, dto.getStudyProgram().getId()));
        }
        if (dto.getThesis() != null && dto.getThesis().getId() != null) {
            student.setThesis(em.getReference(Thesis.class, dto.getThesis().getId()));
        }
    }

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "id", target = "averageGrade", qualifiedByName = "getAverageGrade")
    @Mapping(source = "id", target = "totalECTS", qualifiedByName = "getTotalECTS")
    public abstract StudentDTO toDTO(Student student);

    @Mapping(source = "user.id", target = "userId")
    public abstract Student toModel(StudentDTO studentDTO);

    public List<StudentDTO> toDTO(List<Student> students) {
        List<StudentDTO> list = students.stream().map(this::toDTO).toList();
        if (list.size() > 0) {
            List<Long> studentIds = list.stream().map(StudentDTO::getId).toList();
            List<Double> averageGrades = subjectFeignClient.getAverageGradesByStudentId(studentIds);
            List<Integer> totalECTS = subjectFeignClient.getTotalECTSByStudentId(studentIds);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setAverageGrade(averageGrades.get(i));
                list.get(i).setTotalECTS(totalECTS.get(i));
            }
        }

        return list;
    }

    public abstract UserDTO userDTOFromId(Long id);

    @Mapping(target = "student", ignore = true)
    public abstract ThesisDTO toDTO(Thesis thesis);

    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "manager", ignore = true)
    public abstract StudyProgramDTO toDTO(StudyProgram studyProgram);

    @Named("getAverageGrade")
    public Double getAverageGrade(Long studentId) {
        return subjectFeignClient.getAverageGradesByStudentId(List.of(studentId)).get(0);
    }

    @Named("getTotalECTS")
    public Integer getTotalECTS(Long studentId) {
        return subjectFeignClient.getTotalECTSByStudentId(List.of(studentId)).get(0);
    }
}
