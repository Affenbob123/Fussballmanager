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

	List<Spiel> findByHeimmannschaftAndSaison(Team team, Saison saison);

	List<Spiel> findByGastmannschaftAndSaison(Team team, Saison saison);

	List<Spiel> findByHeimmannschaftOrGastmannschaftAndSaison(Team team, Team team2, Saison saison);

	List<Spiel> findBySpielTypAndHeimmannschaftOrGastmannschaft(SpieleTypen ligaspiel, Team team,
			Team team2);
	
	List<Spiel> findByHeimmannschaftOrGastmannschaftAndSaisonAndSpieltag(Team team, Team team2, Saison saison,
			Spieltag spieltag);

	List<Spiel> findBySpielTypAndHeimmannschaftOrGastmannschaftAndSaisonAndSpieltag(SpieleTypen ligaspiel, Team team,
			Team team2, Saison saison, Spieltag spieltag);

	Spiel findBySpielTypAndHeimmannschaftAndSaisonAndSpieltag(SpieleTypen ligaspiel, Team team,
			Saison saison, Spieltag spieltag);

	List<Spiel> findByAngefangenAndVorbeiAndSpieltag(boolean b, boolean c, Spieltag aktuellerSpieltag);

	List<Spiel> findByAngefangenAndVorbeiAndSaisonAndSpieltag(
			boolean b, boolean c, Saison findeAktuelleSaison, Spieltag findeAktuellenSpieltag);
}
