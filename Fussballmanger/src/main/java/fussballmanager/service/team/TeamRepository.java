package fussballmanager.service.team;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.user.User;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	Team findByName(String name);
	
	Team findFirstByLandAndUser(Land land, User user);

	List<Team> findByUser(User user);
	
	List<Team> findByLiga(Liga liga);
	
	List<Team> findByUserAndImLiveticker(User user, boolean imLiveticker);
}
