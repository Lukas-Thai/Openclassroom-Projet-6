package mddapi.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import mddapi.model.Abonnement;
import mddapi.model.AbonnementRepository;
import mddapi.model.Theme;
import mddapi.model.ThemeRepository;
import mddapi.model.User;
import mddapi.model.UserRepository;

@Service
public class AbonnementsService {
private AbonnementRepository AbonRep;
private ThemeRepository ThemeRep;
private UserRepository UserRep;
	public AbonnementsService(AbonnementRepository ab,ThemeRepository tr, UserRepository ur) {
		AbonRep=ab;
		ThemeRep=tr;
		UserRep=ur;
	}
	public boolean SubscribeOrUnsubscribe(Integer id_theme, Integer id_user) {
		Abonnement subscribeExist = AbonRep.findByThemeAndUser(id_theme, id_user).orElse(null);
		if(subscribeExist != null) {//cas de désabonnement
			AbonRep.delete(subscribeExist);
			return false; //on return false pour signaler le désabonnement
		}
		else {//cas abonnement il faut créer la ligne
			Abonnement toSave= new Abonnement();
			Theme theme = ThemeRep.findById(id_theme).orElseGet(null);
			User user = UserRep.findById(id_user).orElse(null);
			if(theme==null || user==null) {
				throw new EntityNotFoundException();
			}
			toSave.setDate_abonnement(LocalDateTime.now());
			toSave.setTheme(theme);
			toSave.setUser(user);
			AbonRep.save(toSave);
			return true;//return true pour signaler l'abonnement
		}
	}
}
