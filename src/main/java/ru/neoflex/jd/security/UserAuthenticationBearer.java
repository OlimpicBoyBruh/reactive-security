package ru.neoflex.jd.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAuthenticationBearer {

    public static Mono<UsernamePasswordAuthenticationToken> create(JwtHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.claims;
        String subject = claims.getSubject();

        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));


        Long principalId = Long.parseLong(subject);
        CustomPrincipal customPrincipal = new CustomPrincipal(principalId, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(customPrincipal, null, authorities));
    }
}
