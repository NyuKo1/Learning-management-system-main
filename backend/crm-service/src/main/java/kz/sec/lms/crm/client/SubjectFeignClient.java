package kz.sec.lms.crm.client;

import kz.sec.lms.crm.dto.EnrollmentRequest;
import kz.sec.lms.crm.dto.SubjectSimpleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "subject-service", contextId = "crmSubjectFeignClient")
public interface SubjectFeignClient {

    @GetMapping("/subjects/all")
    List<SubjectSimpleDTO> getAllSubjects();

    @PostMapping("/subject-enrollments/enroll")
    void enroll(@RequestBody EnrollmentRequest request);
}
