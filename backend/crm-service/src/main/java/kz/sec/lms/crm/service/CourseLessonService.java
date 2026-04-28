package kz.sec.lms.crm.service;

import kz.sec.lms.crm.dto.CourseLessonDTO;
import kz.sec.lms.crm.mapper.CourseLessonMapper;
import kz.sec.lms.crm.model.CourseLesson;
import kz.sec.lms.crm.repository.CourseLessonRepository;
import ca.utoronto.lms.shared.service.ExtendedService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseLessonService extends ExtendedService<CourseLesson, CourseLessonDTO, Long> {

    private final CourseLessonRepository repository;
    private final CourseLessonMapper mapper;

    public CourseLessonService(CourseLessonRepository repository, CourseLessonMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected List<CourseLessonDTO> mapMissingValues(List<CourseLessonDTO> lessons) {
        return lessons;
    }

    public List<CourseLessonDTO> findByCourse(Long courseId) {
        return mapper.toDTO(repository.findByCourseIdAndDeletedFalseOrderByOrderIndexAsc(courseId));
    }
}
