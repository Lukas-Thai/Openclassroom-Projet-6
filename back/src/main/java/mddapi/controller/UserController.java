package mddapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mddapi.dto.UserRequest;
import mddapi.helper.regexClass;
import mddapi.model.User;
import mddapi.services.JWTService;
import mddapi.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	private UserService userserv;
	@Autowired JWTService jwtserv;
	public UserController(UserService us,JWTService jwt) {
		userserv = us;
		jwtserv=jwt;
	}
	@Operation(summary = "Allow the user to login into their account")
	@SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/update")
	public ResponseEntity<Map<String,String>> updateUser(Authentication auth, @RequestBody UserRequest request){
		if(auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Authorization invalid"));
		}
		String authEmail = auth.getName();
		User user = userserv.findByEmail(authEmail);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Unknown or deleted user"));
		}
		String email = request.getEmail()!=null ? request.getEmail() : "";
		String username = request.getUsername()!=null ? request.getUsername() : "";
		String password = request.getPassword()!=null ? request.getPassword() : "";
		User newUser = new User();
		if(!email.trim().isBlank() && authEmail!=email.trim()) {
			if(!regexClass.checkEmail(email)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "L'email n'est pas au format d'une adresse mail"));
			}
			newUser.setEmail(email);
		}
		else {
			newUser.setEmail("");
		}
		if(user.getUsername()!=username.trim()) {
			newUser.setUsername(username.trim());
		}
		else {
			newUser.setUsername("");
		}
		newUser.setPassword("");
		if(!password.isBlank()) {
	        if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(
	                password, user.getPassword())) {
	    		if(!regexClass.checkPassword(password)) {
	    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Le mot de passe ne répond pas à toutes les exigeances de sécurité (1 majuscule, 1 minuscule, 1 chiffre, 1 caractère spécial, 8+ caractères)"));
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
