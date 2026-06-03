package kz.sec.lms.audit;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients({"kz.sec.lms.audit", "kz.sec.lms.shared"})
@EnableScheduling
@ComponentScan({"kz.sec.lms.audit", "kz.sec.lms.shared"})
@OpenAPIDefinition(
        info = @Info(
                title = "SEC LMS — Audit Service API",
                version = "1.0",
                description = "SEC LMS Audit Log Service v1.0"))
public class AuditServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuditServiceApplication.class, args);
    }
}
