package kz.sec.lms.subject.controller;

import kz.sec.lms.shared.controller.BaseController;
import kz.sec.lms.subject.dto.SubjectDTO;
import kz.sec.lms.subject.model.Subject;
import kz.sec.lms.subject.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController extends BaseController<Subject, SubjectDTO, Long> {
    private final SubjectService service;

    public SubjectController(SubjectService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return new ResponseEntity<>(this.service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/study-program/{id}/all")
    public ResponseEntity<List<SubjectDTO>> getByStudyProgramId(@PathVariable Long id) {
        return new ResponseEntity<>(this.service.findByStudyProgramId(id), HttpStatus.OK);
    }

    @GetMapping("/teacher/{id}/all")
    public ResponseEntity<List<SubjectDTO>> getByTeacherId(@PathVariable Long id) {
        return new ResponseEntity<>(this.service.findByTeacherId(id), HttpStatus.OK);
    }

    @GetMapping("/student/{id}/all")
    public ResponseEntity<List<SubjectDTO>> getByStudentId(@PathVariable Long id) {
        return new ResponseEntity<>(this.service.findByStudentId(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/syllabus")
    public ResponseEntity<SubjectDTO> patchSyllabus(
            @PathVariable Long id, @RequestBody String syllabus) {
        return new ResponseEntity<>(this.service.updateSyllabus(id, syllabus), HttpStatus.OK);
    }
}
