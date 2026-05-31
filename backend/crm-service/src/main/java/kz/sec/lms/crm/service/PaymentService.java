package kz.sec.lms.crm.service;

import kz.sec.lms.crm.client.SubjectFeignClient;
import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.dto.EnrollmentRequest;
import kz.sec.lms.crm.dto.PaymentDTO;
import kz.sec.lms.crm.mapper.CourseMapper;
import kz.sec.lms.crm.mapper.PaymentMapper;
import kz.sec.lms.crm.model.Payment;
import kz.sec.lms.crm.repository.CourseRepository;
import kz.sec.lms.crm.repository.PaymentRepository;
import kz.sec.lms.shared.service.ExtendedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kz.sec.lms.shared.security.SecurityUtils.getStudentId;

@Slf4j
@Service
public class PaymentService extends ExtendedService<Payment, PaymentDTO, Long> {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseService courseService;
    private final SubjectFeignClient subjectFeignClient;

    public PaymentService(
            PaymentRepository repository,
            PaymentMapper mapper,
            CourseRepository courseRepository,
            CourseMapper courseMapper,
            CourseService courseService,
            SubjectFeignClient subjectFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseService = courseService;
        this.subjectFeignClient = subjectFeignClient;
    }

    @Override
    protected List<PaymentDTO> mapMissingValues(List<PaymentDTO> payments) {
        payments.forEach(payment -> {
            if (payment.getCourseId() != null) {
                courseRepository.findById(payment.getCourseId())
                    .ifPresent(course -> {
                        payment.setCourse(courseMapper.toDTO(course));
                        if (payment.getCourseTitle() == null) {
                            payment.setCourseTitle(course.getTitle());
                        }
                    });
            }
        });
        return payments;
    }

    public List<PaymentDTO> findAll() {
        List<PaymentDTO> payments = mapper.toDTO(
            repository.findByDeletedFalseOrderByCreatedAtDesc()
        );
        return payments.isEmpty() ? payments : mapMissingValues(payments);
    }

    @Override
    @Transactional
    public PaymentDTO save(PaymentDTO dto) {
        if (dto.getCardNumber() != null && !dto.getCardNumber().isBlank()) {
            String cleaned = dto.getCardNumber().replaceAll("\\s", "");
            if (cleaned.length() >= 4) {
                dto.setCardLastFour(cleaned.substring(cleaned.length() - 4));
            }
        }
        dto.setCardNumber(null);

        if (dto.getCurrency() == null || dto.getCurrency().isBlank()) {
            dto.setCurrency("₸");
        }
        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            dto.setStatus("SUCCESS");
        }
        dto.setUserId(getCurrentUsername());

        if (dto.getCourseId() != null && (dto.getCourseTitle() == null || dto.getCourseTitle().isBlank())) {
            courseRepository.findById(dto.getCourseId())
                .ifPresent(c -> dto.setCourseTitle(c.getTitle()));
        }

        PaymentDTO saved = super.save(dto);

        if (saved.getCourseId() != null) {
            courseService.incrementStudentsCount(saved.getCourseId());
            courseRepository.findById(saved.getCourseId())
                .ifPresent(course -> {
                    saved.setCourse(courseMapper.toDTO(course));
                    saved.setCourseTitle(course.getTitle());

                    // Enroll student in LMS subject if course is linked
                    if (course.getSubjectId() != null) {
                        tryEnrollInSubject(course.getSubjectId());
                    }
                });
        }

        return saved;
    }

    private void tryEnrollInSubject(Long subjectId) {
        try {
            Long studentId = getStudentId();
            if (studentId != null) {
                subjectFeignClient.enroll(new EnrollmentRequest(studentId, subjectId));
                log.info("Enrolled student {} in subject {}", studentId, subjectId);
            }
        } catch (Exception e) {
            log.warn("Could not enroll student in subject {}: {}", subjectId, e.getMessage());
        }
    }

    public List<PaymentDTO> findByUserId(String userId) {
        List<PaymentDTO> payments = mapper.toDTO(
            repository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId)
        );
        return payments.isEmpty() ? payments : mapMissingValues(payments);
    }

    public List<PaymentDTO> findByClientId(Long clientId) {
        List<PaymentDTO> payments = mapper.toDTO(
            repository.findByClientIdAndDeletedFalseOrderByCreatedAtDesc(clientId)
        );
        return payments.isEmpty() ? payments : mapMissingValues(payments);
    }

    public boolean isCoursePurchased(Long courseId, String userId) {
        return repository.existsByCourseIdAndUserIdAndDeletedFalse(courseId, userId);
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }
}
