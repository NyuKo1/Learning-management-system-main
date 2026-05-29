package kz.sec.lms.auth.sso;

import kz.sec.lms.auth.dto.TokensDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sso")
@RequiredArgsConstructor
public class SsoController {

    private final SsoService ssoService;

    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize(
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        String code = ssoService.generateAuthorizationCode(clientId, redirectUri, username);

        String separator = redirectUri.contains("?") ? "&" : "?";
        String location = redirectUri + separator + "code=" + code;
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(location))
                .build();
    }

    @PostMapping("/token")
    public ResponseEntity<TokensDTO> token(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String clientId = body.get("client_id");
        String clientSecret = body.get("client_secret");
        String redirectUri = body.get("redirect_uri");

        TokensDTO tokens = ssoService.exchangeCode(code, clientId, clientSecret, redirectUri);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("username", authentication.getName());
        result.put("roles", getCurrentRoles());
        return ResponseEntity.ok(result);
    }

    private List<String> getCurrentRoles() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
