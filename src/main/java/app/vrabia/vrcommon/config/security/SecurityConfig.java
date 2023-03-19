package app.vrabia.vrcommon.config.security;

import app.vrabia.vrcommon.models.security.PublicEndpoints;
import app.vrabia.vrcommon.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JWTService jwtService;
    private final PublicEndpoints publicEndpoints;
    private final FilterChainExceptionHandlerFilter filterChainExceptionHandlerFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
        );

        publicEndpoints.getEndpoints().forEach(endpoint -> {
            try {
                log.info("Adding public endpoint: {}", endpoint);
                http.authorizeHttpRequests(requests -> requests.requestMatchers(endpoint).permitAll());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        http.authorizeHttpRequests().anyRequest().authenticated();

        http.addFilterBefore(new AuthorizationFilter(jwtService, publicEndpoints), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandlerFilter, AuthorizationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
