package mddapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import mddapi.model.Theme;
import mddapi.model.ThemeRepository;

/**
 * Service de gestion des thèmes.
 */
@Service
public class ThemesService {
    private ThemeRepository themeRep;

    /**
     * Constructeur.
     */
    public ThemesService(ThemeRepository tr) {
        themeRep = tr;
    }

    /**
     * Récupère tous les thèmes.
     *
     * @return liste des thèmes
     */
    public List<Theme> getAllTheme() {
        return themeRep.findAll();
    }

    /**
     * Recherche un thème par son identifiant.
     *
     * @param id identifiant du thème
     * @return thème trouvé ou {@code null}
     */
    public Theme findThemeId(Integer id) {
        return themeRep.findById(id).orElse(null);
    }
}
