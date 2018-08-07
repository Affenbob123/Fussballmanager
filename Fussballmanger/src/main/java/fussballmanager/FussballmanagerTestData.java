package fussballmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;
import fussballmanager.spielsimulation.SpielSimulation;
import fussballmanager.spielsimulation.torversuch.TorversuchService;


@Component
public class FussballmanagerTestData {
	
	private static final Logger LOG = LoggerFactory.getLogger(FussballmanagerTestData.class);

	
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
	SpielService spielService;
	
	@Autowired
	SpielSimulation spielSimulation;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SpielerZuwachsService spielerZuwachsService;
	
	String LoginA = "a";
		
	Random r = new Random();
	
	int counter = 0;

	public void erzeugeTestDaten() {
		erzeugeTestUser();
	}
	
	private void erzeugeTestUser() {
		User userA = new User(LoginA, LoginA, false, LoginA, LoginA);
		userA.setLand(landService.findeLand(LaenderNamenTypen.DEUTSCHLAND));
		userService.legeUserAn(userA);
	}
	
	@Scheduled(cron = "*/2 * * * * ?", zone="Europe/Berlin")
	public void simuliereSpiele() {
		counter++;
		if(counter < 46) {
			simuliereLigaspielErsteHalbzeit();
			LOG.info("erste halbzeit: {}", counter);
		}
		
		if(counter >= 46 &&counter < 91) {
			simuliereLigaspielZweiteHalbzeit();
			LOG.info("zweite halbzeit: {}", counter);
		}
		
		if(counter > 90) {
			setzeErgebnisseUndSetzteAuswechselungenZurueckLigaspiel();
			LOG.info("Nach dem Spiel: {}", counter);
		}
	}
	public void simuliereLigaspielErsteHalbzeit() {
		spielSimulation.simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen.LIGASPIEL);
	}
	
	public void simuliereLigaspielZweiteHalbzeit() {
		spielSimulation.simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen.LIGASPIEL);
	}
	
	public void setzeErgebnisseUndSetzteAuswechselungenZurueckLigaspiel() {
		LocalTime aktuelleZeitMinusZweiStunden = LocalTime.now(ZoneId.of("Europe/Berlin")).minusHours(2);
		List<Spiel> alleSpieleEinesSpieltages = spielService.findeAlleSpieleEinerSaisonUndSpieltages(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());

		for(Spiel spiel : alleSpieleEinesSpieltages) {
			Team heimTeam = spiel.getHeimmannschaft();
			Team gastTeam = spiel.getGastmannschaft();
			if(!spiel.isVorbei() && (spiel.getSpielTyp().getSpielBeginn().isAfter(aktuelleZeitMinusZweiStunden))) {
				spiel.setVorbei(true);
				heimTeam.setAnzahlAuswechselungen(3);
				gastTeam.setAnzahlAuswechselungen(3);
				
				spielService.aktualisiereSpiel(spiel);
				teamService.aktualisiereTeam(heimTeam);
				teamService.aktualisiereTeam(gastTeam);
			}
		}
	}
	
	@Scheduled(cron = "0 * * * * ?", zone="Europe/Berlin")
	public void erstelleNeueSpielerFuerTransfermarkt() {
		spielerService.loescheSpielerVomTransfermarkt();
		spielerService.erstelleSpielerFuerTransfermarkt();
	}
	
	@Scheduled(cron = "0 * * * * ?", zone="Europe/Berlin")
	public void wechsleDenSpieltag() {
		spieltagService.wechsleAktuellenSpieltag();
		spielerZuwachsService.legeSpielerZuwachsFuerAlleSpielerAn();
	}
}
