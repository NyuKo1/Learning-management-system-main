package kz.sec.lms.faculty;

import kz.sec.lms.faculty.model.Administrator;
import kz.sec.lms.faculty.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {
    private final AdministratorRepository administratorRepository;

    @Override
    public void run(ApplicationArguments args) {
        List<Administrator> administrators =
                (List<Administrator>) administratorRepository.findAll();

        if (administrators.isEmpty()) {
            administratorRepository.save(
                    Administrator.builder()
                            .id(1L)
                            .userId(1L)
                            .firstName("SEC")
                            .lastName("Admin")
                            .build());
        }
    }
}
