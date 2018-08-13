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
import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.liga.LigenNamenTypen;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsService;
import fussballmanager.service.tabelle.TabellenEintrag;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.AusrichtungsTypen;
import fussballmanager.service.team.EinsatzTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;
import fussballmanager.spielsimulation.SpielSimulation;

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
	String LoginB = "b";
		
	Random r = new Random();
	
	int spielminute = 0;
	
	//@PostConstruct
	public void erstelleObjekte() {
		Land land = new Land(LaenderNamenTypen.DEUTSCHLAND);
		landService.legeLandAn(land);
		
		Liga liga = new Liga(LigenNamenTypen.ERSTELIGA, land);
		ligaService.aktualisiereLiga(liga);
		
		Saison saison = new Saison(1);
		saison.setAktuelleSaison(true);
		saisonService.aktualisiereSaison(saison);
		
		Team teamA = new Team(land, "Team A", null, liga);
		teamA.setAusrichtungsTyp(AusrichtungsTypen.SEHROFFENSIV);
		teamA.setEinsatzTyp(EinsatzTypen.BRUTAL);
		teamService.legeTeamAn(teamA);
		
		TabellenEintrag tabellenEintragTeamA = new TabellenEintrag();
		tabellenEintragTeamA.setSaison(saison);
		tabellenEintragTeamA.setTeam(teamA);
		tabellenEintragService.legeTabellenEintragAn(tabellenEintragTeamA);
		
		Team teamB = new Team(land, "Team B", null, liga);
		teamB.setAusrichtungsTyp(AusrichtungsTypen.SEHROFFENSIV);
		teamB.setEinsatzTyp(EinsatzTypen.BRUTAL);
		teamService.legeTeamAn(teamB);
		
		TabellenEintrag tabellenEintragTeamB = new TabellenEintrag();
		tabellenEintragTeamB.setSaison(saison);
		tabellenEintragTeamB.setTeam(teamB);
		tabellenEintragService.legeTabellenEintragAn(tabellenEintragTeamB);
		
		User userA = new User(LoginA, LoginA, false, LoginA, LoginA);
		userA.setLand(landService.findeLand(LaenderNamenTypen.DEUTSCHLAND));
		userService.legeUserAn(userA);
		teamB.setUser(userA);
		teamService.aktualisiereTeam(teamB);
		
		for(int i = 1; i < 36; i++) {
			Spieltag spieltag = new Spieltag(i, saison);
			spieltagService.legeSpieltagAn(spieltag);
			Spiel spiel = new Spiel(SpieleTypen.LIGASPIEL, teamA, teamB, spieltag, saison,"");
			spielService.legeSpielAn(spiel);
		}
		
		Spieltag s = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(saison, 1);
		s.setAktuellerSpieltag(true);
		spieltagService.aktualisiereSpieltag(s);
		
		for(int i = 1; i < 34; i++) {
			spielSimulationTest();
			wechsleDenSpieltag();
		}
		
		int tore = 0;
		int toreSpielEreignis = 0;
		int gelbeKarten = 0;
		int verletzungen = 0;
		int gelbRoteKarten = 0;
		int roteKarten = 0;
		
		List<Spiel> alleSpiele = spielService.findeAlleSpiele();
		for(Spiel spiel : alleSpiele) {
			tore = tore + spiel.getToreGastmannschaft() + spiel.getToreHeimmannschaft();
			for(SpielEreignis spielEreignis : spiel.getSpielEreignisse()) {
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.GELBEKARTE)) {
					gelbeKarten++;
				}
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.GELBROTEKARTE)) {
					gelbRoteKarten++;
				}
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.ROTEKARTE)) {
					roteKarten++;
				}
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.VERLETZUNG)) {
					verletzungen++;
				}
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGETROFFEN)) {
					toreSpielEreignis++;
				}
			}
			LOG.info("Spieltag: {}, Tore: {}, ToreSpielEreignis: {}, Gelbekarten: {}, GelbroteKarten: {}, Rotekarten: {}, Verletzungen: {}", 
					spiel.getSpieltag().getSpieltagNummer(), tore, toreSpielEreignis, gelbeKarten, gelbRoteKarten, roteKarten, verletzungen);
			tore = 0;
			toreSpielEreignis = 0;
			gelbeKarten = 0;
			verletzungen = 0;
			gelbRoteKarten = 0;
			roteKarten = 0;
		}
	}
	
	public void spielSimulationTest() {
		spielAnfang();
		
		for(int i = 1; i < 46; i++) {
			spielSimulation.simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen.LIGASPIEL, i);
		}

		for(int i = 46; i < 91; i++) {
			spielSimulation.simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen.LIGASPIEL, i);
		}
		aktualiserenNachSpielEnde();
	}
	
	public void erzeugeTestDaten() {
		erzeugeTestUser();
		spieltagService.wechsleAktuellenSpieltag();
	}
	
	private void erzeugeTestUser() {
		User userA = new User(LoginA, LoginA, false, LoginA, LoginA);
		userA.setLand(landService.findeLand(LaenderNamenTypen.DEUTSCHLAND));
		userService.legeUserAn(userA);
	}
	
	//@Scheduled(cron = "0 0/3 * * * ?", zone="Europe/Berlin")
	@Scheduled(cron = "0 * * * * ?", zone="Europe/Berlin")
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
	
	//@Scheduled(cron = "0 1/3 * * * ?", zone="Europe/Berlin")
	public void spielHalbzeit() {
		List<Spiel> alleSpieleDesSpieltages = spielService.findeAlleSpieleEinerSaisonUndSpieltages(
				saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());
		for(Spiel spiel :alleSpieleDesSpieltages) {
			spiel.setHalbzeitAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
		}
	}
	
	//@Scheduled(cron = "15-59 0/3 * * * ?", zone="Europe/Berlin")
	@Scheduled(cron = "5/1 * * * * ?", zone="Europe/Berlin")
	public void simuliereSpieleErsteHalbzeit() {
		spielminute++;
		simuliereLigaspielErsteHalbzeit(spielminute);
		LOG.info("erste halbzeit: {}", spielminute);
	}
	
	//@Scheduled(cron = "15-59 1/3 * * * ?", zone="Europe/Berlin")
	public void simuliereSpieleZweiteHalbzeit() {
		spielminute++;
		simuliereLigaspielZweiteHalbzeit(spielminute);
		LOG.info("zweite halbzeit: {}", spielminute);
	}
	
	//@Scheduled(cron = "05 2/3 * * * ?", zone="Europe/Berlin")
	public void simuliereSpielEnde() {
		spielminute = 0;
		aktualiserenNachSpielEnde();
	}
	
	public void simuliereLigaspielErsteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen.LIGASPIEL, spielminute);
	}
	
	public void simuliereLigaspielZweiteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen.LIGASPIEL, spielminute);
	}
	
	public void aktualiserenNachSpielEnde() {
		spielService.aufgabenNachSpiel();
		spielerService.aufgabenNachSpiel();
	}
	
	//@Scheduled(cron = "0/30 * * * * ?", zone="Europe/Berlin")
	public void erstelleNeueSpielerFuerTransfermarkt() {
		spielerService.loescheSpielerVomTransfermarkt();
		spielerService.erstelleSpielerFuerTransfermarkt();
	}
	
	//@Scheduled(cron = "* * * * * ?", zone="Europe/Berlin")
	//@Scheduled(cron = "15 2/3 * * * ?", zone="Europe/Berlin")
	public void wechsleDenSpieltag() {
		spieltagService.wechsleSpieltag();
	}
}
