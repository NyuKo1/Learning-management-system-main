package kz.sec.lms.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("faculty-service")
public interface FacultyFeignClient {
    @GetMapping("/administrators/user-id/{id}/id")
    Long getAdministratorIdByUserId(@PathVariable Long id);

    @GetMapping("/teachers/user-id/{id}/id")
    Long getTeacherIdByUserId(@PathVariable Long id);

    @GetMapping("/students/user-id/{id}/id")
    Long getStudentIdByUserId(@PathVariable Long id);

    // Provision a minimal student record. Forwards the caller's admin token so the
    // faculty-service ROLE_ADMIN check passes (no inter-service token interceptor exists).
    @PostMapping("/students/provision")
    Long provisionStudent(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "username", required = false) String username);
}
