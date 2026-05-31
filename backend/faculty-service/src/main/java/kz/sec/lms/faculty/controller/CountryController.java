package kz.sec.lms.faculty.controller;

import kz.sec.lms.faculty.dto.CountryDTO;
import kz.sec.lms.faculty.model.Country;
import kz.sec.lms.faculty.service.CountryService;
import kz.sec.lms.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController extends BaseController<Country, CountryDTO, Long> {
    private final CountryService service;

    public CountryController(CountryService service) {
        super(service);
        this.service = service;
    }
}
