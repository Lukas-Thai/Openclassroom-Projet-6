package mddapi.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_commentaire;

    @ManyToOne
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private LocalDateTime date_commentaire;

	public Integer getId_commentaire() {
		return id_commentaire;
	}

	public void setId_commentaire(Integer id_commentaire) {
		this.id_commentaire = id_commentaire;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public LocalDateTime getDate_commentaire() {
		return date_commentaire;
	}

	public void setDate_commentaire(LocalDateTime date_commentaire) {
		this.date_commentaire = date_commentaire;
	}

    	
}