package kz.sec.lms.auth.security;

import kz.sec.lms.auth.client.FacultyFeignClient;
import kz.sec.lms.auth.model.User;
import kz.sec.lms.auth.repository.UserRepository;
import kz.sec.lms.shared.exception.BadRequestException;
import kz.sec.lms.shared.exception.NotFoundException;
import kz.sec.lms.shared.security.TokenUtils;
import feign.FeignException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static kz.sec.lms.shared.security.SecurityUtils.*;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final UserRepository userRepository;
    private final FacultyFeignClient facultyFeignClient;
    private final TokenUtils tokenUtils;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.accessExpiration}")
    private Long accessExpiration;

    @Value("${token.refreshExpiration}")
    private Long refreshExpiration;

    public String refreshAccessToken(String refreshToken) {
        String username = tokenUtils.getUsername(refreshToken);
        if (username == null) {
            throw new AuthenticationException("Refresh token isn't valid!") {};
        }
        return generateAccessToken(username);
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = generateClaims(username);
        return Jwts.builder()
                .setClaims(claims) // It has to be first because it overwrites the rest
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(String username) {
        validateUser(username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Map<String, Object> generateClaims(String username) {
        Map<String, Object> claims = new HashMap<>();

        User user = validateUser(username);
        Long userId = user.getId();
        claims.put("userId", userId);

        List<String> authorities =
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", authorities);

        // Accounts created outside the LMS (e.g. course buyers provisioned from the
        // CRM via /users/register-student) have an auth user but no faculty-service
        // record. The profile-id lookup must not block login in that case — the claim
        // is simply omitted and LMS features that key on username still work.
        if (authorities.contains(ROLE_ADMIN)) {
            putIfPresent(claims, "adminId", () -> facultyFeignClient.getAdministratorIdByUserId(userId));
        } else if (authorities.contains(ROLE_TEACHER)) {
            putIfPresent(claims, "teacherId", () -> facultyFeignClient.getTeacherIdByUserId(userId));
        } else if (authorities.contains(ROLE_STUDENT)) {
            putIfPresent(claims, "studentId", () -> facultyFeignClient.getStudentIdByUserId(userId));
        }

        return claims;
    }

    private void putIfPresent(Map<String, Object> claims, String key, Supplier<Long> lookup) {
        try {
            Long value = lookup.get();
            if (value != null) {
                claims.put(key, value);
            }
        } catch (FeignException.NotFound ignored) {
            // No faculty-service profile for this user — omit the claim, allow login.
        }
    }

    private User validateUser(String username) {
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.isDeleted()) {
            throw new BadRequestException("User is deleted");
        }
        return user;
    }
}
