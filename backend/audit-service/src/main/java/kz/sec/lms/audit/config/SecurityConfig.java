package kz.sec.lms.audit.config;

import kz.sec.lms.shared.security.AuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static kz.sec.lms.shared.security.SecurityUtils.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationTokenFilter authenticationTokenFilter)
            throws Exception {
        return http
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/actuator/**", "/docs/**").permitAll()
                // POST /audit/events authenticates via X-Internal-Token (checked in the controller)
                .antMatchers(HttpMethod.POST, "/audit/events").permitAll()
                .antMatchers(HttpMethod.GET, "/audit/**").hasAnyAuthority(ROLE_ADMIN, ROLE_ROOT)
                .anyRequest().hasAuthority(ROLE_ROOT)
                .and()
                .build();
    }
}
