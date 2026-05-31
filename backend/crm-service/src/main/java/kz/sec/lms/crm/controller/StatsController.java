package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.model.Payment;
import kz.sec.lms.crm.repository.ClientRepository;
import kz.sec.lms.crm.repository.CourseRepository;
import kz.sec.lms.crm.repository.LeadRepository;
import kz.sec.lms.crm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class StatsController {

    private final LeadRepository leadRepository;
    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalLeads = leadRepository.countByDeletedFalse();
        long totalClients = clientRepository.countByDeletedFalse();

        List<Payment> successPayments = paymentRepository.findByStatusInAndDeletedFalse(List.of("SUCCESS", "COMPLETED"));
        BigDecimal totalRevenue = successPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long wonLeads = leadRepository.countByStatusAndDeletedFalse("WON");
        long conversionRate = totalLeads > 0 ? (wonLeads * 100 / totalLeads) : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLeads", totalLeads);
        stats.put("newLeadsToday", leadRepository.countByStatusAndDeletedFalse("NEW"));
        stats.put("qualifiedLeads", leadRepository.countByStatusAndDeletedFalse("QUALIFIED"));
        stats.put("totalClients", totalClients);
        stats.put("newClientsThisMonth", totalClients);
        stats.put("lmsLinkedClients", clientRepository.countWithLmsAccount());
        stats.put("totalRevenue", totalRevenue);
        stats.put("revenueThisMonth", totalRevenue);
        stats.put("conversionRate", conversionRate);
        stats.put("totalCourses", courseRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {

        // ── Monthly data via native queries (no getCreatedAt() needed) ──────────
        // Build a map from "yyyy-MM" -> lead count
        Map<String, Long> leadsByMonth = new HashMap<>();
        for (Object[] row : leadRepository.countByMonthLast6()) {
            leadsByMonth.put((String) row[0], ((Number) row[1]).longValue());
        }

        // Build a map from "yyyy-MM" -> revenue
        Map<String, BigDecimal> revenueByMonth = new HashMap<>();
        for (Object[] row : paymentRepository.revenueByMonthLast6()) {
            BigDecimal rev = row[1] instanceof BigDecimal
                    ? (BigDecimal) row[1]
                    : BigDecimal.valueOf(((Number) row[1]).doubleValue());
            revenueByMonth.put((String) row[0], rev);
        }

        // Build last-6-months list in order
        LocalDate now = LocalDate.now();
        List<Map<String, Object>> monthly = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.from(now.minusMonths(i));
            String key = ym.toString(); // "yyyy-MM"
            Map<String, Object> entry = new HashMap<>();
            entry.put("month", ym.getMonth().getDisplayName(TextStyle.SHORT, new Locale("ru")));
            entry.put("leads",   leadsByMonth.getOrDefault(key, 0L));
            entry.put("revenue", revenueByMonth.getOrDefault(key, BigDecimal.ZERO));
            monthly.add(entry);
        }

        // ── Funnel by status ─────────────────────────────────────────────────────
        Map<String, Long> funnel = new LinkedHashMap<>();
        for (String status : List.of("NEW", "CONTACTED", "QUALIFIED", "PROPOSAL", "WON", "LOST")) {
            funnel.put(status, leadRepository.countByStatusAndDeletedFalse(status));
        }

        // ── Payment methods & top courses from in-memory list ────────────────────
        List<Payment> allPayments = paymentRepository.findByStatusInAndDeletedFalse(List.of("SUCCESS", "COMPLETED"));

        Map<String, Long> methods = allPayments.stream()
                .filter(p -> p.getMethod() != null)
                .collect(Collectors.groupingBy(Payment::getMethod, Collectors.counting()));

        Map<String, Long> courseCountMap = allPayments.stream()
                .filter(p -> p.getCourseTitle() != null)
                .collect(Collectors.groupingBy(Payment::getCourseTitle, Collectors.counting()));

        List<Map<String, Object>> topCourses = courseCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    BigDecimal rev = allPayments.stream()
                            .filter(p -> e.getKey().equals(p.getCourseTitle()))
                            .map(Payment::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Map<String, Object> c = new HashMap<>();
                    c.put("title",   e.getKey());
                    c.put("sales",   e.getValue());
                    c.put("revenue", rev);
                    return c;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("monthly",    monthly);
        result.put("funnel",     funnel);
        result.put("methods",    methods);
        result.put("topCourses", topCourses);
        return ResponseEntity.ok(result);
    }
}
