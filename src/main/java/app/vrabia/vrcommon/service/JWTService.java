package app.vrabia.vrcommon.service;

import app.vrabia.vrcommon.models.Role;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface JWTService {
    String createAccessToken(String username, List<Role> roles);
    String createRefreshToken(String username);
    DecodedJWT decodeJWT(String token);
    String getUsername(DecodedJWT decodedJWT);
    List<SimpleGrantedAuthority> getAuthorities(DecodedJWT decodedJWT);
}
