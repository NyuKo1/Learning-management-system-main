package ca.utoronto.lms.crm.service;

import ca.utoronto.lms.crm.dto.PaymentDTO;
import ca.utoronto.lms.crm.mapper.CourseMapper;
import ca.utoronto.lms.crm.mapper.PaymentMapper;
import ca.utoronto.lms.crm.model.Payment;
import ca.utoronto.lms.crm.repository.CourseRepository;
import ca.utoronto.lms.crm.repository.PaymentRepository;
import ca.utoronto.lms.shared.service.ExtendedService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService extends ExtendedService<Payment, PaymentDTO, Long> {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseService courseService;

    public PaymentService(
            PaymentRepository repository,
            PaymentMapper mapper,
            CourseRepository courseRepository,
            CourseMapper courseMapper,
            CourseService courseService) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseService = courseService;
    }

    @Override
    protected List<PaymentDTO> mapMissingValues(List<PaymentDTO> payments) {
        payments.forEach(payment ->
            courseRepository.findById(payment.getCourseId())
                .ifPresent(course -> payment.setCourse(courseMapper.toDTO(course)))
        );
        return payments;
    }

    @Override
    @Transactional
    public PaymentDTO save(PaymentDTO dto) {
        if (dto.getCardNumber() != null && dto.getCardNumber().replaceAll("\\s", "").length() >= 4) {
            String cleaned = dto.getCardNumber().replaceAll("\\s", "");
            dto.setCardLastFour(cleaned.substring(cleaned.length() - 4));
        }
        dto.setCardNumber(null);

        if (dto.getCurrency() == null || dto.getCurrency().isBlank()) {
            dto.setCurrency("USD");
        }
        dto.setStatus("COMPLETED");
        dto.setUserId(getCurrentUsername());

        PaymentDTO saved = super.save(dto);

        if (saved.getCourseId() != null) {
            courseService.incrementStudentsCount(saved.getCourseId());
        }

        courseRepository.findById(saved.getCourseId())
            .ifPresent(course -> saved.setCourse(courseMapper.toDTO(course)));

        return saved;
    }

    public List<PaymentDTO> findByUserId(String userId) {
        List<PaymentDTO> payments = mapper.toDTO(
            repository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId)
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
