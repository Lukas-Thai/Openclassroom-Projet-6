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
import jakarta.persistence.EntityNotFoundException;
import mddapi.dto.ArticleRequest;
import mddapi.dto.ArticleResponse;
import mddapi.model.User;
import mddapi.services.ArticlesService;
import mddapi.services.UserService;

/**
 * Contrôleur REST pour la gestion des articles.
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticlesService artserv;
    private final UserService userserv;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param as service de gestion des articles
     * @param us service de gestion des utilisateurs
     */
    @Autowired
    public ArticleController(ArticlesService as, UserService us) {
        artserv = as;
        userserv = us;
    }

    /**
     * Récupère l'actualité d'un utilisateur en fonction de ses abonnements.
     *
     * @param auth authentification utilisateur
     * @return liste des articles
     */
    @Operation(summary = "Display all article depending on theme subscription")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<Map<String,Object>> fetchActualite(Authentication auth){
        if(auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }
        User user = userserv.findByEmail(auth.getName());
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }
        List<ArticleResponse> filActu = artserv.fetchActualite(user.getIdUser());
        return ResponseEntity.ok(Map.of("articles",filActu));
    }

    /**
     * Récupère un article par son identifiant.
     *
     * @param auth authentification utilisateur
     * @param id identifiant de l'article
     * @return article trouvé
     */
    @Operation(summary = "Get an article by its ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> fetchArticleById(Authentication auth,@PathVariable Integer id){
        if(id==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "You must provide an id"));
        }
        try {
            ArticleResponse article = this.artserv.fetchArticle(id);
            return ResponseEntity.ok(Map.of("article",article));
        }
        catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "The article with the provided id does not exist"));
        }
    }

    /**
     * Crée un nouvel article.
     *
     * @param auth authentification utilisateur
     * @param article données de l'article
     * @return message de confirmation
     */
    @Operation(summary = "Create an article")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping
    public ResponseEntity<Map<String,String>> createArticle(Authentication auth, @RequestBody ArticleRequest article){
        if(auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
        }
        if(article.getTitle().trim().isBlank() || article.getContenu().trim().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Le titre ou le contenu ne peut pas être vide"));
        }
        User user = userserv.findByEmail(auth.getName());
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
        }
        try {
            artserv.createArticle(article.getId_theme(), user.getIdUser(), article.getTitle(), article.getContenu());
            return ResponseEntity.ok(Map.of("message","article created"));
        }catch(EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","the theme does not exist"));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","The creation of the article failed"));
        }
    }
}
