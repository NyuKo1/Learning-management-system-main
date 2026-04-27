package kz.sec.lms.crm.service;

import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.mapper.CourseMapper;
import kz.sec.lms.crm.model.Course;
import kz.sec.lms.crm.repository.CourseRepository;
import ca.utoronto.lms.shared.service.ExtendedService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService extends ExtendedService<Course, CourseDTO, Long> {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    public CourseService(CourseRepository repository, CourseMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected List<CourseDTO> mapMissingValues(List<CourseDTO> courses) {
        return courses;
    }

    public List<CourseDTO> findAvailable() {
        return mapper.toDTO(repository.findByAvailableTrueAndDeletedFalseOrderByTitleAsc());
    }

    public void incrementStudentsCount(Long courseId) {
        repository.findById(courseId).ifPresent(course -> {
            course.setStudentsCount(course.getStudentsCount() + 1);
            repository.save(course);
        });
    }
}
