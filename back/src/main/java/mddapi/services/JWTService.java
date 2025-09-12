package mddapi.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import mddapi.model.User;

/**
 * Service de gestion des tokens JWT.
 */
@Service
public class JWTService {
    private JwtEncoder jwtEncode;
    private JwtDecoder jwtDecode;

    /**
     * Constructeur.
     */
    public JWTService(JwtEncoder jwt, JwtDecoder jwtDecode) {
        this.jwtEncode = jwt;
        this.jwtDecode = jwtDecode;
    }

    /**
     * Génère un token JWT pour un utilisateur donné.
     *
     * @param user utilisateur concerné
     * @return token JWT signé
     */
    public String generateTokenForUser(User user) {
        Instant now = Instant.now();
        String subject = user.getEmail();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(3, ChronoUnit.DAYS))
                .subject(subject)
                .build();

        JwtEncoderParameters jwtParams = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        );

        return this.jwtEncode.encode(jwtParams).getTokenValue();
    }

    /**
     * Vérifie la validité d’un token et retourne son sujet.
     *
     * @param token token JWT
     * @return l’adresse email contenue dans le token si valide, sinon chaîne vide
     */
    public String verifyToken(String token) {
        try {
            Jwt decodedJwt = jwtDecode.decode(token);
            return decodedJwt.getSubject();
        } catch (Exception e) {
            return "";
        }
    }
}
