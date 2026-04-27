package kz.sec.lms.exam.service;

import kz.sec.lms.exam.dto.ExamTypeDTO;
import kz.sec.lms.exam.mapper.ExamTypeMapper;
import kz.sec.lms.exam.model.ExamType;
import kz.sec.lms.exam.repository.ExamTypeRepository;
import ca.utoronto.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ExamTypeService extends BaseService<ExamType, ExamTypeDTO, Long> {
    private final ExamTypeRepository repository;
    private final ExamTypeMapper mapper;

    public ExamTypeService(ExamTypeRepository repository, ExamTypeMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }
}
