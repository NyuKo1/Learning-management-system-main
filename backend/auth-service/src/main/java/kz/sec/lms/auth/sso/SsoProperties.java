package kz.sec.lms.auth.sso;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sso")
public class SsoProperties {
    private List<SsoClient> allowedClients = new ArrayList<>();
    private int codeTtlMinutes = 10;
    private String allowedOrigins = "";
}
