package mddapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import mddapi.model.Article;
import mddapi.model.User;
import mddapi.services.ArticlesService;
import mddapi.services.UserService;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
	@Autowired
	private ArticlesService artserv;
	@Autowired
	private UserService userserv; 
	@Autowired
	public ArticleController(ArticlesService as, UserService us) {
		artserv=as;
		userserv=us;
	}
	
	@Operation(summary = "Display all article depending on theme subscription")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping
	public ResponseEntity<Map<String,Object>> fetchActualite(Authentication auth){
		if(auth==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		String email = auth.getName();
		User user = userserv.findByEmail(email);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
		}
		List<Article> filActu = artserv.fetchActualite(user.getId_user());
		return ResponseEntity.ok(Map.of("articles",filActu));
	}
	@Operation(summary = "Create an article")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping
	public ResponseEntity<Map<String,String>> createArticle(Authentication auth, @RequestParam Integer id_theme, @RequestParam String title, @RequestParam String contenu){
		if(auth==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		if(title.trim() == "" || contenu.trim() == "") {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Le titre ou le contenu ne peut pas être vide"));
		}
		String email = auth.getName();
		User user = userserv.findByEmail(email);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
		}
		try {
			artserv.createArticle(id_theme, id_theme, title, contenu);
			return ResponseEntity.ok(Map.of("message","article created"));
		}catch(EntityNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","the theme does not exist"));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","The creation of the article failed"));
		}
	}
}
