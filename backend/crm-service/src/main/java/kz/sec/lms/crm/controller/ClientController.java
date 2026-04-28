package kz.sec.lms.crm.controller;

import kz.sec.lms.crm.dto.ClientDTO;
import kz.sec.lms.crm.model.Client;
import kz.sec.lms.crm.service.ClientService;
import ca.utoronto.lms.shared.controller.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController extends BaseController<Client, ClientDTO, Long> {

    private final ClientService service;

    public ClientController(ClientService service) {
        super(service);
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
}
