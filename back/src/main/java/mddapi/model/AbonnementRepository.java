package mddapi.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Integer>{
	public Optional<Abonnement> findById(Integer id_abonnement);
	public Optional<Abonnement> findByTheme_IdThemeAndUser_IdUser(Integer id_theme, Integer id_user);
	public List<Abonnement> findByUser_IdUser(Integer id_user);
}
