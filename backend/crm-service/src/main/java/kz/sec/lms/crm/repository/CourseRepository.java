package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Course;
import kz.sec.lms.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends BaseRepository<Course, Long> {

    List<Course> findByAvailableTrueAndDeletedFalseOrderByTitleAsc();

    List<Course> findByDeletedFalseOrderByTitleAsc();

    boolean existsByStudyProgramIdAndDeletedFalse(Long studyProgramId);

    boolean existsBySubjectIdAndDeletedFalse(Long subjectId);

    @Query("SELECT DISTINCT c.category FROM Course c WHERE c.deleted = false ORDER BY c.category ASC")
    List<String> findDistinctCategories();
}
