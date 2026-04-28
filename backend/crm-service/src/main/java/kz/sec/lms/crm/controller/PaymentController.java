package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.PaymentDTO;
import kz.sec.lms.crm.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody PaymentDTO dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
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