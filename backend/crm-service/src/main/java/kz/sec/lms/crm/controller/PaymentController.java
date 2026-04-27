package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.PaymentDTO;
import kz.sec.lms.crm.model.Payment;
import kz.sec.lms.crm.service.PaymentService;
import ca.utoronto.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController extends BaseController<Payment, PaymentDTO, Long> {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> getByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(service.findByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkPurchased(
            @RequestParam Long courseId,
            @RequestParam String userId) {
        return new ResponseEntity<>(service.isCoursePurchased(courseId, userId), HttpStatus.OK);
    }
}
