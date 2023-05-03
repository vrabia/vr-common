package app.vrabia.vrcommon.config.security;

import app.vrabia.vrcommon.exception.ErrorCodes;
import app.vrabia.vrcommon.exception.VrabiaException;
import app.vrabia.vrcommon.models.security.PublicEndpoints;
import app.vrabia.vrcommon.service.JWTService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String SWAGGER_UI_BASE_PATH = "/swagger-ui";
    private static final String FAVICON_URL = "/favicon.ico";
    private static final String OPEN_API_V3 = "/v3/api-docs";

    private final JWTService jwtService;
    private final PublicEndpoints publicRestEndpoints;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldValidateAuthorizationHeader(request)) {
            validateAuthorizationHeader(request);
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldValidateAuthorizationHeader(HttpServletRequest request) {
        if("OPTIONS".equals(request.getMethod())) {
            return false;
        }
        if (request.getServletPath().startsWith(SWAGGER_UI_BASE_PATH)) {
            return false;
        }
        if (request.getServletPath().startsWith(OPEN_API_V3)) {
            return false;
        }
        if (request.getServletPath().equals(FAVICON_URL)) {
            return false;
        }

        return !publicRestEndpoints.getEndpoints().contains(request.getServletPath());
    }

    private void validateAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (containsBearerHeader(authorizationHeader)) {
            String jwt = extractBearerToken(authorizationHeader);
            DecodedJWT decodedJWT = decodeJWT(jwt);
            String username = jwtService.getUsername(decodedJWT);
            List<SimpleGrantedAuthority> authorities = jwtService.getAuthorities(decodedJWT);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            throw new VrabiaException(ErrorCodes.MISSING_AUTHORIZATION_HEADER);
        }
    }

    private boolean containsBearerHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return !extractBearerToken(authorizationHeader).isEmpty();
        }
        return false;
    }

    private String extractBearerToken(String authorizationHeader) {
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    private DecodedJWT decodeJWT(String jwt) {
        try {
            return jwtService.decodeJWT(jwt);
        } catch (TokenExpiredException ex) {
            log.error("Token expired: {}", jwt);
            throw new VrabiaException(ErrorCodes.TOKEN_EXPIRED);
        } catch (JWTVerificationException ex) {
            log.error("Token not valid: {}", jwt);
            throw new VrabiaException(ErrorCodes.TOKEN_INVALID);
        }
    }

}
