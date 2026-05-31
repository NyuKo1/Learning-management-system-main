package kz.sec.lms.subject.service;

import kz.sec.lms.shared.exception.ForbiddenException;
import kz.sec.lms.shared.exception.NotFoundException;
import kz.sec.lms.shared.service.ExtendedService;
import kz.sec.lms.subject.client.FacultyFeignClient;
import kz.sec.lms.subject.dto.SubjectDTO;
import kz.sec.lms.subject.dto.SubjectMaterialDTO;
import kz.sec.lms.subject.dto.TeacherDTO;
import kz.sec.lms.subject.mapper.SubjectMaterialMapper;
import kz.sec.lms.subject.model.Subject;
import kz.sec.lms.subject.model.SubjectMaterial;
import kz.sec.lms.subject.repository.SubjectMaterialRepository;
import kz.sec.lms.subject.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static kz.sec.lms.shared.security.SecurityUtils.*;

@Service
public class SubjectMaterialService
        extends ExtendedService<SubjectMaterial, SubjectMaterialDTO, Long> {
    private final SubjectMaterialRepository repository;
    private final SubjectMaterialMapper mapper;
    private final SubjectRepository subjectRepository;
    private final FacultyFeignClient facultyFeignClient;

    public SubjectMaterialService(
            SubjectMaterialRepository repository,
            SubjectMaterialMapper mapper,
            SubjectRepository subjectRepository,
            FacultyFeignClient facultyFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subjectRepository = subjectRepository;
        this.facultyFeignClient = facultyFeignClient;
    }

    @Override
    @Transactional
    public SubjectMaterialDTO save(SubjectMaterialDTO subjectMaterialDTO) {
        if (hasAuthority(ROLE_TEACHER)) {
            TeacherDTO teacher = facultyFeignClient.getTeacher(Set.of(getTeacherId())).get(0);
            SubjectDTO subject = subjectMaterialDTO.getSubject();
            if (!subject.getProfessor().getId().equals(teacher.getId())
                    && !subject.getAssistant().getId().equals(teacher.getId())) {
                throw new ForbiddenException(
                        "You are not allowed to manager this subject material");
            }

            if (subjectMaterialDTO.getTeacher() == null) {
                subjectMaterialDTO.setTeacher(teacher);
            }
        }

        return super.save(subjectMaterialDTO);
    }

    @Override
    @Transactional
    public void delete(Set<Long> id) {
        if (hasAuthority(ROLE_TEACHER)) {
            Long teacherId = getTeacherId();
            List<SubjectMaterial> subjectMaterials =
                    (List<SubjectMaterial>) repository.findAllById(id);
            boolean forbidden =
                    subjectMaterials.stream()
                            .anyMatch(
                                    subjectMaterial -> {
                                        Subject subject = subjectMaterial.getSubject();
                                        return !subject.getProfessorId().equals(teacherId)
                                                && !subject.getAssistantId().equals(teacherId);
                                    });
            if (forbidden) {
                throw new ForbiddenException(
                        "You are not allowed to delete these subject materials");
            }
        }

        super.delete(id);
    }

    @Override
    protected List<SubjectMaterialDTO> mapMissingValues(List<SubjectMaterialDTO> subjectMaterials) {
        map(
                subjectMaterials,
                SubjectMaterialDTO::getTeacher,
                SubjectMaterialDTO::setTeacher,
                facultyFeignClient::getTeacher);

        return subjectMaterials;
    }

    public List<SubjectMaterialDTO> findBySubjectId(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found");
        }

        List<SubjectMaterialDTO> subjectMaterials =
                mapper.toDTO(
                        repository.findBySubjectIdAndDeletedFalseOrderByPublicationDateDesc(id));
        return subjectMaterials.isEmpty()
                ? subjectMaterials
                : this.mapMissingValues(subjectMaterials);
    }

    public Page<SubjectMaterialDTO> findBySubjectId(Long id, Pageable pageable, String search) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found");
        }

        Page<SubjectMaterialDTO> subjectMaterials =
                repository
                        .findBySubjectIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return subjectMaterials.getContent().isEmpty()
                ? subjectMaterials
                : new PageImpl<>(
                        this.mapMissingValues(subjectMaterials.getContent()),
                        pageable,
                        subjectMaterials.getTotalElements());
    }
}
