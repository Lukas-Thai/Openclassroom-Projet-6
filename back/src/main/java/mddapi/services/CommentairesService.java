package mddapi.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mddapi.dto.CommentaireResponse;
import mddapi.model.Article;
import mddapi.model.ArticleRepository;
import mddapi.model.Commentaire;
import mddapi.model.CommentaireRepository;
import mddapi.model.User;
import mddapi.model.UserRepository;

/**
 * Service de gestion des commentaires.
 */
@Service
public class CommentairesService {
    private CommentaireRepository comRep;
    private ArticleRepository artRep;
    private UserRepository userRep;

    /**
     * Constructeur.
     */
    public CommentairesService(CommentaireRepository cr, ArticleRepository ar, UserRepository ur) {
        this.comRep = cr;
        this.artRep = ar;
        this.userRep = ur;
    }

    /**
     * Récupère tous les commentaires liés à un article.
     *
     * @param id_article identifiant de l’article
     * @return liste de réponses de commentaires
     */
    public List<CommentaireResponse> fetchCommentaireByArticle(Integer id_article) {
        List<Commentaire> fetchResult = comRep.findByArticle_IdArticle(id_article);
        ArrayList<CommentaireResponse> result = new ArrayList<>();
        for (Commentaire com : fetchResult) {
            CommentaireResponse toPush = new CommentaireResponse();
            toPush.setAuthor(com.getUser().getUsername());
            toPush.setContent(com.getContenu());
            toPush.setDate(com.getDate_commentaire());
            toPush.setId_article(com.getArticle().getIdArticle());
            toPush.setId_commentaire(com.getId_commentaire());
            result.add(toPush);
        }
        return result;
    }

    /**
     * Crée un commentaire pour un article donné.
     *
     * @param id_article identifiant de l’article
     * @param id_user    identifiant de l’utilisateur
     * @param comment    contenu du commentaire
     * @throws EntityNotFoundException si l’article ou l’utilisateur n’existe pas
     */
    public void createCommentaire(Integer id_article, Integer id_user, String comment) {
        Article article = artRep.findById(id_article).orElse(null);
        User user = userRep.findById(id_user).orElse(null);
        if (article == null || user == null) {
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
