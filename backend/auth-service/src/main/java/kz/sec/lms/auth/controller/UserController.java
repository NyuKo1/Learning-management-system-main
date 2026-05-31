package kz.sec.lms.auth.controller;

import kz.sec.lms.auth.dto.RegisterStudentDTO;
import kz.sec.lms.auth.model.User;
import kz.sec.lms.auth.service.UserService;
import kz.sec.lms.shared.controller.BaseController;
import kz.sec.lms.shared.dto.RoleDTO;
import kz.sec.lms.shared.dto.UserDTO;
import kz.sec.lms.shared.dto.UserDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static kz.sec.lms.shared.security.SecurityUtils.*;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, UserDetailsDTO, Long> {
    private final UserService service;

    public UserController(UserService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/{id}/public")
    public ResponseEntity<List<UserDTO>> getPublic(@PathVariable Set<Long> id) {
        return new ResponseEntity<>(service.findByIdPublic(id), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        UserDetailsDTO details = service.findByUsername(username);
        Map<String, Object> safe = new LinkedHashMap<>();
        safe.put("id", details.getId());
        safe.put("username", details.getUsername());
        if (details.getAuthorities() != null) {
            safe.put("roles", details.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList()));
        }
        return new ResponseEntity<>(safe, HttpStatus.OK);
    }

    @GetMapping("/username/{username}/id")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable String username) {
        return new ResponseEntity<>(service.findIdByUsername(username), HttpStatus.OK);
    }

    @PatchMapping({"/{id}"})
    public ResponseEntity<UserDetailsDTO> patch(
            @PathVariable Long id, @RequestBody UserDetailsDTO DTO) {
        DTO.setId(id);
        return new ResponseEntity<>(this.service.update(DTO), HttpStatus.OK);
    }

    @PostMapping("/register-student")
    public ResponseEntity<UserDetailsDTO> registerStudent(@Valid @RequestBody RegisterStudentDTO req) {
        UserDetailsDTO dto = UserDetailsDTO.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .authorities(Set.of(RoleDTO.builder()
                        .id(ROLE_STUDENT_ID)
                        .authority(ROLE_STUDENT)
                        .build()))
                .build();
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }
}
