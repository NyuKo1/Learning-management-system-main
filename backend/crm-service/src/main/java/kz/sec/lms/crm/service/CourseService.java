package kz.sec.lms.crm.service;

import kz.sec.lms.shared.exception.NotFoundException;
import kz.sec.lms.crm.client.SubjectFeignClient;
import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.dto.SubjectSimpleDTO;
import kz.sec.lms.crm.mapper.CourseMapper;
import kz.sec.lms.crm.model.Course;
import kz.sec.lms.crm.repository.CourseRepository;
import kz.sec.lms.shared.service.ExtendedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CourseService extends ExtendedService<Course, CourseDTO, Long> {

    private static final String[] COLORS = {"#3b82f6", "#8b5cf6", "#10b981", "#6366f1", "#0ea5e9", "#f59e0b", "#ec4899"};
    private static final String[] ICONS  = {"book", "school", "science", "functions", "code", "bar_chart", "palette"};

    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final SubjectFeignClient subjectFeignClient;

    public CourseService(CourseRepository repository, CourseMapper mapper, SubjectFeignClient subjectFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subjectFeignClient = subjectFeignClient;
    }

    @Override
    @Transactional
    public CourseDTO save(CourseDTO courseDTO) {
        if (courseDTO.getLessons() == null)       courseDTO.setLessons(0);
        if (courseDTO.getRating() == null)         courseDTO.setRating(0.0);
        if (courseDTO.getStudentsCount() == null)  courseDTO.setStudentsCount(0);
        if (courseDTO.getAvailable() == null)      courseDTO.setAvailable(true);
        if (courseDTO.getPrice() == null)          courseDTO.setPrice(BigDecimal.ZERO);
        if (courseDTO.getDuration() == null || courseDTO.getDuration().isBlank())
            courseDTO.setDuration("Self-paced");
        if (courseDTO.getInstructor() == null || courseDTO.getInstructor().isBlank())
            courseDTO.setInstructor("Faculty Staff");
        if (courseDTO.getCategory() == null || courseDTO.getCategory().isBlank())
            courseDTO.setCategory("Academic");
        if (courseDTO.getLevel() == null || courseDTO.getLevel().isBlank())
            courseDTO.setLevel("BEGINNER");
        if (courseDTO.getTags() == null || courseDTO.getTags().isBlank())
            courseDTO.setTags(courseDTO.getTitle() != null ? courseDTO.getTitle() : "General");
        if (courseDTO.getColor() == null || courseDTO.getColor().isBlank())
            courseDTO.setColor("#6366f1");
        if (courseDTO.getIcon() == null || courseDTO.getIcon().isBlank())
            courseDTO.setIcon("menu_book");
        if (courseDTO.getDescription() == null || courseDTO.getDescription().isBlank())
            courseDTO.setDescription(courseDTO.getTitle() != null ? courseDTO.getTitle() : "No description");
        return super.save(courseDTO);
    }

    @Transactional
    public int syncFromSubjects() {
        List<SubjectSimpleDTO> subjects;
        try {
            subjects = subjectFeignClient.getAllSubjects();
        } catch (Exception e) {
            log.warn("Cannot reach subject-service during sync: {}", e.getMessage());
            return 0;
        }
        if (subjects == null || subjects.isEmpty()) return 0;

        int created = 0;
        for (int i = 0; i < subjects.size(); i++) {
            SubjectSimpleDTO s = subjects.get(i);
            if (s.getId() == null) continue;
            if (repository.existsBySubjectIdAndDeletedFalse(s.getId())) continue;

            CourseDTO dto = new CourseDTO();
            dto.setTitle(s.getName());
            dto.setDescription(s.getSyllabus() != null && !s.getSyllabus().isBlank() ? s.getSyllabus() : s.getName());
            dto.setInstructor("Faculty Staff");
            dto.setCategory("Academic");
            dto.setLevel("INTERMEDIATE");
            dto.setDuration((s.getEcts() != null ? s.getEcts() * 10 : 30) + "h");
            dto.setLessons(s.getEcts() != null ? s.getEcts() * 5 : 20);
            dto.setPrice(new BigDecimal("49.00"));
            dto.setOriginalPrice(new BigDecimal("89.00"));
            dto.setRating(0.0);
            dto.setStudentsCount(0);
            dto.setTags(s.getName());
            dto.setColor(COLORS[i % COLORS.length]);
            dto.setIcon(ICONS[i % ICONS.length]);
            dto.setAvailable(true);
            dto.setSubjectId(s.getId());
            save(dto);
            created++;
        }
        log.info("Synced {} new courses from subjects", created);
        return created;
    }

    @Override
    protected List<CourseDTO> mapMissingValues(List<CourseDTO> courses) {
        return courses;
    }

    public List<CourseDTO> findAll() {
        return mapper.toDTO(repository.findByDeletedFalseOrderByTitleAsc());
    }

    public List<CourseDTO> findAvailable() {
        return mapper.toDTO(repository.findByAvailableTrueAndDeletedFalseOrderByTitleAsc());
    }

    public List<String> findCategories() {
        return repository.findDistinctCategories();
    }

    public void incrementStudentsCount(Long courseId) {
        repository.findById(courseId).ifPresent(course -> {
            course.setStudentsCount(course.getStudentsCount() + 1);
            repository.save(course);
        });
    }

    @Transactional
    public CourseDTO linkSubject(Long courseId, Long subjectId) {
        Course course = repository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.setSubjectId(subjectId);
        return mapper.toDTO(repository.save(course));
    }

    @Transactional
    public CourseDTO linkStudyProgram(Long courseId, Long studyProgramId) {
        Course course = repository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.setStudyProgramId(studyProgramId);
        return mapper.toDTO(repository.save(course));
    }

    public boolean existsByStudyProgramId(Long studyProgramId) {
        return repository.existsByStudyProgramIdAndDeletedFalse(studyProgramId);
    }
}