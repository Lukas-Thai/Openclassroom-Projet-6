package mddapi.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mddapi.dto.ArticleResponse;
import mddapi.model.Abonnement;
import mddapi.model.AbonnementRepository;
import mddapi.model.Article;
import mddapi.model.ArticleRepository;
import mddapi.model.Theme;
import mddapi.model.ThemeRepository;
import mddapi.model.User;
import mddapi.model.UserRepository;

@Service
public class ArticlesService {
	private ArticleRepository artRep;
	private UserRepository userRep;
	private ThemeRepository themeRep;
	private AbonnementRepository abonRep;
	public ArticlesService(ArticleRepository ar,UserRepository ur, ThemeRepository tr, AbonnementRepository abr) {
		artRep=ar;
		userRep = ur;
		themeRep = tr;
		abonRep = abr;
	}
	public List<ArticleResponse> fetchActualite(Integer id_user){
		List<Abonnement> listeAb = abonRep.findByUser_IdUser(id_user);
		if(listeAb.isEmpty()) {
			return List.of();
		}
	    List<Theme> themes = listeAb.stream()
                .map(Abonnement::getTheme)
                .toList();
	    List<Article> fetchedList = artRep.findByThemeIn(themes);
    	List<ArticleResponse> result = new ArrayList<>();
	    for(Article art: fetchedList) {
	    	ArticleResponse temp = new ArticleResponse();
	    	temp.setAuthor(art.getUser().getUsername());
	    	temp.setContent(art.getContenu());
	    	temp.setCreated_at(art.getDate_creation());
	    	temp.setId_article(art.getIdArticle());
	    	temp.setNom_theme(art.getTheme().getName());
	    	temp.setTitle(art.getTitre());
	    	result.add(temp);
	    }
	    return result;
	}
	public void createArticle(Integer id_theme, Integer id_user, String titre, String content) {
		Theme theme = themeRep.findById(id_theme).orElse(null);
		User user = userRep.findById(id_user).orElse(null);
		if(theme == null || user == null) {
			throw new EntityNotFoundException();
		}
		Article toSave = new Article();
		toSave.setContenu(content);
		toSave.setTitre(titre);
		toSave.setDate_creation(LocalDateTime.now());
		toSave.setTheme(theme);
		toSave.setUser(user);
		artRep.save(toSave);
	}
}
