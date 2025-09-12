package mddapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mddapi.model.Theme;
import mddapi.model.User;
import mddapi.services.AbonnementsService;
import mddapi.services.ThemesService;
import mddapi.services.UserService;

/**
 * Contrôleur REST pour la gestion des thèmes et abonnements.
 */
@RestController
@RequestMapping("/api/theme")
public class ThemeController {

    private final ThemesService themeserv;
    private final AbonnementsService abonserv;
    private final UserService userserv;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param ts service de gestion des thèmes
     * @param as service de gestion des abonnements
     * @param us service utilisateur
     */
    @Autowired
    public ThemeController(ThemesService ts, AbonnementsService as,UserService us) {
        themeserv=ts;
        abonserv=as;
        userserv=us;
    }

    /**
     * Récupère tous les thèmes disponibles.
     *
     * @return liste des thèmes
     */
    @Operation(summary = "Fetch all theme stored in the database")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<Map<String,List<Theme>>> fetchAllTheme(){
        List<Theme> fetchResult = themeserv.getAllTheme();
        return ResponseEntity.ok(Map.of("themes",fetchResult));
    }

    /**
     * Permet de s'abonner ou se désabonner à un thème.
     *
     * @param auth authentification utilisateur
     * @param id identifiant du thème
     * @return message de succès ou d'échec
     */
    @Operation(summary = "Subscribe or unsubscribe from a theme")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/subscribe/{id}")
    public ResponseEntity<Map<String,String>> subscribeOrUnsubscribeTheme(Authentication auth,@PathVariable Integer id){
        if(auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }
        User user = userserv.findByEmail(auth.getName());
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }
        try {
            boolean result = abonserv.SubscribeOrUnsubscribe(id, user.getIdUser());
            return ResponseEntity.ok(Map.of("message", result ? "subscribe success" : "unsubscribe success"));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","the operation failed"));
        }
    }
}
