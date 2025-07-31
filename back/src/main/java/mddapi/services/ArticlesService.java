package mddapi.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
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
	public List<Article> fetchActualite(Integer id_user){
		List<Abonnement> listeAb = abonRep.findByUser(id_user);
		if(listeAb.isEmpty()) {
			return List.of();
		}
	    List<Theme> themes = listeAb.stream()
                .map(Abonnement::getTheme)
                .toList();
	    return artRep.findByThemeIn(themes);
	}
	public void createArticle(Integer id_theme, Integer id_user, String titre, String content) {
		Theme theme = themeRep.findById(id_theme).orElseGet(null);
		User user = userRep.findById(id_user).orElseGet(null);
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
