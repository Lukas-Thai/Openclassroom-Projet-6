package mddapi.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_abonnement;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_theme", nullable = false)
    private Theme theme;

    // Getters and Setters
    public Integer getId_abonnement() {
        return id_abonnement;
    }

    public void setId_abonnement(Integer id_abonnement) {
        this.id_abonnement = id_abonnement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

}