package fussballmanager.service.spieler;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.team.Team;

@Repository
public interface SpielerRepository extends JpaRepository<Spieler, Long> {

	List<Spieler> findByTeam(Team team);
}
