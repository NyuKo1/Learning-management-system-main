package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.model.Course;
import kz.sec.lms.crm.service.CourseService;
import ca.utoronto.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController extends BaseController<Course, CourseDTO, Long> {

    private final CourseService service;

    public CourseController(CourseService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseDTO>> getAvailable() {
        return new ResponseEntity<>(service.findAvailable(), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return new ResponseEntity<>(service.findCategories(), HttpStatus.OK);
    }

    @PostMapping("/sync-from-subjects")
    public ResponseEntity<Integer> syncFromSubjects() {
        return new ResponseEntity<>(service.syncFromSubjects(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/link-subject/{subjectId}")
    public ResponseEntity<CourseDTO> linkSubject(
            @PathVariable Long id, @PathVariable Long subjectId) {
        return new ResponseEntity<>(service.linkSubject(id, subjectId), HttpStatus.OK);
    }

    @PatchMapping("/{id}/link-study-program/{studyProgramId}")
    public ResponseEntity<CourseDTO> linkStudyProgram(
            @PathVariable Long id, @PathVariable Long studyProgramId) {
        return new ResponseEntity<>(service.linkStudyProgram(id, studyProgramId), HttpStatus.OK);
    }
}
