package mddapi.dto;

public class ArticleRequest {
	private Integer id_theme;
	private String title;
	private String contenu;
	public Integer getId_theme() {
		return id_theme;
	}
	public void setId_theme(Integer id_theme) {
		this.id_theme = id_theme;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
}
