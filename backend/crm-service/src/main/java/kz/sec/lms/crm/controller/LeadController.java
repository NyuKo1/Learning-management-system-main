package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.LeadDTO;
import kz.sec.lms.crm.model.Lead;
import kz.sec.lms.crm.service.LeadService;
import kz.sec.lms.shared.audit.Audited;
import kz.sec.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leads")
public class LeadController extends BaseController<Lead, LeadDTO, Long> {

    private final LeadService service;

    public LeadController(LeadService service) {
        super(service);
        this.service = service;
    }

    @Audited(sensitive = true)
    @GetMapping("/all")
    public ResponseEntity<List<LeadDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeadDTO> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) return ResponseEntity.badRequest().build();
        LeadDTO updated = service.updateStatus(id, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}
