package fussballmanager;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.UserService;
import fussballmanager.spielsimulation.SpielSimulation;

@Service
@Transactional
public class FussballmanagerErstellung {
	
	private static final Logger LOG = LoggerFactory.getLogger(FussballmanagerErstellung.class);
	
	@Autowired
	LandService landService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	LigaService ligaService;

	@Autowired
	TeamService teamService;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpielEreignisService spielEreignisService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SpielSimulation spielSimulation;
	
	public FussballmanagerErstellung() {

	}
	
	//@Scheduled(fixedDelay=100)
	@PostConstruct
	public void checkTimeForCreation() {
		LocalDateTime aktuellesDatum = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
		LocalDateTime erstellDatum = LocalDateTime.of(2018, 7, 23, 10, 03);
		
		if(aktuellesDatum.isAfter(erstellDatum) || aktuellesDatum.isEqual(erstellDatum)) {
			erstelleSpiel();
		}
}
	
	public void erstelleSpiel() {
		if(landService.findeAlleLaender().size() == 0) {
			for(LaenderNamenTypen laenderNamenTypen : LaenderNamenTypen.values()) {
				landService.legeLandAn(new Land(laenderNamenTypen));
				ligaService.legeHauptteamLigenAn(landService.findeLand(laenderNamenTypen));
			}
			saisonService.ersteSaisonErstellen();
			spieltagService.checkAktuellerSpieltag();
			spieltagService.simuliereSpieltag();
			LOG.info("DurchschnittlicheTorVersuche: {}", spielEreignisService.findeSpielEreignisse().size());
		}
	}
}
