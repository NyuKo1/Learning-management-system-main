package kz.sec.lms.faculty.service;

import kz.sec.lms.faculty.dto.CityDTO;
import kz.sec.lms.faculty.mapper.CityMapper;
import kz.sec.lms.faculty.model.City;
import kz.sec.lms.faculty.repository.CityRepository;
import ca.utoronto.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class CityService extends BaseService<City, CityDTO, Long> {
    private final CityRepository repository;
    private final CityMapper mapper;

    public CityService(CityRepository repository, CityMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }
}
