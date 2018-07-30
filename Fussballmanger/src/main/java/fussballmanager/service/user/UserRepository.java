package fussballmanager.service.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	List<User> findByIsAdmin(boolean b);
	
	User findByUsername(String username);
}
