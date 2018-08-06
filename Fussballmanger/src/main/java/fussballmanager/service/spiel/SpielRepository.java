package fussballmanager.service.spiel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.team.Team;

@Repository
public interface SpielRepository extends JpaRepository<Spiel, Long> {

	List<Spiel> findByHeimmannschaft(Team heimmannschaft);
	
	List<Spiel> findByGastmannschaft(Team gastmannschaft);
	
	List<Spiel> findBySaisonAndSpieltag(Saison saison, Spieltag spieltag);
	
	List<Spiel> findBySaisonAndSpieltagAndSpielTyp(Saison saison, Spieltag spieltag, SpieleTypen spielTyp);
	
	List<Spiel> findBySaison(Saison saison);
}
