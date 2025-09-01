package mddapi.model;

import jakarta.persistence.*;

@Entity
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_theme")
    private Integer idTheme;

    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdTheme() {
		return idTheme;
	}

	public void setIdTheme(Integer idTheme) {
		this.idTheme = idTheme;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}