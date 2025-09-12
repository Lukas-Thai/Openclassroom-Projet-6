package mddapi.services;

import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mddapi.model.User;
import mddapi.model.UserRepository;

/**
 * Service de gestion des utilisateurs.
 */
@Service
public class UserService {
    private UserRepository usersRepository;
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructeur.
     */
    public UserService(UserRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Recherche un utilisateur par email.
     */
    public User findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

    /**
     * Recherche un utilisateur par identifiant.
     */
    public User findById(Integer id) {
        return usersRepository.findById(id).orElse(null);
    }

    /**
     * Recherche un utilisateur par username.
     */
    public User findByUsername(String Username) {
        return usersRepository.findByUsername(Username).orElse(null);
    }

    /**
     * Enregistre un nouvel utilisateur en base avec validation et hashage du mot de passe.
     *
     * @param user utilisateur à créer
     * @return utilisateur créé
     * @throws RuntimeException si l’email ou le username est déjà utilisé
     */
    public User registerUser(User user) {
        if (usersRepository.findByEmail(user.getEmail().trim()).isPresent()) {
            throw new RuntimeException("L'email est déjà pris");
        }
        if (usersRepository.findByUsername(user.getUsername().trim()).isPresent()) {
            throw new RuntimeException("L'username est déjà pris");
        }
        user.setEmail(user.getEmail().trim());
        user.setUsername(user.getUsername().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated_at(LocalDateTime.now());
        return usersRepository.save(user);
    }

    /**
     * Met à jour les informations d’un utilisateur existant.
     *
     * @param oldUser     utilisateur actuel
     * @param updatedUser utilisateur contenant les nouvelles infos
     * @return utilisateur mis à jour
     * @throws RuntimeException si l’email ou l’username est déjà pris
     */
    public User updateUser(User oldUser, User updatedUser) {
        if (!updatedUser.getEmail().isBlank()) {
            if (oldUser.getEmail().compareTo(updatedUser.getEmail()) != 0) {
                if (usersRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                    throw new RuntimeException("L'email est déjà pris");
                }
                oldUser.setEmail(updatedUser.getEmail());
            }
        }
        if (!updatedUser.getUsername().isBlank()) {
            if (oldUser.getUsername().compareTo(updatedUser.getUsername()) != 0) {
                if (usersRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
                    throw new RuntimeException("L'username est déjà pris");
                }
                oldUser.setUsername(updatedUser.getUsername());
            }
        }
        if (!updatedUser.getPassword().isBlank()) {
            oldUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return usersRepository.save(oldUser);
    }
}
