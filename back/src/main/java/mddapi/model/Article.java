package mddapi.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_article;

    @ManyToOne
    @JoinColumn(name = "id_theme", nullable = false)
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime date_creation;

	public Integer getId_article() {
		return id_article;
	}

	public void setId_article(Integer id_article) {
		this.id_article = id_article;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public LocalDateTime getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(LocalDateTime date_creation) {
		this.date_creation = date_creation;
	}

}