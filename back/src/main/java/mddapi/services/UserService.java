package mddapi.services;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mddapi.model.User;
import mddapi.model.UserRepository;






@Service
public class UserService {
    private UserRepository usersRepository;

    private BCryptPasswordEncoder passwordEncoder;
    
    public UserService(UserRepository usersRepository,BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User findByEmail(String email) {//cherche un utilisateur à partir de son email
        return usersRepository.findByEmail(email).orElseGet(null);
    }
    public User findById(Integer id) {//cherche un utilisateur à partir de son id
        return usersRepository.findById(id).orElseGet(null);
    }
    public User registerUser(User user) {//enregistre un utilisateur en bd
    	user.setEmail(user.getEmail().trim());
    	user.setUsername(user.getUsername().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword()));//hashage du mnot de passe 
        user.setCreated_at(LocalDateTime.now());
        return usersRepository.save(user);
    }
    public User updateUser(User oldUser,User updatedUser) {
    	if(updatedUser.getEmail() != "") {
    		if(usersRepository.findByEmail(updatedUser.getEmail()).orElseGet(null)!=null) {
    			throw new RuntimeException("L'email est déjà pris");
    		}
    		oldUser.setEmail(updatedUser.getEmail());
    	}
    	if(updatedUser.getUsername() != "") {
    		if(usersRepository.findByUsername(updatedUser.getUsername()).orElseGet(null)!=null) {
    			throw new RuntimeException("L'username est déjà pris");
    		}
    	}
    	if(updatedUser.getPassword()!="") {
    		oldUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    	}
    	return usersRepository.save(oldUser);
    }
}
