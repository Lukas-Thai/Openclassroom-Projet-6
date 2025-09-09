package mddapi.dto;

import java.time.LocalDateTime;

public class CommentaireResponse {
	private Integer id_commentaire;
	private Integer id_article;
	private String author;
	private String content;
	private LocalDateTime date;
	public Integer getId_commentaire() {
		return id_commentaire;
	}
	public void setId_commentaire(Integer id_commentaire) {
		this.id_commentaire = id_commentaire;
	}
	public Integer getId_article() {
		return id_article;
	}
	public void setId_article(Integer id_article) {
		this.id_article = id_article;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
