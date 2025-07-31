package mddapi.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public Optional<User> findByEmail(String email);
	public Optional<User> findById(Integer id_user);
	public Optional<User> findByUsername(String username);
}
