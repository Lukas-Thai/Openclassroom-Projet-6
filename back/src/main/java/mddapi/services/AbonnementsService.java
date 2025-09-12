package mddapi.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mddapi.model.Abonnement;
import mddapi.model.AbonnementRepository;
import mddapi.model.Theme;
import mddapi.model.ThemeRepository;
import mddapi.model.User;
import mddapi.model.UserRepository;

/**
 * Service permettant de gérer les abonnements des utilisateurs à des thèmes.
 */
@Service
public class AbonnementsService {
    private AbonnementRepository AbonRep;
    private ThemeRepository ThemeRep;
    private UserRepository UserRep;

    /**
     * Constructeur avec injection des repositories nécessaires.
     *
     * @param ab repository pour les abonnements
     * @param tr repository pour les thèmes
     * @param ur repository pour les utilisateurs
     */
    public AbonnementsService(AbonnementRepository ab, ThemeRepository tr, UserRepository ur) {
        AbonRep = ab;
        ThemeRep = tr;
        UserRep = ur;
    }

    /**
     * Récupère la liste des identifiants de thèmes auxquels un utilisateur est abonné.
     *
     * @param user l’utilisateur dont on veut les abonnements
     * @return liste des identifiants de thèmes
     */
    public List<Integer> getUserAbonnement(User user) {
        List<Abonnement> abonList = AbonRep.findByUser_IdUser(user.getIdUser());
        ArrayList<Integer> result = new ArrayList<>();
        for (Abonnement abon : abonList) {
            result.add(abon.getTheme().getIdTheme());
        }
        return result;
    }

    /**
     * Abonne ou désabonne un utilisateur à un thème.
     *
     * @param id_theme identifiant du thème
     * @param id_user  identifiant de l’utilisateur
     * @return {@code true} si un abonnement a été créé, {@code false} si un désabonnement a eu lieu
     * @throws EntityNotFoundException si le thème ou l’utilisateur n’existe pas
     */
    public boolean SubscribeOrUnsubscribe(Integer id_theme, Integer id_user) {
        Abonnement subscribeExist = AbonRep.findByTheme_IdThemeAndUser_IdUser(id_theme, id_user).orElse(null);
        if (subscribeExist != null) { // cas de désabonnement
            AbonRep.delete(subscribeExist);
            return false;
        } else { // cas abonnement
            Abonnement toSave = new Abonnement();
            Theme theme = ThemeRep.findById(id_theme).orElse(null);
            User user = UserRep.findById(id_user).orElse(null);
            if (theme == null || user == null) {
                throw new EntityNotFoundException();
            }
            toSave.setTheme(theme);
            toSave.setUser(user);
            AbonRep.save(toSave);
            return true;
        }
    }
}
