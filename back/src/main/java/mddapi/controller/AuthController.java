package mddapi.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import mddapi.model.User;
import mddapi.services.UserService;
import mddapi.services.JWTService;

/**
 * Contrôleur pour la gestion de l'authentification et de l'inscription des utilisateurs.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JWTService jwtService;

    /**
     * Constructeur avec injection des services.
     *
     * @param userService service utilisateur
     * @param jwtService  service de gestion des JWT
     */
    public AuthController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Enregistre un nouvel utilisateur et retourne son token JWT.
     *
     * @param user utilisateur à enregistrer
     * @return token JWT de l'utilisateur créé
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        String token = jwtService.generateTokenForUser(savedUser);
        return ResponseEntity.ok(token);
    }

    /**
     * Authentifie un utilisateur et retourne son token JWT.
     *
     * @param user utilisateur à authentifier
     * @return token JWT si l'authentification est réussie
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null && jwtService.verifyToken(jwtService.generateTokenForUser(existingUser)) != null) {
            String token = jwtService.generateTokenForUser(existingUser);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Identifiants invalides");
    }
}
