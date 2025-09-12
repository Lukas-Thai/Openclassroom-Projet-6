package mddapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mddapi.dto.UserRequest;
import mddapi.helper.InfoBuilder;
import mddapi.helper.regexClass;
import mddapi.model.User;
import mddapi.services.JWTService;
import mddapi.services.UserService;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userserv;
    private final JWTService jwtserv;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param us service utilisateur
     * @param jwt service JWT
     */
    @Autowired
    public UserController(UserService us,JWTService jwt) {
        userserv = us;
        jwtserv=jwt;
    }

    /**
     * Récupère les informations de l'utilisateur courant.
     *
     * @param auth authentification utilisateur
     * @return informations utilisateur
     */
    @Operation(summary="Retrieve current information about the logged user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping()
    public ResponseEntity<Map<String,Object>> getUser(Authentication auth){
        if(auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }
        User user = userserv.findByEmail(auth.getName());
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }
        return ResponseEntity.ok(Map.of("user",InfoBuilder.userInfoBuilder(user)));
    }

    /**
     * Met à jour les informations de l'utilisateur.
     *
     * @param auth authentification utilisateur
     * @param request nouvelle requête utilisateur
     * @return nouveau token JWT si succès
     */
    @Operation(summary = "Update user information")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/update")
    public ResponseEntity<Map<String,String>> updateUser(Authentication auth, @RequestBody UserRequest request){
        if(auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }
        User user = userserv.findByEmail(auth.getName());
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }

        String email = request.getEmail()!=null ? request.getEmail() : "";
        String username = request.getUsername()!=null ? request.getUsername() : "";
        String password = request.getPassword()!=null ? request.getPassword() : "";

        User newUser = new User();

        if(!email.trim().isBlank() && !auth.getName().equals(email.trim())) {
            if(!regexClass.checkEmail(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "L'email n'est pas au format d'une adresse mail"));
            }
            newUser.setEmail(email);
        } else {
            newUser.setEmail("");
        }

        if(!user.getUsername().equals(username.trim())) {
            newUser.setUsername(username.trim());
        } else {
            newUser.setUsername("");
        }

        newUser.setPassword("");
        if(!password.isBlank()) {
            if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(password, user.getPassword())) {
                if(!regexClass.checkPassword(password)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Le mot de passe ne répond pas aux exigences de sécurité"));
                }
                newUser.setPassword(password);
            }
        }

        try {
            User updatedUser=userserv.updateUser(user,newUser);
            String token = jwtserv.generateTokenForUser(updatedUser);
            return ResponseEntity.ok(Map.of("token",token));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message",e.getMessage()));
        }
    }
}
