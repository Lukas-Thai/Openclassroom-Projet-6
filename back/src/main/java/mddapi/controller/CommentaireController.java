package mddapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import mddapi.model.User;
import mddapi.services.CommentairesService;
import mddapi.services.UserService;

/**
 * Contrôleur REST pour la gestion des commentaires.
 */
@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {

    private final UserService userserv;
    private final CommentairesService comserv;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param us service utilisateur
     * @param cs service commentaire
     */
    @Autowired
    public CommentaireController(UserService us, CommentairesService cs) {
        userserv = us;
        comserv = cs;
    }

    /**
     * Récupère tous les commentaires liés à un article.
     *
     * @param id identifiant de l'article
     * @return liste des commentaires
     */
    @Operation(summary = "Get all commentary from an article with its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> fetchAllCommentaryFromArticle(@PathVariable Integer id){
        if(id==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","The request must include the id of the article"));
        }
        return ResponseEntity.ok(Map.of("commentaires",comserv.fetchCommentaireByArticle(id)));
    }

    /**
     * Crée un commentaire pour un article.
     *
     * @param auth authentification utilisateur
     * @param id identifiant de l'article
     * @param contenu contenu du commentaire
     * @return message de confirmation
     */
    @Operation(summary = "Create commentary for an article")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/create/{id}")
    public ResponseEntity<Map<String,String>> createCommentForArticle(Authentication auth,@PathVariable Integer id,@RequestParam String contenu){
        if(auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }
        if (contenu == null || contenu.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Le commentaire ne peut pas être vide"));
        }
        User user = userserv.findByEmail(auth.getName());
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }
        try {
            comserv.createCommentaire(id, user.getIdUser(), contenu);
            return ResponseEntity.ok(Map.of("message", "Commentary created"));
        }catch(EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","the article does not exist"));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","The creation of the commentary failed"));
        }
    }
}
