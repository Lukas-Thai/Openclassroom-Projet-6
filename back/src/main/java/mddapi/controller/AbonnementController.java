package mddapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mddapi.model.User;
import mddapi.services.AbonnementsService;
import mddapi.services.UserService;

/**
 * Contrôleur REST pour la gestion des abonnements d'un utilisateur.
 * 
 * <p>Ce contrôleur permet de récupérer tous les abonnements d’un utilisateur connecté
 * via son token JWT.</p>
 */
@RestController
@RequestMapping("/api/abonnement")
public class AbonnementController {

    @Autowired
    private UserService userserv;

    @Autowired
    private AbonnementsService abonserv;

    /**
     * Constructeur du contrôleur.
     *
     * @param us service de gestion des utilisateurs
     * @param as service de gestion des abonnements
     */
    public AbonnementController(UserService us, AbonnementsService as) {
        userserv = us;
        abonserv = as;
    }

    /**
     * Récupère tous les abonnements de l’utilisateur authentifié.
     *
     * @param auth l’objet {@link Authentication} contenant les infos de l’utilisateur connecté
     * @return une réponse HTTP contenant soit :
     *         <ul>
     *             <li>Code 200 et la liste des abonnements</li>
     *             <li>Code 401 si l’utilisateur n’est pas authentifié</li>
     *             <li>Code 404 si l’utilisateur est inconnu ou supprimé</li>
     *         </ul>
     */
    @Operation(summary = "Return all the subscription of the user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAbonnement(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }

        String email = auth.getName();
        User user = userserv.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }

        List<Integer> result = abonserv.getUserAbonnement(user);
        return ResponseEntity.ok(Map.of("subscriptions", result));
    }
}
