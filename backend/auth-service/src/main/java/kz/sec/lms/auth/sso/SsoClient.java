package kz.sec.lms.auth.sso;

import lombok.Data;

import java.util.List;

@Data
public class SsoClient {
    private String clientId;
    private String clientSecret;
    private List<String> redirectUris;
}
