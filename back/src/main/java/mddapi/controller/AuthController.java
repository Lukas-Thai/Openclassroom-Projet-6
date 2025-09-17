package mddapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mddapi.dto.LoginRequest;
import mddapi.dto.UserResponse;
import mddapi.helper.InfoBuilder;
import mddapi.helper.regexClass;
import mddapi.model.User;
import mddapi.services.JWTService;
import mddapi.services.UserService;

/**
 * Contrôleur REST gérant l'authentification et l'inscription des utilisateurs.
 * <p>
 * Fournit des endpoints pour :
 * <ul>
 *   <li>Inscrire un nouvel utilisateur</li>
 *   <li>Se connecter et obtenir un token JWT</li>
 *   <li>Récupérer les informations du profil connecté</li>
 *   <li>Vérifier la validité d’un token JWT</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    /**
     * Constructeur injectant les dépendances nécessaires.
     *
     * @param userService service de gestion des utilisateurs
     * @param jwt         service de gestion des tokens JWT
     */
    @Autowired
    public AuthController(UserService userService, JWTService jwt) {
        this.userService = userService;
        this.jwtService = jwt;
    }

    /**
     * Enregistre un nouvel utilisateur si les informations sont valides
     * (username non vide, email correct, mot de passe conforme).
     * <p>
     * Retourne un token JWT si l’inscription réussit.
     *
     * @param user l'utilisateur à créer
     * @return {@code 200 OK} avec un token si succès,
     *         {@code 400 BAD REQUEST} si les données sont invalides,
     *         {@code 409 CONFLICT} si l'email ou le nom d'utilisateur est déjà pris
     */
    @PostMapping("/register")
    @Operation(summary = "Allow the user to register an account")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getUsername().length() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "L'username ne peut pas être vide"));
            }
            if (!regexClass.checkEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "L'email n'est pas au bon format"));
            }
            if (!regexClass.checkPassword(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Le mot de passe ne répond pas à toutes les exigeances de sécurité"));
            }
            User RegisteredUser = userService.registerUser(user);
            String token = jwtService.generateTokenForUser(RegisteredUser);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Authentifie un utilisateur à partir de son identifiant (email ou username)
     * et de son mot de passe.
     * <p>
     * Retourne un token JWT si les informations sont correctes.
     *
     * @param loginRequest objet contenant l'identifiant et le mot de passe
     * @return {@code 200 OK} avec un token JWT si succès,
     *         {@code 400 BAD REQUEST} si identifiants incorrects
     */
    @Operation(summary = "Allow the user to login into their account")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getIdentifier());
        if (user == null) {
            user = userService.findByUsername(loginRequest.getIdentifier());
        }
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "L'email ou le mot de passe est erroné"));
        }

        if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(
                loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "L'email ou le mot de passe est erroné"));
        }

        String token = jwtService.generateTokenForUser(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Récupère les informations de l’utilisateur connecté en se basant sur le token JWT.
     *
     * @param auth objet d'authentification fourni par Spring Security
     * @return {@code 200 OK} avec les informations de l'utilisateur,
     *         {@code 401 UNAUTHORIZED} si l'utilisateur n'est pas connecté,
     *         {@code 404 NOT FOUND} si l'utilisateur n'existe pas
     */
    @Operation(summary = "Fetch the information of the current user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = auth.getName();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserResponse userInfo = InfoBuilder.userInfoBuilder(user);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Vérifie si le token JWT fourni est encore valide.
     *
     * @param auth objet d'authentification fourni par Spring Security
     * @return {@code 200 OK} avec {"valid": true} si valide,
     *         {"valid": false} sinon
     */
    @Operation(summary = "Return if a JWT Token is still valid or not")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkToken(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
        String email = auth.getName();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
        return ResponseEntity.ok(Map.of("valid", true));
    }
}
