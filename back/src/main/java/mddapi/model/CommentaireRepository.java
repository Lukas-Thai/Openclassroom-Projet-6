package mddapi.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire,Integer>{
	public Optional<Commentaire> findById(Integer id_commentaire);
	public List<Commentaire> findByArticle(Integer id_article);
}
