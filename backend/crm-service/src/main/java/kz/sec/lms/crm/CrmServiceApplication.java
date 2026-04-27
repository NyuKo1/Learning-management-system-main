package kz.sec.lms.crm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients({"kz.sec.lms.crm", "ca.utoronto.lms.shared"})
@ComponentScan({"kz.sec.lms.crm", "ca.utoronto.lms.shared"})
@OpenAPIDefinition(
        info =
                @Info(
                        title = "CRM API",
                        version = "1.0",
                        description = "Documentation CRM API v1.0"))
public class CrmServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmServiceApplication.class, args);
    }
}
