package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Course;
import ca.utoronto.lms.shared.repository.BaseRepository;

import java.util.List;

public interface CourseRepository extends BaseRepository<Course, Long> {

    List<Course> findByAvailableTrueAndDeletedFalseOrderByTitleAsc();
}
