package app.vrabia.vrcommon.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JWTServiceImpl implements JWTService {
    private static final String ROLES_CLAIM = "roles";

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpirationTimeMilis:360000000}")
    private int accessTokenExpirationTimeMilis;

    @Value("${jwt.config.refreshTokenExpirationTimeMilis:86400000}")
    private int refreshTokenExpirationTimeMilis;

    public String createAccessToken(String username, List<String> roles) {
        return JWT
                .create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationTimeMilis))
                .withIssuer(issuer)
                .withClaim(ROLES_CLAIM, roles)
                .sign(getAlgorithm());
    }

    public String createRefreshToken(String username) {
        return JWT
                .create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationTimeMilis))
                .withIssuer(issuer)
                .sign(getAlgorithm());
    }

    public DecodedJWT decodeJWT(String token) {
        return JWT
                .require(getAlgorithm())
                .build()
                .verify(token);
    }

    public String getUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public List<SimpleGrantedAuthority> getAuthorities(DecodedJWT decodedJWT) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        String[] roles = decodedJWT.getClaim(ROLES_CLAIM).asArray(String.class);
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }
}
