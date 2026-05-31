package kz.sec.lms.exam;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients({"kz.sec.lms.exam", "kz.sec.lms.shared"})
@ComponentScan({"kz.sec.lms.exam", "kz.sec.lms.shared"})
@OpenAPIDefinition(
        info =
                @Info(
                        title = "SEC LMS — Exam Service API",
                        version = "1.0",
                        description = "SmartEduControl Exam & Assessment Management v1.0"))
public class ExamServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamServiceApplication.class, args);
    }
}
