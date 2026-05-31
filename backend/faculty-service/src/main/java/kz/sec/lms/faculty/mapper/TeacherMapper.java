package kz.sec.lms.faculty.mapper;

import kz.sec.lms.faculty.dto.TeacherDTO;
import kz.sec.lms.faculty.model.Teacher;
import kz.sec.lms.shared.dto.UserDTO;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper extends BaseMapper<Teacher, TeacherDTO, Long> {
    @Mapping(source = "userId", target = "user")
    TeacherDTO toDTO(Teacher teacher);

    @Mapping(source = "user.id", target = "userId")
    Teacher toModel(TeacherDTO teacherDTO);

    UserDTO userDTOFromId(Long id);
}
