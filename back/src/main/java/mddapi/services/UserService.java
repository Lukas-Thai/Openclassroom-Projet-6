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
        return usersRepository.findByEmail(email).orElse(null);
    }
    public User findById(Integer id) {//cherche un utilisateur à partir de son id
        return usersRepository.findById(id).orElse(null);
    }
    public User findByUsername(String Username) {
    	return usersRepository.findByUsername(Username).orElse(null);
    }
    public User registerUser(User user) {//enregistre un utilisateur en bd
    	if(usersRepository.findByEmail(user.getEmail().trim()).isPresent()) {
			throw new RuntimeException("L'email est déjà pris");
		}
    	if(usersRepository.findByUsername(user.getUsername().trim()).isPresent()) {
			throw new RuntimeException("L'username est déjà pris");
		}
    	user.setEmail(user.getEmail().trim());
    	user.setUsername(user.getUsername().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword()));//hashage du mnot de passe 
        user.setCreated_at(LocalDateTime.now());
        return usersRepository.save(user);
    }
    public User updateUser(User oldUser,User updatedUser) {
    	if(!updatedUser.getEmail().isBlank()) {
    		if(oldUser.getEmail().compareTo(updatedUser.getEmail())!=0)
    		{
        		if(usersRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
        			throw new RuntimeException("L'email est déjà pris");
        		}
        		oldUser.setEmail(updatedUser.getEmail());
    		}
    	}
    	if(!updatedUser.getUsername().isBlank()) {
    		if(oldUser.getUsername().compareTo(updatedUser.getUsername())!=0) {
        		if(usersRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
        			throw new RuntimeException("L'username est déjà pris");
        		}
        		oldUser.setUsername(updatedUser.getUsername());
    		}
    	}
    	if(!updatedUser.getPassword().isBlank()) {
    		oldUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    	}
    	return usersRepository.save(oldUser);
    }
}
