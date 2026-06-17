package kz.sec.lms.auth.controller;

import kz.sec.lms.auth.client.FacultyFeignClient;
import kz.sec.lms.auth.dto.RegisterStudentDTO;
import kz.sec.lms.auth.model.User;
import kz.sec.lms.auth.service.UserService;
import kz.sec.lms.shared.audit.Audited;
import kz.sec.lms.shared.controller.BaseController;
import kz.sec.lms.shared.dto.RoleDTO;
import kz.sec.lms.shared.dto.UserDTO;
import kz.sec.lms.shared.dto.UserDetailsDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static kz.sec.lms.shared.security.SecurityUtils.*;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User, UserDetailsDTO, Long> {
    private final UserService service;
    private final FacultyFeignClient facultyFeignClient;

    public UserController(UserService service, FacultyFeignClient facultyFeignClient) {
        super(service);
        this.service = service;
        this.facultyFeignClient = facultyFeignClient;
    }

    @Audited(sensitive = true)
    @GetMapping("/{id}/public")
    public ResponseEntity<List<UserDTO>> getPublic(@PathVariable Set<Long> id) {
        return new ResponseEntity<>(service.findByIdPublic(id), HttpStatus.OK);
    }

    @Audited(sensitive = true)
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetailsDTO> getUserByUsername(@PathVariable String username) {
        UserDetailsDTO details = service.findByUsername(username);
        details.setPassword(null);
        return new ResponseEntity<>(details, HttpStatus.OK);
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
    public ResponseEntity<UserDetailsDTO> registerStudent(
            @Valid @RequestBody RegisterStudentDTO req,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        // The email may already have an account (e.g. a half-finished earlier attempt
        // where the CRM link wasn't saved). Make this idempotent: if an existing STUDENT
        // has this email, make sure the faculty record exists and return it so the CRM
        // links the client to it. A non-student email is a real conflict → 409.
        // (Return 409 directly, not via an exception — the shared catch-all
        // @ExceptionHandler(Exception.class) would otherwise turn it into a 500.)
        if (service.existsByUsername(req.getUsername())) {
            UserDetailsDTO existing = service.findByUsername(req.getUsername());
            boolean isStudent = existing.getAuthorities() != null
                    && existing.getAuthorities().stream()
                            .anyMatch(a -> ROLE_STUDENT.equals(a.getAuthority()));
            if (!isStudent) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            facultyFeignClient.provisionStudent(authorization, existing.getId(), existing.getUsername());
            existing.setPassword(null);
            return new ResponseEntity<>(existing, HttpStatus.OK);
        }
        UserDetailsDTO dto = UserDetailsDTO.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .authorities(Set.of(RoleDTO.builder()
                        .id(ROLE_STUDENT_ID)
                        .authority(ROLE_STUDENT)
                        .build()))
                .build();
        UserDetailsDTO saved = service.save(dto);

        // Also create the matching faculty-service student record so the account
        // is usable in the LMS. Roll back the auth user if provisioning fails, so
        // we never leave an orphaned login that can't be used.
        try {
            facultyFeignClient.provisionStudent(authorization, saved.getId(), saved.getUsername());
        } catch (RuntimeException e) {
            service.delete(Set.of(saved.getId()));
            throw e;
        }
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
