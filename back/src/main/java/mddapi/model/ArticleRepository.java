package mddapi.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer>{
	public Optional<Article> findById(Integer id_article);
	public List<Article> findByTheme(Integer id_theme);
	public List<Article> findByThemeIn(List<Theme> themes);
}
