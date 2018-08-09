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
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsService;
import fussballmanager.service.tabelle.TabellenEintrag;
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
	
	//TODO Wieder mit LocalTime in SpielMinutesimulation machen
	int spielminute = 0;

	public void erzeugeTestDaten() {
		erzeugeTestUser();
	}
	
	private void erzeugeTestUser() {
		User userA = new User(LoginA, LoginA, false, LoginA, LoginA);
		userA.setLand(landService.findeLand(LaenderNamenTypen.DEUTSCHLAND));
		userService.legeUserAn(userA);
	}
	
	@Scheduled(cron = "0 0/3 * * * ?", zone="Europe/Berlin")
	public void spielAnfang() {
		List<Spiel> alleSpieleDesSpieltages = spielService.findeAlleSpieleEinerSaisonUndSpieltages(
				saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());
		for(Spiel spiel :alleSpieleDesSpieltages) {
			spiel.setAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
			LOG.info("spiel angefangen");
		}
		tabellenEintragService.alleTabellenEintraegeAktualisieren();
	}
	
	@Scheduled(cron = "0 1/3 * * * ?", zone="Europe/Berlin")
	public void spielHalbzeit() {
		List<Spiel> alleSpieleDesSpieltages = spielService.findeAlleSpieleEinerSaisonUndSpieltages(
				saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());
		for(Spiel spiel :alleSpieleDesSpieltages) {
			spiel.setHalbzeitAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
		}
	}
	
	@Scheduled(cron = "15-59 0/3 * * * ?", zone="Europe/Berlin")
	public void simuliereSpieleErsteHalbzeit() {
		spielminute++;
		simuliereLigaspielErsteHalbzeit(spielminute);
		LOG.info("erste halbzeit: {}", spielminute);
	}
	
	@Scheduled(cron = "15-59 1/3 * * * ?", zone="Europe/Berlin")
	public void simuliereSpieleZweiteHalbzeit() {
		spielminute++;
		simuliereLigaspielZweiteHalbzeit(spielminute);
		LOG.info("zweite halbzeit: {}", spielminute);
	}
	
	@Scheduled(cron = "05 2/3 * * * ?", zone="Europe/Berlin")
	public void simuliereSpielEnde() {
		setzeErfahrungeUndSetzteAuswechselungenZurueckLigaspiel();
		spielminute = 0;
	}
	
	public void simuliereLigaspielErsteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen.LIGASPIEL, spielminute);
	}
	
	public void simuliereLigaspielZweiteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen.LIGASPIEL, spielminute);
	}
	
	public void setzeErfahrungeUndSetzteAuswechselungenZurueckLigaspiel() {
		LocalTime aktuelleZeitMinusZweiStunden = LocalTime.now(ZoneId.of("Europe/Berlin")).minusHours(2);
		List<Spiel> alleSpieleEinesSpieltages = spielService.findeAlleSpieleEinerSaisonUndSpieltages(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			Team heimTeam = spiel.getHeimmannschaft();
			Team gastTeam = spiel.getGastmannschaft();
			if(!spiel.isVorbei() && (spiel.getSpielTyp().getSpielBeginn().isAfter(aktuelleZeitMinusZweiStunden))) {
				spielService.anzahlToreEinesSpielSetzen(spiel);
				spiel.setVorbei(true);
				heimTeam.setAnzahlAuswechselungen(3);
				gastTeam.setAnzahlAuswechselungen(3);
				
				spielService.aktualisiereSpiel(spiel);
				teamService.aktualisiereTeam(heimTeam);
				teamService.aktualisiereTeam(gastTeam);
			}
		}
		
		//Erfahrung nach spiel um eins erhöhen
		for(Spieler spieler : spielerService.findeAlleSpieler()) {
			if(!spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) {
				spieler.setErfahrung(spieler.getErfahrung() + 1);
				spielerService.aktualisiereSpieler(spieler);
			}
		}
	}
	
	@Scheduled(cron = "0/30 * * * * ?", zone="Europe/Berlin")
	public void erstelleNeueSpielerFuerTransfermarkt() {
		spielerService.loescheSpielerVomTransfermarkt();
		spielerService.erstelleSpielerFuerTransfermarkt();
	}
	
	@Scheduled(cron = "15 2/3 * * * ?", zone="Europe/Berlin")
	public void wechsleDenSpieltag() {
		spieltagService.wechsleSpieltag();
		spielerZuwachsService.legeSpielerZuwachsFuerAlleSpielerAn();
	}
}
