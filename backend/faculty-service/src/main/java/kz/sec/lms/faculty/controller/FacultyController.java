package kz.sec.lms.faculty.controller;

import kz.sec.lms.faculty.dto.FacultyDTO;
import kz.sec.lms.faculty.model.Faculty;
import kz.sec.lms.faculty.service.FacultyService;
import kz.sec.lms.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faculties")
public class FacultyController extends BaseController<Faculty, FacultyDTO, Long> {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        super(service);
        this.service = service;
    }
}
