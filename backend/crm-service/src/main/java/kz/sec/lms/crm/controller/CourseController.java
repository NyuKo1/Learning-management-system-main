package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.model.Course;
import kz.sec.lms.crm.service.CourseService;
import ca.utoronto.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController extends BaseController<Course, CourseDTO, Long> {

    private final CourseService service;

    public CourseController(CourseService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseDTO>> getAvailable() {
        return new ResponseEntity<>(service.findAvailable(), HttpStatus.OK);
    }
}
