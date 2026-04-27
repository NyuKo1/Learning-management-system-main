package kz.sec.lms.faculty.service;

import kz.sec.lms.faculty.dto.FacultyDTO;
import kz.sec.lms.faculty.mapper.FacultyMapper;
import kz.sec.lms.faculty.model.Faculty;
import kz.sec.lms.faculty.repository.FacultyRepository;
import ca.utoronto.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class FacultyService extends BaseService<Faculty, FacultyDTO, Long> {
    private final FacultyRepository repository;
    private final FacultyMapper mapper;

    public FacultyService(FacultyRepository repository, FacultyMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }
}
