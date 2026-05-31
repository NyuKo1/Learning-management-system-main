package kz.sec.lms.notify;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients({"kz.sec.lms.notify", "kz.sec.lms.shared"})
@ComponentScan({"kz.sec.lms.notify", "kz.sec.lms.shared"})
@OpenAPIDefinition(
        info =
                @Info(
                        title = "SEC Notify API",
                        version = "1.0",
                        description = "SEC Notify — Telegram notifications API v1.0"))
public class NotifyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotifyServiceApplication.class, args);
    }
}
