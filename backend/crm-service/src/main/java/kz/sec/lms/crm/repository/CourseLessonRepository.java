package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.CourseLesson;
import ca.utoronto.lms.shared.repository.BaseRepository;

import java.util.List;

public interface CourseLessonRepository extends BaseRepository<CourseLesson, Long> {

    List<CourseLesson> findByCourseIdAndDeletedFalseOrderByOrderIndexAsc(Long courseId);
}
