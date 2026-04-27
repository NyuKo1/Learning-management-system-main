package kz.sec.lms.exam.controller;

import kz.sec.lms.exam.dto.ExamPeriodDTO;
import kz.sec.lms.exam.model.ExamPeriod;
import kz.sec.lms.exam.service.ExamPeriodService;
import ca.utoronto.lms.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam-periods")
public class ExamPeriodController extends BaseController<ExamPeriod, ExamPeriodDTO, Long> {
    private final ExamPeriodService service;

    public ExamPeriodController(ExamPeriodService service) {
        super(service);
        this.service = service;
    }
}
