package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.ClientDTO;
import kz.sec.lms.crm.model.Client;
import kz.sec.lms.crm.service.ClientService;
import kz.sec.lms.shared.audit.Audited;
import kz.sec.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController extends BaseController<Client, ClientDTO, Long> {

    private final ClientService service;

    public ClientController(ClientService service) {
        super(service);
        this.service = service;
    }

    @Audited(sensitive = true)
    @GetMapping("/all")
    public ResponseEntity<List<ClientDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    /**
     * Links an existing CRM client to their LMS user account.
     * Called after successfully registering a student in auth-service.
     */
    @PatchMapping("/{id}/link-lms")
    public ResponseEntity<ClientDTO> linkLmsAccount(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long lmsUserId = body.get("lmsUserId") != null
                ? Long.parseLong(body.get("lmsUserId").toString())
                : null;
        return new ResponseEntity<>(service.linkLmsAccount(id, lmsUserId), HttpStatus.OK);
    }
}
