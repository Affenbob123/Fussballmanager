package fussballmanager.service.spiel.turnier;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fussballmanager.service.saison.spieltag.Spieltag;

public interface TurnierRepository extends JpaRepository<Turnier, Long> {

	List<Turnier> findBySpieltagSpieltagNummerGreaterThan(int i);

	List<Turnier> findBySpieltag(Spieltag findeAktuellenSpieltag);

	List<Turnier> findByGestartet(boolean b);

	List<Turnier> findByOrderBySpieltagSpieltagNummerDesc(Pageable seite);

}
