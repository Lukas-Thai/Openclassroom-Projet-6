package mddapi.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mddapi.model.Article;
import mddapi.model.ArticleRepository;
import mddapi.model.Commentaire;
import mddapi.model.CommentaireRepository;
import mddapi.model.User;
import mddapi.model.UserRepository;

@Service
public class CommentairesService {
	private CommentaireRepository comRep;
	private ArticleRepository artRep;
	private UserRepository userRep;
	
	public List<Commentaire> fetchCommentaireByArticle(Integer id_article){
		return comRep.findByArticle_IdArticle(id_article);
	}
	public void createCommentaire(Integer id_article, Integer id_user, String comment) {
		Article article = artRep.findById(id_article).orElse(null);
		User user = userRep.findById(id_user).orElse(null);
		if(article == null || user == null) {
			throw new EntityNotFoundException();
		}
		Commentaire toSave = new Commentaire();
		toSave.setContenu(comment);
		toSave.setArticle(article);
		toSave.setDate_commentaire(LocalDateTime.now());
		toSave.setUser(user);
		comRep.save(toSave);
	}
}
