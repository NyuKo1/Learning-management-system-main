package kz.sec.lms.auth.sso;

import ca.utoronto.lms.shared.exception.BadRequestException;
import kz.sec.lms.auth.dto.TokensDTO;
import kz.sec.lms.auth.security.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SsoService {

    private static final String CODE_PREFIX = "sso:code:";

    private final SsoProperties ssoProperties;
    private final TokenGenerator tokenGenerator;
    private final RedisTemplate<String, Object> redisTemplate;

    public String generateAuthorizationCode(String clientId, String redirectUri, String username) {
        SsoClient client = findClient(clientId);
        if (!client.getRedirectUris().contains(redirectUri)) {
            throw new BadRequestException("Invalid redirect_uri for client: " + clientId);
        }

        String code = UUID.randomUUID().toString();
        SsoAuthorizationCode authCode = new SsoAuthorizationCode(username, clientId, redirectUri);
        redisTemplate.opsForValue().set(
                CODE_PREFIX + code,
                authCode,
                ssoProperties.getCodeTtlMinutes(),
                TimeUnit.MINUTES
        );
        return code;
    }

    public TokensDTO exchangeCode(String code, String clientId, String clientSecret, String redirectUri) {
        SsoClient client = findClient(clientId);
        if (!client.getClientSecret().equals(clientSecret)) {
            throw new BadRequestException("Invalid client_secret");
        }

        String key = CODE_PREFIX + code;
        Object stored = redisTemplate.opsForValue().get(key);
        if (stored == null) {
            throw new BadRequestException("Invalid or expired authorization code");
        }

        SsoAuthorizationCode authCode;
        if (stored instanceof SsoAuthorizationCode) {
            authCode = (SsoAuthorizationCode) stored;
        } else if (stored instanceof java.util.Map<?, ?> map) {
            authCode = new SsoAuthorizationCode(
                    String.valueOf(map.get("username")),
                    String.valueOf(map.get("clientId")),
                    String.valueOf(map.get("redirectUri")));
        } else {
            throw new BadRequestException("Corrupted authorization code");
        }

        if (!authCode.getClientId().equals(clientId) || !authCode.getRedirectUri().equals(redirectUri)) {
            throw new BadRequestException("Code/client mismatch");
        }

        redisTemplate.delete(key);

        String username = authCode.getUsername();
        return new TokensDTO(
                tokenGenerator.generateAccessToken(username),
                tokenGenerator.generateRefreshToken(username)
        );
    }

    private SsoClient findClient(String clientId) {
        return ssoProperties.getAllowedClients().stream()
                .filter(c -> c.getClientId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Unknown client_id: " + clientId));
    }
}
