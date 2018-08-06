package fussballmanager.spielsimulation.torversuch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.team.Team;

@Repository
public interface TorversuchRepository extends JpaRepository<Torversuch, Long> {
	
	List<Torversuch> findByVerteidiger(Team verteidiger);
}
