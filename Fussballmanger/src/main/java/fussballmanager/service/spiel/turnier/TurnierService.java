package fussballmanager.service.spiel.turnier;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.KOSpielTypen;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spieler.staerke.SpielerStaerke;
import fussballmanager.service.spieler.staerke.SpielerStaerkeRepository;
import fussballmanager.service.spieler.staerke.SpielerStaerkeService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class TurnierService {

	private static final Logger LOG = LoggerFactory.getLogger(TurnierService.class);

	@Autowired
	TurnierRepository turnierRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;
	
	public Turnier findeTurnier(Long id) {
		return turnierRepository.getOne(id);
	}
	
	public List<Turnier> findeAlleTurniere() {
		return turnierRepository.findAll();
	}
	
	public List<Turnier> findeAlleTurniereDieAmAktuellenSpieltagStarten() {
		return turnierRepository.findBySpieltag(spieltagService.findeAktuellenSpieltag());
	}	
	
	public List<Turnier> findeAlleTurniereDieGestartetSind() {
		return turnierRepository.findByGestartet(true);
	}
	
	public void legeTurnierAn(Turnier turnier) {
		turnierRepository.save(turnier);
	}
	
	public void aktualisiereTurnier(Turnier turnier) {
		turnierRepository.save(turnier);
	}
	
	public void loescheTurnier(Turnier turnier) {
		turnierRepository.delete(turnier);
	}
	
	public void aufgabenAmEndeDerSaison() {
		turnierRepository.deleteAll();
	}
	
	public void aufgabenNachTurnierSpielenFuerGestarteteTurniere() {
		List<Turnier> alleTurniere = findeAlleTurniereDieGestartetSind();
		for(Turnier turnier : alleTurniere) {
			if(turnier.getkOSpielTyp().equals(KOSpielTypen.FINALE)) {
				schließeTurnierAb(turnier);
			}
			entferneVerliererTeams(turnier);
			erstelleTurnierSpiele(turnier, turnier.getTeams().size());
		}
	}

	public void entferneVerliererTeams(Turnier turnier) {
		for(Spiel turnierSpiel : turnier.getTurnierSpiele()) {
			if(turnierSpiel.getkOSpielTyp().equals(turnier.getkOSpielTyp())) {
				if(turnierSpiel.getToreHeimmannschaft() > turnierSpiel.getToreGastmannschaft()) {
					turnier.getTeams().remove(turnierSpiel.getGastmannschaft());
				}
				if(turnierSpiel.getToreHeimmannschaft() < turnierSpiel.getToreGastmannschaft()) {
					turnier.getTeams().remove(turnierSpiel.getHeimmannschaft());
				}
				if(turnierSpiel.getToreHeimmannschaft() == turnierSpiel.getToreGastmannschaft()) {
					Random random = new Random();
					int zufallsZahl = random.nextInt(1);
					
					if(zufallsZahl == 0) {
						turnier.getTeams().remove(turnierSpiel.getHeimmannschaft());
					} else {
						turnier.getTeams().remove(turnierSpiel.getGastmannschaft());
					}
				}
			}
		}
		aktualisiereTurnier(turnier);
	}

	public void schließeTurnierAb(Turnier turnier) {
		//TODO Prämien verteilen
		turnier.setTeams(new ArrayList<Team>());
		aktualisiereTurnier(turnier);
	}

	public List<Turnier> findeAlleAktuellenTurniere() {
		Spieltag aktuellerSpieltag = spieltagService.findeAktuellenSpieltag();
		List<Turnier> alleAktuellenTurniere = turnierRepository.findBySpieltagSpieltagNummerGreaterThan(aktuellerSpieltag.getSpieltagNummer()-1);
		return alleAktuellenTurniere;
	}
	
	public void starteAlleTurniereDesSpieltages() {
		List<Turnier> alleTurniereDieAmAkuellenSpieltagStarten = findeAlleTurniereDieAmAktuellenSpieltagStarten();
		
		for(Turnier turnier : alleTurniereDieAmAkuellenSpieltagStarten) {
			turnier.setGestartet(true);
			turnier.setGeschlossen(true);
			aktualisiereTurnier(turnier);
			starteTurnier(turnier);
		}
	}
	
	public void starteTurnier(Turnier turnier) {
		int anzahlTeams = turnier.getTeams().size();
		
		if(anzahlTeams < 8) {
			loescheTurnier(turnier);
		} else if(anzahlTeams == 8) {
			erstelleTurnierSpiele(turnier, 8);
		} else if(anzahlTeams < 17) {
			erstelleTurnierSpiele(turnier, 16);
		} else if(anzahlTeams < 33) {
			erstelleTurnierSpiele(turnier, 32);
		} else if(anzahlTeams < 65) {
			erstelleTurnierSpiele(turnier, 64);
		} else if(anzahlTeams < 129) {
			erstelleTurnierSpiele(turnier, 128);
		}
	}

	public void erstelleTurnierSpiele(Turnier turnier, int anzahlTeams) {
		int counter2 = anzahlTeams;
		Spieltag spieltag;
		if(turnier.getTurnierSpiele().isEmpty()) {
			spieltag = spieltagService.findeAktuellenSpieltag();
		} else {
			spieltag = spieltagService.findeNaechstenSpieltag();
		}
		
		Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		for(int i = 1; i <= anzahlTeams/2; i = i + 1) {
			Spiel turnierSpiel = new Spiel();
			turnierSpiel.setSpieltag(spieltag);
			turnierSpiel.setSaison(aktuelleSaison);
			turnierSpiel.setSpielTyp(SpieleTypen.TURNIERSPIEL);
			turnierSpiel.setSpielort("Hallo");
			turnierSpiel.setHeimmannschaft(turnier.getTeams().get(i-1));
			if(counter2 <= turnier.getTeams().size()) {
				turnierSpiel.setGastmannschaft(turnier.getTeams().get(counter2-1));
			}
			counter2--;
			
			if(anzahlTeams == 2) {
				turnierSpiel.setkOSpielTyp(KOSpielTypen.FINALE);
			}
			if(anzahlTeams == 4) {
				turnierSpiel.setkOSpielTyp(KOSpielTypen.HALBFINALE);
			}
			if(anzahlTeams == 8) {
				turnierSpiel.setkOSpielTyp(KOSpielTypen.VIERTELFINALE);
			}
			if(anzahlTeams == 16) {
				turnierSpiel.setkOSpielTyp(KOSpielTypen.ACHTELFINALE);
			}
			if(anzahlTeams == 32) {
				if(turnier.getkOSpielTyp() == null) {
					turnierSpiel.setkOSpielTyp(KOSpielTypen.ERSTERUNDE);
				} else {
					if(turnier.getkOSpielTyp().equals(KOSpielTypen.ERSTERUNDE)) {
						turnierSpiel.setkOSpielTyp(KOSpielTypen.ZWEITERUNDE);
					}
					if(turnier.getkOSpielTyp().equals(KOSpielTypen.ZWEITERUNDE)) {
						turnierSpiel.setkOSpielTyp(KOSpielTypen.DRITTERUNDE);
					}
				}
			}
			if(anzahlTeams == 64) {
				if(turnier.getkOSpielTyp() == null) {
					turnierSpiel.setkOSpielTyp(KOSpielTypen.ERSTERUNDE);
				} else {
					if(turnier.getkOSpielTyp().equals(KOSpielTypen.ERSTERUNDE)) {
						turnierSpiel.setkOSpielTyp(KOSpielTypen.ZWEITERUNDE);
					}
				}

			}
			if(anzahlTeams == 128) {
				turnierSpiel.setkOSpielTyp(KOSpielTypen.ERSTERUNDE);
			}
			spielService.legeSpielAn(turnierSpiel);
			turnier.getTurnierSpiele().add(turnierSpiel);
		}
		turnier.setkOSpielTyp(turnier.getTurnierSpiele().get(turnier.getTurnierSpiele().size() -1).getkOSpielTyp());
		aktualisiereTurnier(turnier);
	}
}
