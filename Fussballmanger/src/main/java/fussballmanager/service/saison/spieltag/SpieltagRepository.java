package fussballmanager.service.saison.spieltag;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.saison.Saison;

@Repository
public interface SpieltagRepository extends JpaRepository<Spieltag, Long>{

	Spieltag findBySaisonAndSpieltagNummer(Saison saison, int spieltagNummer);
	
	Spieltag findByAktuellerSpieltagTrue();
	
	List<Spieltag> findBySaison(Saison saison);

	List<Spieltag> findBySpieltagNummerGreaterThanAndSaison(int spieltagNummer, Saison findeAktuelleSaison);
}
