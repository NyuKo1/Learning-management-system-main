package kz.sec.lms.exam.controller;

import kz.sec.lms.exam.dto.ExamTypeDTO;
import kz.sec.lms.exam.model.ExamType;
import kz.sec.lms.exam.service.ExamTypeService;
import ca.utoronto.lms.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam-types")
public class ExamTypeController extends BaseController<ExamType, ExamTypeDTO, Long> {
    private final ExamTypeService service;

    public ExamTypeController(ExamTypeService service) {
        super(service);
        this.service = service;
    }
}
