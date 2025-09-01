package mddapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import mddapi.model.User;
import mddapi.services.CommentairesService;
import mddapi.services.UserService;

@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {
	@Autowired
	private UserService userserv; 
	@Autowired
	private CommentairesService comserv;
	@Autowired
	public CommentaireController(UserService us, CommentairesService cs) {
		userserv = us;
		comserv = cs;
	}
	@Operation(summary = "Get all commentary from an article with its id")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	public ResponseEntity<Map<String,Object>> fetchAllCommentaryFromArticle(@PathVariable Integer id){
		return ResponseEntity.ok(Map.of("commentaire",comserv.fetchCommentaireByArticle(id)));
	}
	@Operation(summary = "Get all commentary from an article with its id")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	public ResponseEntity<Map<String,String>> createCommentForArticle(Authentication auth,@PathVariable Integer id,@RequestParam String contenu){
		if(auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		if(contenu.trim() == "") {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Le commentaire ne peut pas être vide"));
		}
		String email = auth.getName();
		User user = userserv.findByEmail(email);
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
