package mddapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import mddapi.dto.ArticleRequest;
import mddapi.dto.ArticleResponse;
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
		if(auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		String email = auth.getName();
		User user = userserv.findByEmail(email);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
		}
		List<ArticleResponse> filActu = artserv.fetchActualite(user.getIdUser());
		return ResponseEntity.ok(Map.of("articles",filActu));
	}
	@Operation(summary = "Display all article depending on theme subscription")
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
	@Operation(summary = "Create an article")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping
	public ResponseEntity<Map<String,String>> createArticle(Authentication auth, @RequestBody ArticleRequest article){
		if(auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		String title= article.getTitle();
		String contenu = article.getContenu();
		Integer id_theme=article.getId_theme();
		if(title.trim().isBlank() || contenu.trim().isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Le titre ou le contenu ne peut pas être vide"));
		}
		String email = auth.getName();
		User user = userserv.findByEmail(email);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
		}
		try {
			artserv.createArticle(id_theme, user.getIdUser(), title, contenu);
			return ResponseEntity.ok(Map.of("message","article created"));
		}catch(EntityNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","the theme does not exist"));
		}catch(Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","The creation of the article failed"));
		}
	}
}
