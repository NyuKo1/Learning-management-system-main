package kz.sec.lms.shared.audit;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@AutoConfiguration
@EnableAsync
@ComponentScan("kz.sec.lms.shared.audit")
public class AuditAutoConfiguration {

    @Bean(name = "auditExecutor")
    public Executor auditExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(2);
        ex.setMaxPoolSize(8);
        ex.setQueueCapacity(500);
        ex.setThreadNamePrefix("audit-");
        ex.initialize();
        return ex;
    }
}
