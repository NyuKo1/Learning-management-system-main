package ca.utoronto.lms.crm.mapper;

import ca.utoronto.lms.crm.dto.CourseDTO;
import ca.utoronto.lms.crm.model.Course;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper extends BaseMapper<Course, CourseDTO, Long> {
}
