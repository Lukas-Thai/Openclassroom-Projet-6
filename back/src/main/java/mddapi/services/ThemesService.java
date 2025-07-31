package mddapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import mddapi.model.Theme;
import mddapi.model.ThemeRepository;

@Service
public class ThemesService {
	private ThemeRepository themeRep;
	public ThemesService(ThemeRepository tr) {
		themeRep=tr;
	}
	public List<Theme> getAllTheme() {
		return themeRep.findAll();
	}
	public Theme findThemeId(Integer id) {
		return themeRep.findById(id).orElse(null);
	}
}
