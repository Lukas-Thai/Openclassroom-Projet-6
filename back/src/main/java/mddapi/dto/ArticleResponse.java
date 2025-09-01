package mddapi.dto;

import java.time.LocalDateTime;

public class ArticleResponse {
	private Integer id_article;
	private String nom_theme;
	private String author;
	private String title; 
	private String content;
	private LocalDateTime created_at;
	public Integer getId_article() {
		return id_article;
	}
	public void setId_article(Integer id_article) {
		this.id_article = id_article;
	}
	public String getNom_theme() {
		return nom_theme;
	}
	public void setNom_theme(String nom_theme) {
		this.nom_theme = nom_theme;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
}
