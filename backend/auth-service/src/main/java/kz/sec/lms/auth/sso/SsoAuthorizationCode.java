package kz.sec.lms.auth.sso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsoAuthorizationCode implements Serializable {
    private String username;
    private String clientId;
    private String redirectUri;
}
