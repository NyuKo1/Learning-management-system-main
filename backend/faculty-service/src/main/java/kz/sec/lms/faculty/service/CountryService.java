package kz.sec.lms.faculty.service;

import kz.sec.lms.faculty.dto.CountryDTO;
import kz.sec.lms.faculty.mapper.CountryMapper;
import kz.sec.lms.faculty.model.Country;
import kz.sec.lms.faculty.repository.CountryRepository;
import ca.utoronto.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends BaseService<Country, CountryDTO, Long> {
    private final CountryRepository repository;
    private final CountryMapper mapper;

    public CountryService(CountryRepository repository, CountryMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }
}
