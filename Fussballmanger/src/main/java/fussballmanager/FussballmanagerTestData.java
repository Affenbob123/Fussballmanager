package fussballmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

import fussballmanager.service.finanzen.Bilanz;
import fussballmanager.service.finanzen.BilanzService;
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
import fussballmanager.service.spiel.turnier.Turnier;
import fussballmanager.service.spiel.turnier.TurnierService;
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
import fussballmanager.service.team.stadion.Stadion;
import fussballmanager.service.team.stadion.StadionService;
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
	
	@Autowired
	BilanzService bilanzService;
	
	@Autowired
	StadionService stadionService;
	
	@Autowired
	TurnierService turnierService;
	
	String loginA = "a";
	String loginB = "b";
		
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
		
		Bilanz bilanzA = new Bilanz();
		bilanzService.legeBilanzAn(bilanzA);
		
		Stadion stadionA = new Stadion();
		stadionService.legeStadionAn(stadionA);
		
		Team teamA = new Team(land, "Team A", null, liga, bilanzA, stadionA);
		teamA.setAusrichtungsTyp(AusrichtungsTypen.SEHROFFENSIV);
		teamA.setEinsatzTyp(EinsatzTypen.BRUTAL);
		teamService.legeTeamAn(teamA);
		
		TabellenEintrag tabellenEintragTeamA = new TabellenEintrag();
		tabellenEintragTeamA.setSaison(saison);
		tabellenEintragTeamA.setTeam(teamA);
		tabellenEintragService.legeTabellenEintragAn(tabellenEintragTeamA);
		
		Bilanz bilanzB = new Bilanz();
		bilanzService.legeBilanzAn(bilanzB);
		
		Stadion stadionB = new Stadion();
		stadionService.legeStadionAn(stadionB);
		
		Team teamB = new Team(land, "Team B", null, liga, bilanzB, stadionB);
		teamB.setAusrichtungsTyp(AusrichtungsTypen.SEHROFFENSIV);
		teamB.setEinsatzTyp(EinsatzTypen.BRUTAL);
		teamService.legeTeamAn(teamB);
		
		TabellenEintrag tabellenEintragTeamB = new TabellenEintrag();
		tabellenEintragTeamB.setSaison(saison);
		tabellenEintragTeamB.setTeam(teamB);
		tabellenEintragService.legeTabellenEintragAn(tabellenEintragTeamB);
		
		User userA = new User(loginA, loginA, false, loginA, loginA);
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
	
	public void erzeugeTestTurnier() {
		Turnier turnier = new Turnier();
		turnier.setName("turn1");
		turnier.setBeschreibung("turnier1");
		turnier.setPraemien(1000L);
		turnier.setTeams(teamService.findeAlleTeamsEinesUsers(userService.findeUser(loginA)));
		turnier.setUser(userService.findeUser("a"));
		turnier.setSpieltag(spieltagService.
				findeSpieltagDurchSaisonUndSpieltagNummer(saisonService.findeAktuelleSaison(), 
						spieltagService.findeAktuellenSpieltag().getSpieltagNummer() +1));
		turnierService.legeTurnierAn(turnier);
	}
	
	private void erzeugeTestUser() {
		User userA = new User(loginA, loginA, false, loginA, loginA);
		userA.setLand(landService.findeLand(LaenderNamenTypen.DEUTSCHLAND));
		userService.legeUserAn(userA);
		User userB = new User(loginB, loginB, false, loginB, loginB);
		userB.setLand(landService.findeLand(LaenderNamenTypen.DEUTSCHLAND));
		userService.legeUserAn(userB);
		
		for(int i = 0; i < teamService.findeAlleTeams().size() - 5; i++) {
			teamService.findeAlleTeams().get(i).setUser(userA);
		}
	}
	
	//@Scheduled(cron = "0 3/6 * * * ?", zone="Europe/Berlin")
	public void spielAnfang() {
		List<Spiel> alleLigaspieleEinesSpieltages = spielService.
			findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag(), SpieleTypen.LIGASPIEL);
		
		for(Spiel spiel :alleLigaspieleEinesSpieltages) {
			spiel.setAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
			LOG.info("spiel angefangen");
		}
		tabellenEintragService.alleTabellenEintraegeAktualisieren();
	}
	
	//@Scheduled(cron = "15-59 3/6 * * * ?", zone="Europe/Berlin")
	public void simuliereSpieleErsteHalbzeit() {
		spielminute++;
		simuliereLigaspielErsteHalbzeit(spielminute);
		LOG.info("erste halbzeit: {}", spielminute);
	}
	
	//@Scheduled(cron = "0 4/6 * * * ?", zone="Europe/Berlin")
	public void spielHalbzeit() {
		List<Spiel> alleLigaspieleEinesSpieltages = spielService.
				findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saisonService.findeAktuelleSaison(), 
						spieltagService.findeAktuellenSpieltag(), SpieleTypen.LIGASPIEL);
			
		for(Spiel spiel :alleLigaspieleEinesSpieltages) {
			spiel.setHalbzeitAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
		}
	}
	
	//@Scheduled(cron = "15-59 4/6 * * * ?", zone="Europe/Berlin")
	public void simuliereSpieleZweiteHalbzeit() {
		spielminute++;
		simuliereLigaspielZweiteHalbzeit(spielminute);
		LOG.info("zweite halbzeit: {}", spielminute);
	}
	
	//@Scheduled(cron = "05 5/6 * * * ?", zone="Europe/Berlin")
	public void simuliereSpielEnde() {
		spielminute = 0;
		aktualiserenNachSpielEnde();
	}
	
	@Scheduled(cron = "0 0/3 * * * ?", zone="Europe/Berlin")
	public void turnierSpielAnfang() {
		List<Spiel> alleTurnierspieleEinesSpieltages = spielService.
				findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saisonService.findeAktuelleSaison(), 
						spieltagService.findeAktuellenSpieltag(), SpieleTypen.TURNIERSPIEL);
		LOG.info("Turnierspiele:{}", alleTurnierspieleEinesSpieltages.size());
		for(Spiel spiel :alleTurnierspieleEinesSpieltages) {
			spiel.setAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
			LOG.info("Turnier angefangen");
		}
	}
	
	@Scheduled(cron = "15-59 0/3 * * * ?", zone="Europe/Berlin")
	public void simuliereTurnierSpieleErsteHalbzeit() {
		spielminute++;
		simuliereTurnierspielErsteHalbzeit(spielminute);
		LOG.info("erste halbzeit Turnier: {}", spielminute);
	}
	
	@Scheduled(cron = "0 1/3 * * * ?", zone="Europe/Berlin")
	public void turnierSpielHalbzeit() {
		List<Spiel> alleTurnierspieleEinesSpieltages = new ArrayList<Spiel>();
		alleTurnierspieleEinesSpieltages = spielService.
				findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saisonService.findeAktuelleSaison(), 
						spieltagService.findeAktuellenSpieltag(), SpieleTypen.TURNIERSPIEL);
		for(Spiel spiel :alleTurnierspieleEinesSpieltages) {
			spiel.setHalbzeitAngefangen(true);
			spielService.aktualisiereSpiel(spiel);
		}
	}
	
	@Scheduled(cron = "15-59 1/3 * * * ?", zone="Europe/Berlin")
	public void simuliereTurnierSpieleZweiteHalbzeit() {
		spielminute++;
		simuliereTurnierspielZweiteHalbzeit(spielminute);
		LOG.info("zweite halbzeit Turnier: {}", spielminute);
	}
	
	@Scheduled(cron = "05 2/3 * * * ?", zone="Europe/Berlin")
	public void simuliereTurnierSpielEnde() {
		spielminute = 0;
		aktualiserenNachSpielEnde();
		turnierService.aufgabenNachTurnierSpielenFuerGestarteteTurniere();
	}
	
	public void simuliereLigaspielErsteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen.LIGASPIEL, spielminute);
	}
	
	public void simuliereLigaspielZweiteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen.LIGASPIEL, spielminute);
	}
	
	public void simuliereTurnierspielErsteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen.TURNIERSPIEL, spielminute);
	}
	
	public void simuliereTurnierspielZweiteHalbzeit(int spielminute) {
		spielSimulation.simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen.TURNIERSPIEL, spielminute);
	}
	
	public void aktualiserenNachSpielEnde() {
		spielService.aufgabenNachSpiel();
		spielerService.aufgabenNachSpiel();
	}
	
	@Scheduled(cron = "0/30 * * * * ?", zone="Europe/Berlin")
	public void erstelleNeueSpielerFuerTransfermarkt() {
		spielerService.loescheSpielerVomTransfermarkt();
		spielerService.erstelleSpielerFuerTransfermarkt();
	}
	
	//@Scheduled(cron = "*/20 * * * * ?", zone="Europe/Berlin")
	@Scheduled(cron = "15 2/3 * * * ?", zone="Europe/Berlin")
	public void wechsleDenSpieltag() {
		spieltagService.wechsleSpieltag();
		turnierService.starteAlleTurniereDesSpieltages();
	}
}
