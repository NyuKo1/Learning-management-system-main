package kz.sec.lms.crm.mapper;

import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.model.Course;
import kz.sec.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper extends BaseMapper<Course, CourseDTO, Long> {
}
