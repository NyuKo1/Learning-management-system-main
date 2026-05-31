package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.CourseLessonDTO;
import kz.sec.lms.crm.model.CourseLesson;
import kz.sec.lms.crm.service.CourseLessonService;
import kz.sec.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class CourseLessonController extends BaseController<CourseLesson, CourseLessonDTO, Long> {

    private final CourseLessonService service;

    public CourseLessonController(CourseLessonService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseLessonDTO>> getByCourse(@PathVariable Long courseId) {
        return new ResponseEntity<>(service.findByCourse(courseId), HttpStatus.OK);
    }
}
