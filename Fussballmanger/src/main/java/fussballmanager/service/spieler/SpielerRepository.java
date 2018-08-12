package fussballmanager.service.spieler;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.land.Land;
import fussballmanager.service.spieler.staerke.SpielerStaerke;
import fussballmanager.service.team.Team;

@Repository
public interface SpielerRepository extends JpaRepository<Spieler, Long> {
	
	List<Spieler> findByTeam(Team team);
	
	List<Spieler> findByNationalitaetAndTeam(Land nationalitaet, Team team);

	List<Spieler> findByAufstellungsPositionsTyp(AufstellungsPositionsTypen aufstellungsPositionsTyp);

	List<Spieler> findByPositionAndNationalitaetAndAlterBetweenAndSpielerStaerkeBetweenAndPreisBetween(PositionenTypen position, LaenderNamenTypen land,
			int minimalesAlter, int maximalesAlter, double minimaleStaerke, double maximaleStaerke, double minimalerPreis, double maximalerPreis);
	
	List<Spieler> findByTransfermarktAndAlterBetweenAndPreisBetween(boolean transfermarkt,
			int minimalesAlter, int maximalesAlter, long minimalerPreis, long maximalerPreis);

	List<Spieler> findByTeamIsNotNull();

	List<Spieler> findByGelbeKarteTrue();
	
	//Erfahrung
	List<Spieler> findByOrderByErfahrungDesc(Pageable seite);
	List<Spieler> findByNationalitaetOrderByErfahrungDesc(Land nationalitaet, Pageable seite);
	List<Spieler> findByAlterOrderByErfahrungDesc(int alter, Pageable seite);
	List<Spieler> findByPositionOrderByErfahrungDesc(PositionenTypen position, Pageable seite);
	List<Spieler> findByAlterAndPositionOrderByErfahrungDesc(int alter, PositionenTypen position, Pageable seite);
	List<Spieler> findByNationalitaetAndAlterOrderByErfahrungDesc(Land nationalitaet, int alter, Pageable seite);
	List<Spieler> findByNationalitaetAndPositionOrderByErfahrungDesc(Land nationalitaet, PositionenTypen position,
			Pageable seite);
	List<Spieler> findByNationalitaetAndAlterAndPositionOrderByErfahrungDesc(Land nationalitaet, int alter, 
			PositionenTypen position, Pageable seite);
	
	//Tore
	List<Spieler> findByOrderByToreDesc(Pageable seite);
	List<Spieler> findByNationalitaetOrderByToreDesc(Land nationalitaet, Pageable seite);
	List<Spieler> findByAlterOrderByToreDesc(int alter, Pageable seite);
	List<Spieler> findByPositionOrderByToreDesc(PositionenTypen position, Pageable seite);
	List<Spieler> findByAlterAndPositionOrderByToreDesc(int alter, PositionenTypen position, Pageable seite);
	List<Spieler> findByNationalitaetAndAlterOrderByToreDesc(Land nationalitaet, int alter, Pageable seite);
	List<Spieler> findByNationalitaetAndPositionOrderByToreDesc(Land nationalitaet, PositionenTypen position,
			Pageable seite);
	List<Spieler> findAllByNationalitaetAndAlterAndPositionOrderByToreDesc(Land nationalitaet, int alter, 
			PositionenTypen position, Pageable pageable);
	
	//GelbeKarten
	List<Spieler> findByOrderByGelbeKartenDesc(Pageable seite);
	List<Spieler> findByNationalitaetOrderByGelbeKartenDesc(Land nationalitaet, Pageable seite);
	List<Spieler> findByAlterOrderByGelbeKartenDesc(int alter, Pageable seite);
	List<Spieler> findByPositionOrderByGelbeKartenDesc(PositionenTypen position, Pageable seite);
	List<Spieler> findByAlterAndPositionOrderByGelbeKartenDesc(int alter, PositionenTypen position,
			Pageable seite);
	List<Spieler> findByNationalitaetAndAlterOrderByGelbeKartenDesc(Land nationalitaet, int alter, Pageable seite);
	List<Spieler> findByNationalitaetAndPositionOrderByGelbeKartenDesc(Land nationalitaet, PositionenTypen position,
			Pageable seite);
	List<Spieler> findAllByNationalitaetAndAlterAndPositionOrderByGelbeKartenDesc(Land nationalitaet, int alter,
			PositionenTypen position, Pageable seite);
	
	//GelbRoteKarten
	List<Spieler> findByOrderByGelbRoteKartenDesc(Pageable pageable);
	List<Spieler> findByNationalitaetOrderByGelbRoteKartenDesc(Land nationalitaet, Pageable seite);
	List<Spieler> findByAlterOrderByGelbRoteKartenDesc(int alter, Pageable seite);
	List<Spieler> findByPositionOrderByGelbRoteKartenDesc(PositionenTypen position, Pageable seite);
	List<Spieler> findByAlterAndPositionOrderByGelbRoteKartenDesc(int alter, PositionenTypen position,
			Pageable seite);
	List<Spieler> findByNationalitaetAndAlterOrderByGelbRoteKartenDesc(Land nationalitaet, int alter, Pageable seite);
	List<Spieler> findByNationalitaetAndPositionOrderByGelbRoteKartenDesc(Land nationalitaet, PositionenTypen position,
			Pageable seite);
	List<Spieler> findByNationalitaetAndAlterAndPositionOrderByGelbRoteKartenDesc(Land nationalitaet, int alter,
			PositionenTypen position, Pageable seite);
	
	//RoteKarten
	List<Spieler> findByOrderByRoteKartenDesc(Pageable pageable);
	List<Spieler> findByNationalitaetOrderByRoteKartenDesc(Land nationalitaet, Pageable seite);
	List<Spieler> findByAlterOrderByRoteKartenDesc(int alter, Pageable seite);
	List<Spieler> findByPositionOrderByRoteKartenDesc(PositionenTypen position, Pageable seite);
	List<Spieler> findByAlterAndPositionOrderByRoteKartenDesc(int alter, PositionenTypen position,
			Pageable seite);
	List<Spieler> findByNationalitaetAndAlterOrderByRoteKartenDesc(Land nationalitaet, int alter, Pageable seite);
	List<Spieler> findByNationalitaetAndPositionOrderByRoteKartenDesc(Land nationalitaet, PositionenTypen position,
			Pageable seite);
	List<Spieler> findByNationalitaetAndAlterAndPositionOrderByRoteKartenDesc(Land nationalitaet, int alter,
			PositionenTypen position, Pageable seite);

}
