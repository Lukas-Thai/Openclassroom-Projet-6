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

/**
 * Service de gestion des articles.
 */
@Service
public class ArticlesService {
    private ArticleRepository artRep;
    private UserRepository userRep;
    private ThemeRepository themeRep;
    private AbonnementRepository abonRep;

    /**
     * Constructeur.
     */
    public ArticlesService(ArticleRepository ar, UserRepository ur, ThemeRepository tr, AbonnementRepository abr) {
        artRep = ar;
        userRep = ur;
        themeRep = tr;
        abonRep = abr;
    }

    /**
     * Récupère les articles liés aux abonnements d’un utilisateur.
     *
     * @param id_user identifiant de l’utilisateur
     * @return liste de réponses contenant les articles
     */
    public List<ArticleResponse> fetchActualite(Integer id_user) {
        List<Abonnement> listeAb = abonRep.findByUser_IdUser(id_user);
        if (listeAb.isEmpty()) {
            return List.of();
        }
        List<Theme> themes = listeAb.stream()
                .map(Abonnement::getTheme)
                .toList();
        List<Article> fetchedList = artRep.findByThemeIn(themes);
        List<ArticleResponse> result = new ArrayList<>();
        for (Article art : fetchedList) {
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

    /**
     * Crée un nouvel article.
     *
     * @param id_theme identifiant du thème
     * @param id_user  identifiant de l’utilisateur
     * @param titre    titre de l’article
     * @param content  contenu de l’article
     * @throws EntityNotFoundException si le thème ou l’utilisateur n’existe pas
     */
    public void createArticle(Integer id_theme, Integer id_user, String titre, String content) {
        Theme theme = themeRep.findById(id_theme).orElse(null);
        User user = userRep.findById(id_user).orElse(null);
        if (theme == null || user == null) {
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

    /**
     * Récupère un article par son identifiant.
     *
     * @param id_article identifiant de l’article
     * @return DTO contenant les infos de l’article
     * @throws EntityNotFoundException si l’article n’existe pas
     */
    public ArticleResponse fetchArticle(Integer id_article) {
        Article art = artRep.findById(id_article).orElse(null);
        if (art == null) {
            throw new EntityNotFoundException();
        }
        ArticleResponse result = new ArticleResponse();
        result.setAuthor(art.getUser().getUsername());
        result.setContent(art.getContenu());
        result.setCreated_at(art.getDate_creation());
        result.setId_article(art.getIdArticle());
        result.setNom_theme(art.getTheme().getName());
        result.setTitle(art.getTitre());
        return result;
    }
}
