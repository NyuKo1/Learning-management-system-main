package kz.sec.lms.auth.config;

import kz.sec.lms.auth.security.AuthTokenFilter;
import kz.sec.lms.auth.sso.SsoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static kz.sec.lms.shared.security.SecurityUtils.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SsoProperties ssoProperties;

    public SecurityConfig(SsoProperties ssoProperties) {
        this.ssoProperties = ssoProperties;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Build allowed origins from SsoProperties (comma-separated string)
        // plus localhost dev origins. Falls back to permissive patterns if
        // nothing is configured (legacy behaviour).
        Set<String> origins = new LinkedHashSet<>();
        String configured = ssoProperties.getAllowedOrigins();
        if (configured != null && !configured.isBlank()) {
            Arrays.stream(configured.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(origins::add);
        }
        origins.add("http://localhost:4200");
        origins.add("http://localhost:4201");
        origins.add("http://localhost:3000");

        config.setAllowedOrigins(new ArrayList<>(origins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthTokenFilter authTokenFilter)
            throws Exception {
        return http
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/actuator/**",
                        "/docs/**",
                        "/refresh",
                        "/branding").permitAll()
                .antMatchers(
                        HttpMethod.POST,
                        "/login").anonymous()
                .antMatchers(HttpMethod.GET, "/sso/authorize").authenticated()
                .antMatchers(HttpMethod.POST, "/sso/token").permitAll()
                .antMatchers(HttpMethod.GET, "/sso/verify").authenticated()
                .antMatchers(
                        HttpMethod.GET,
                        "/users/username/*/id",
                        "/users/**/public").permitAll()
                .antMatchers(
                        HttpMethod.GET,
                        "/users/username/*").authenticated()
                .antMatchers(HttpMethod.POST, "/users/register-student").hasAuthority(ROLE_ADMIN)
                .antMatchers("/users/**").hasAuthority(ROLE_ADMIN)
                .anyRequest().hasAuthority(ROLE_ROOT)
                .and()
                .build();
    }
}
