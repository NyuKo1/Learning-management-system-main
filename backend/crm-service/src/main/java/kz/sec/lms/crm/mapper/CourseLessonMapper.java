package kz.sec.lms.crm.mapper;

import kz.sec.lms.crm.dto.CourseLessonDTO;
import kz.sec.lms.crm.model.CourseLesson;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseLessonMapper extends BaseMapper<CourseLesson, CourseLessonDTO, Long> {
}
