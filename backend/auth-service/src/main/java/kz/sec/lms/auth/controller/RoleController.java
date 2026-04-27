package kz.sec.lms.auth.controller;

import kz.sec.lms.auth.model.Role;
import kz.sec.lms.auth.service.RoleService;
import ca.utoronto.lms.shared.controller.BaseController;
import ca.utoronto.lms.shared.dto.RoleDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController<Role, RoleDTO, Long> {
    private final RoleService service;

    public RoleController(RoleService service) {
        super(service);
        this.service = service;
    }
}
