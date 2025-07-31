package mddapi.model;

import jakarta.persistence.*;

@Entity
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_theme;

    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId_theme() {
		return id_theme;
	}

	public void setId_theme(Integer id_theme) {
		this.id_theme = id_theme;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}