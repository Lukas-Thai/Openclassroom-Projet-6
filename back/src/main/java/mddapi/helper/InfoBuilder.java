package mddapi.helper;

import java.time.format.DateTimeFormatter;

import mddapi.dto.UserResponse;
import mddapi.model.User;




public class InfoBuilder {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static UserResponse userInfoBuilder(User user){//permet de convertir un objet user en map pour l'envoi du résultat d'une requete API
        UserResponse dto = new UserResponse();
        dto.setId(user.getId_user());
        dto.setName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setCreated_at(user.getCreated_at().format(FORMATTER));
        dto.setUpdated_at(user.getUpdated_at() != null ? user.getUpdated_at().format(FORMATTER) : "");
        return dto;
    }
}
