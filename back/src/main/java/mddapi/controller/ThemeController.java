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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mddapi.model.Theme;
import mddapi.model.User;
import mddapi.services.AbonnementsService;
import mddapi.services.ThemesService;
import mddapi.services.UserService;

@RestController
@RequestMapping("/api/theme")
public class ThemeController {
	@Autowired
	private ThemesService themeserv;
	@Autowired
	private AbonnementsService abonserv;
	@Autowired 
	UserService userserv;
	@Autowired
	public ThemeController(ThemesService ts, AbonnementsService as,UserService us) {
		themeserv=ts;
		abonserv=as;
		userserv=us;
	}
	@Operation(summary = "Fetch all theme stored in the database")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping
	public ResponseEntity<Map<String,List<Theme>>> fetchAllTheme(){
		List<Theme> fetchResult = themeserv.getAllTheme();
		return ResponseEntity.ok(Map.of("themes",fetchResult));
	}
	@Operation(summary = "Subscribe or unsubscribe from a theme")
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/subscribe/{id}")
	public ResponseEntity<Map<String,String>> subscribeOrUnsubscribeTheme(Authentication auth,@PathVariable Integer id){
		if(auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		String email = auth.getName();
		User user = userserv.findByEmail(email);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
		}try {
			boolean result = abonserv.SubscribeOrUnsubscribe(id, user.getIdUser());
			if(result) {
				return ResponseEntity.ok(Map.of("message","subscribe success"));
			}
			else {
				return ResponseEntity.ok(Map.of("message","unsubscribe success"));
			}
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","the operation failed"));
		}

	}
}
