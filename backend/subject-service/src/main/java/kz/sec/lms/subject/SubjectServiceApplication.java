package kz.sec.lms.subject;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients({"kz.sec.lms.subject", "kz.sec.lms.shared"})
@ComponentScan({"kz.sec.lms.subject", "kz.sec.lms.shared"})
@OpenAPIDefinition(
        info =
                @Info(
                        title = "SEC LMS — Subject Service API",
                        version = "1.0",
                        description = "SmartEduControl Subject & Enrollment Management v1.0"))
public class SubjectServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SubjectServiceApplication.class, args);
    }
}
