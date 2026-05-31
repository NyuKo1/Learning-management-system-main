package kz.sec.lms.faculty.service;

import kz.sec.lms.faculty.dto.StudyProgramDTO;
import kz.sec.lms.faculty.mapper.StudyProgramMapper;
import kz.sec.lms.faculty.model.StudyProgram;
import kz.sec.lms.faculty.repository.FacultyRepository;
import kz.sec.lms.faculty.repository.StudyProgramRepository;
import kz.sec.lms.shared.exception.NotFoundException;
import kz.sec.lms.shared.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyProgramService extends BaseService<StudyProgram, StudyProgramDTO, Long> {
    private final StudyProgramRepository repository;
    private final StudyProgramMapper mapper;
    private final FacultyRepository facultyRepository;

    public StudyProgramService(
            StudyProgramRepository repository,
            StudyProgramMapper mapper,
            FacultyRepository facultyRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.facultyRepository = facultyRepository;
    }

    public List<StudyProgramDTO> findByFacultyId(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new NotFoundException("Faculty not found");
        }
        return mapper.toDTO(repository.findByFacultyIdAndDeletedFalse(id));
    }
}
