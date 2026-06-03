package kz.sec.lms.shared.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Captures every inbound HTTP request, classifies it, and hands off to
 * AuditDispatcher for asynchronous delivery.
 *
 * Endpoints marked @Audited(sensitive=true) cause the event to be flagged sensitive
 * even on GET; otherwise GETs are skipped.
 */
@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuditFilter extends OncePerRequestFilter {

    private static final Set<String> WRITE_METHODS = Set.of("POST", "PUT", "PATCH", "DELETE");
    private static final Set<String> SKIP_PATH_PREFIXES = Set.of(
            "/actuator", "/docs", "/v3/api-docs", "/audit");

    private final AuditDispatcher dispatcher;

    @Value("${spring.application.name:unknown-service}")
    private String serviceName;

    private final Set<String> sensitiveGetPaths = new HashSet<>();

    public AuditFilter(AuditDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Registers a path as sensitive (called by AuditAnnotationScanner at startup
     * for every @Audited-annotated endpoint).
     */
    public void registerSensitiveGet(String path) {
        sensitiveGetPaths.add(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest rawRequest,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (shouldSkip(rawRequest)) {
            chain.doFilter(rawRequest, response);
            return;
        }

        CachedBodyHttpServletRequest request =
                rawRequest instanceof CachedBodyHttpServletRequest
                        ? (CachedBodyHttpServletRequest) rawRequest
                        : new CachedBodyHttpServletRequest(rawRequest);

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            int durationMs = (int) (System.currentTimeMillis() - start);
            try {
                AuditEventDTO event = buildEvent(request, response, durationMs);
                if (event != null) {
                    dispatcher.dispatch(event);
                }
            } catch (Exception e) {
                log.warn("Failed to build/dispatch audit event: {}", e.getMessage());
            }
        }
    }

    private boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path == null) return true;
        for (String prefix : SKIP_PATH_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        if ("true".equalsIgnoreCase(request.getHeader("X-Internal-Call"))) return true;
        return false;
    }

    private AuditEventDTO buildEvent(CachedBodyHttpServletRequest request,
                                     HttpServletResponse response,
                                     int durationMs) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        boolean isWrite = WRITE_METHODS.contains(method);
        boolean isSensitive = sensitiveGetPaths.contains(path) || matchesSensitivePattern(path);

        if (!isWrite && !isSensitive) return null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;
        List<String> roles = auth == null ? List.of() :
                auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

        String requestId = Objects.toString(request.getAttribute(RequestIdFilter.ATTRIBUTE), "");
        String body = request.getBodyAsString();
        String redactedBody = body == null || body.isEmpty() ? null : BodyRedactor.redact(body);

        return AuditEventDTO.builder()
                .occurredAt(Instant.now())
                .requestId(requestId)
                .username(username)
                .roles(roles)
                .serviceName(serviceName)
                .httpMethod(method)
                .path(path)
                .status(response.getStatus())
                .remoteIp(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .sensitive(isSensitive)
                .requestBody(redactedBody)
                .durationMs(durationMs)
                .build();
    }

    private boolean matchesSensitivePattern(String requestPath) {
        for (String pattern : sensitiveGetPaths) {
            if (pattern.contains("{") && pathMatchesPattern(requestPath, pattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean pathMatchesPattern(String path, String pattern) {
        String[] pSeg = path.split("/");
        String[] pat = pattern.split("/");
        if (pSeg.length != pat.length) return false;
        for (int i = 0; i < pSeg.length; i++) {
            if (pat[i].startsWith("{") && pat[i].endsWith("}")) continue;
            if (!pat[i].equals(pSeg[i])) return false;
        }
        return true;
    }
}
