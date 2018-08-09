package fussballmanager.spielsimulation;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.AusrichtungsTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.spielsimulation.torversuch.Torversuch;
import fussballmanager.spielsimulation.torversuch.TorversuchService;
import fussballmanager.spielsimulation.torversuch.TorversuchTypen;

@Service
@Transactional
public class SpielSimulation {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielSimulation.class);
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	TorVersuchWahrscheinlicheit torVersuchWahrscheinlichkeit;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	TorversuchService torversuchService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	private boolean heimmannschaftAngreifer;
		
	public SpielSimulation() {
		
	}
	
	/*
	 * TODO Wahrscheinlichkeit senken wenn ein team schon ein tor geschossen hat
	 * TODO Torwahrscheinlichket am ende erhöhen dafür am anfang senken
	 * TODO Aufstellung miteinbeziehen
	 */
	
	public void simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen spielTyp, int spielminute) {	
		List<Spiel> alleSpieleEinesSpieltages = spielService.
				findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag(), spielTyp);
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			spiel.setAktuelleSpielminute(spielminute);
			spielService.aktualisiereSpiel(spiel);
			simuliereSpielminuteEinesSpieles(spiel, spielminute);
		}
		torversuchService.ueberPruefeObTorversucheNochAktuell();
	}
	
	public void simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen spielTyp, int spielminute) {
		List<Spiel> alleSpieleEinesSpieltages = 
				spielService.findeAlleSpieleEinerSaisonUndSpieltages(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			spiel.setAktuelleSpielminute(spielminute);
			spielService.aktualisiereSpiel(spiel);
			simuliereSpielminuteEinesSpieles(spiel, spielminute);
			spielminute++;
		}
		torversuchService.ueberPruefeObTorversucheNochAktuell();
	}
	
	public void simuliereSpielminuteEinesSpieles(Spiel spiel, int spielminute) {
		List<Spieler> spielerHeimmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getHeimmannschaft());
		List<Spieler> spielerGastmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getGastmannschaft());		
		spiel = spielService.findeSpiel(spiel.getId());
		
		bestimmeAngreifer(spiel.getHeimVorteil(), spielerHeimmannschaft, spielerGastmannschaft);
		ermittleObEinTorversuchStattfindet(heimmannschaftAngreifer, spiel);
		ermittleObEsEineVerletzungGibt(heimmannschaftAngreifer, spiel, spielminute);
		ermittleObEsEineGelbeKarteGibt(heimmannschaftAngreifer, spiel, spielminute);
		ermittleObEsEineRoteKarteGibt(heimmannschaftAngreifer, spiel, spielminute);
	}

	public boolean isHeimmannschaftAngreifer() {
		return heimmannschaftAngreifer;
	}

	public void setHeimmannschaftAngreifer(boolean heimmannschaftAngreifer) {
		this.heimmannschaftAngreifer = heimmannschaftAngreifer;
	}
	
	public void bestimmeAngreifer(double heimVorteil, List<Spieler> spielerHeimmannschaft, List<Spieler> spielerGastmannschaft) {
		int zufallsZahl = ThreadLocalRandom.current().nextInt(1, 100);
		//LOG.info("Torversuchwahrscheinlichkeit: {}", torVersuchWahrscheinlichkeit.wahrscheinlichkeitDasHeimmannschaftImAngriffIst(spielerHeimmannschaft, spielerGastmannschaft, heimVorteil));
		if(torVersuchWahrscheinlichkeit.wahrscheinlichkeitDasHeimmannschaftImAngriffIst(spielerHeimmannschaft, spielerGastmannschaft, heimVorteil) > zufallsZahl) {
			setHeimmannschaftAngreifer(true);
		} else {
			setHeimmannschaftAngreifer(false);
		}
	}
	
	/*
	 * Nimmt summe der Stärken der teams und vergleicht diese. Anschließend wird mit der fuenften wurzel der stärkefaktor der heimmanschaft berechnet
	 */
	public double staerkeFaktorHeimmannschaft(Spiel spiel) {
		List<Spieler> spielerHeimmannschaft = spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft());
		List<Spieler> spielerGastmannschaft = spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft());
		double staerkeFaktor = 1.0;
		double gesamtStaerkeHeimmannschaft = 0.0;
		double gesamtStaerkeGastmannschaft = 0.0;
		double tatsaechlicherFaktor = 1.0;
		
		for(Spieler spieler : spielerHeimmannschaft) {
			gesamtStaerkeHeimmannschaft = gesamtStaerkeHeimmannschaft + spieler.getStaerke().getDurchschnittsStaerke();
		}
		
		for(Spieler spieler : spielerGastmannschaft) {
			gesamtStaerkeGastmannschaft = gesamtStaerkeGastmannschaft + spieler.getStaerke().getDurchschnittsStaerke();
		}
		
		gesamtStaerkeHeimmannschaft = gesamtStaerkeHeimmannschaft * spiel.getHeimVorteil();
		
		if(gesamtStaerkeHeimmannschaft <= 0.0) {
			gesamtStaerkeHeimmannschaft = 0.01;
		}
		
		if(gesamtStaerkeGastmannschaft <= 0.0) {
			gesamtStaerkeGastmannschaft = 0.01;
		}
		
		//LOG.info("Heim: {}, Gast:{}",gesamtStaerkeHeimmannschaft, gesamtStaerkeGastmannschaft);
		tatsaechlicherFaktor = gesamtStaerkeHeimmannschaft / gesamtStaerkeGastmannschaft;
		
		if(tatsaechlicherFaktor > 100) {
			tatsaechlicherFaktor = 100;
		}
		staerkeFaktor = Math.pow(tatsaechlicherFaktor, 1.0/5);
		
		return staerkeFaktor;
	}
	
	private TorversuchTypen zufaelligeTorversuchRichtung() {
		Random random = new Random();
		int zufallsZahl = random.nextInt(2);
		
		if(zufallsZahl == 2) {
			return TorversuchTypen.LINKS;
		}
		
		if(zufallsZahl == 1) {
			return TorversuchTypen.MITTE;
		}
		return TorversuchTypen.RECHTS;
	}
	
	private void ermittleObEinTorversuchStattfindet(boolean heimmannschaftAngreifer, Spiel spiel) {
		List<Spieler> spielerHeimmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getHeimmannschaft());
		List<Spieler> spielerGastmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getGastmannschaft());		
		Random random = new Random();
		
		int zufallsZahl = random.nextInt(100 - 1 + 1) + 1;
		double wahrscheinlichkeitTorVersuch;
		
		AusrichtungsTypen ausrichtungsTypAngreifer;
		AusrichtungsTypen ausrichtungsTypVerteidiger;
		
		double erfolgsWahrscheinlichkeitTorwart;
		double erfolgsWahrscheinlichkeitAbwehr;
		double erfolgsWahrscheinlichkeitMittelfeld;
		double erfolgsWahrscheinlichkeitAngriff;
		
		double staerkeFaktorHeimmannschaft = staerkeFaktorHeimmannschaft(spiel);
		double staerkeFaktorGastmannschaft = 1 / staerkeFaktorHeimmannschaft(spiel);
		
		if(heimmannschaftAngreifer) {
			ausrichtungsTypAngreifer = spiel.getHeimmannschaft().getAusrichtungsTyp();
			ausrichtungsTypVerteidiger = spiel.getGastmannschaft().getAusrichtungsTyp();
			
			erfolgsWahrscheinlichkeitTorwart = torVersuchWahrscheinlichkeit.wahrscheinlichkeitTorwartGegenTorwart(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktorHeimmannschaft);
			erfolgsWahrscheinlichkeitAbwehr = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAbwehrGegenAngriff(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktorHeimmannschaft);
			erfolgsWahrscheinlichkeitMittelfeld = torVersuchWahrscheinlichkeit.wahrscheinlichkeitMittelfeldGegenMittelfeld(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktorHeimmannschaft);
			erfolgsWahrscheinlichkeitAngriff = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAngriffGegenAbwehr(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktorHeimmannschaft);
			
			wahrscheinlichkeitTorVersuch = erfolgsWahrscheinlichkeitAbwehr * erfolgsWahrscheinlichkeitMittelfeld * erfolgsWahrscheinlichkeitAngriff * erfolgsWahrscheinlichkeitTorwart;
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * ausrichtungsTypAngreifer.getWahrscheinlichkeitTorZuErzielen() * 
					ausrichtungsTypVerteidiger.getWahrscheinlichkeitTorZuKassieren();
			
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * 100;
			if(zufallsZahl < wahrscheinlichkeitTorVersuch) {
				erstellenEinesTorversuches(spiel.getHeimmannschaft(), spiel.getGastmannschaft(), spiel);
			}	
		} else {
			ausrichtungsTypAngreifer = spiel.getGastmannschaft().getAusrichtungsTyp();
			ausrichtungsTypVerteidiger = spiel.getHeimmannschaft().getAusrichtungsTyp();
			
			erfolgsWahrscheinlichkeitTorwart = torVersuchWahrscheinlichkeit.wahrscheinlichkeitTorwartGegenTorwart(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktorGastmannschaft);
			erfolgsWahrscheinlichkeitAbwehr = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAbwehrGegenAngriff(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktorGastmannschaft);
			erfolgsWahrscheinlichkeitMittelfeld = torVersuchWahrscheinlichkeit.wahrscheinlichkeitMittelfeldGegenMittelfeld(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktorGastmannschaft);
			erfolgsWahrscheinlichkeitAngriff = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAngriffGegenAbwehr(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktorGastmannschaft);
			
			wahrscheinlichkeitTorVersuch = erfolgsWahrscheinlichkeitAbwehr * erfolgsWahrscheinlichkeitMittelfeld * erfolgsWahrscheinlichkeitAngriff * erfolgsWahrscheinlichkeitTorwart;
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * ausrichtungsTypAngreifer.getWahrscheinlichkeitTorZuErzielen() * 
					ausrichtungsTypVerteidiger.getWahrscheinlichkeitTorZuKassieren();
			
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * 100;
			
			if(zufallsZahl < wahrscheinlichkeitTorVersuch) {
				erstellenEinesTorversuches(spiel.getGastmannschaft(), spiel.getHeimmannschaft(), spiel);
			} 
		}
	}
	
	private void ermittleObEsEineRoteKarteGibt(boolean heimmannschaftAngreifer, Spiel spiel, int spielminute) {
		Random random = new Random();
		int zufallsZahl = random.nextInt(99999);
		if(heimmannschaftAngreifer) {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = (int) (33 * spiel.getGastmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spielminute);
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.ROTEKARTE);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				Spieler spieler = ermittleSpielerFuerRoteKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft()));
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
				spielEreignis.setSpieler(spieler);
				LOG.info("rotekarte gast");
			}
		} else {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = (int) (33 * spiel.getHeimmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spielminute);
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.ROTEKARTE);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				Spieler spieler = ermittleSpielerFuerRoteKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft()));
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
				spielEreignis.setSpieler(spieler);
				LOG.info("rotekarte heim");
			}
		}
	}
	
	private void ermittleObEsEineGelbeKarteGibt(boolean heimmannschaftAngreifer, Spiel spiel, int spielminute) {
		Random random = new Random();
		int zufallsZahl = random.nextInt(99999);
		if(heimmannschaftAngreifer) {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = (int) (1900 * spiel.getGastmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spielminute);
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBEKARTE);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				Spieler spieler = ermittleSpielerFuerGelbeKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft()));
				if(spieler.isGelbeKarte()) {
					spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBROTEKARTE);
					spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
					spielerService.aktualisiereSpieler(spieler);
				}
				spielEreignis.setSpieler(spieler);
				LOG.info("gelbekarte gast");
			}
		} else {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = (int) (1900 * spiel.getHeimmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spielminute);
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBEKARTE);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				Spieler spieler = ermittleSpielerFuerGelbeKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft()));
				if(spieler.isGelbeKarte()) {
					spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBROTEKARTE);
					spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
					spielerService.aktualisiereSpieler(spieler);
				}
				spielEreignis.setSpieler(spieler);
				LOG.info("gelbekarte heim");
			}
		}
	}

	private void ermittleObEsEineVerletzungGibt(boolean heimmannschaftAngreifer, Spiel spiel, int spielminute) {
		Random random = new Random();
		int zufallsZahl = random.nextInt(9999);
		if(heimmannschaftAngreifer) {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = (int) (38 * spiel.getGastmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spielminute);
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.VERLETZUNG);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				Spieler spieler = ermittleSpielerFuerRoteKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft()));
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);				
				LOG.info("verletzung gast");
			}
		} else {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = (int) (38 * spiel.getHeimmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spielminute);
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.VERLETZUNG);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				Spieler spieler = ermittleSpielerFuerRoteKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft()));
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);		
				LOG.info("verletzung heim");
			}
		}
	}
	
	private void erstellenEinesTorversuches(Team angreifer, Team verteidiger, Spiel spiel) {
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		Torversuch torversuch = new Torversuch();
		torversuch.setRichtung(zufaelligeTorversuchRichtung());
		torversuch.setTorschuetze(ermittleSpielerFuerTorversuch(spielerService.findeAlleSpielerEinesTeamsInAufstellung(angreifer)));
		torversuch.setAngreifer(angreifer);
		torversuch.setTorwart(spielerService.findeTorwartEinesTeams(verteidiger));
		torversuch.setVerteidiger(verteidiger);
		torversuch.setSpiel(spiel);
		torversuch.setSpielminute(spiel.getAktuelleSpielminute());
		torversuch.setErstellZeit(aktuelleUhrzeit);
		
		LOG.info("tor von Spieler: {}, vom Team: {}", torversuch.getTorschuetze().getPosition().getPositionsName(), torversuch.getAngreifer().getName());
		torversuchService.legeTorversuchAn(torversuch);
		
		//Wenn der Verteidiger keinen Torwart hat, dann wird automatisch ein Tor geschossen
		if(torversuch.getTorwart() == null) {
			torversuchService.erstelleSpielEreignisAusTorversuch(torversuch);
		}
		
		//aktualisiert spiel und die Tabelle
		spielService.anzahlToreEinesSpielSetzen(spiel);
		tabellenEintragService.einenTabellenEintragAktualisieren(
				tabellenEintragService.findeTabellenEintragDurchTeamUndSaison(spiel.getHeimmannschaft(), saisonService.findeAktuelleSaison()));
		tabellenEintragService.einenTabellenEintragAktualisieren(
				tabellenEintragService.findeTabellenEintragDurchTeamUndSaison(spiel.getGastmannschaft(), saisonService.findeAktuelleSaison()));
	}

	private Spieler ermittleSpielerFuerTorversuch(List<Spieler> spielerDesAngreifers) {
		Random random = new Random();
		List<Spieler> alleSpielerEinesTeams = spielerDesAngreifers;
		List<Spieler> listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit = new ArrayList<>();
		int summeDerWahrscheinlichkeiten = 0;
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			summeDerWahrscheinlichkeiten = summeDerWahrscheinlichkeiten + spieler.getAufstellungsPositionsTyp().getTorversuchWahrscheinlichkeit();
		}
		int zufallsZahl = random.nextInt(summeDerWahrscheinlichkeiten);
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			int spielerZahl = spieler.getAufstellungsPositionsTyp().getTorversuchWahrscheinlichkeit();
			for(int i = 1; i <= spielerZahl; i++) {
				listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit.add(spieler);
			}
		}
		return listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit.get(zufallsZahl);
	}
	
	private Spieler ermittleSpielerFuerGelbeKarte(List<Spieler> spielerDesTeams) {
		Random random = new Random();
		List<Spieler> alleSpielerEinesTeams = spielerDesTeams;
		List<Spieler> listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit = new ArrayList<>();
		int summeDerWahrscheinlichkeiten = 0;
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			summeDerWahrscheinlichkeiten = summeDerWahrscheinlichkeiten + spieler.getAufstellungsPositionsTyp().getGelbeKarteWahrscheinlichkeit();
		}
		int zufallsZahl = random.nextInt(summeDerWahrscheinlichkeiten);
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			int spielerZahl = spieler.getAufstellungsPositionsTyp().getGelbeKarteWahrscheinlichkeit();
			for(int i = 1; i <= spielerZahl; i++) {
				listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit.add(spieler);
			}
		}
		return listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit.get(zufallsZahl);
	}
	
	private Spieler ermittleSpielerFuerRoteKarte(List<Spieler> spielerDesTeams) {
		Random random = new Random();
		List<Spieler> alleSpielerEinesTeams = spielerDesTeams;
		List<Spieler> listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit = new ArrayList<>();
		int summeDerWahrscheinlichkeiten = 0;
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			summeDerWahrscheinlichkeiten = summeDerWahrscheinlichkeiten + spieler.getAufstellungsPositionsTyp().getRoteKarteWahrscheinlichkeit();
		}
		int zufallsZahl = random.nextInt(summeDerWahrscheinlichkeiten);
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			int spielerZahl = spieler.getAufstellungsPositionsTyp().getRoteKarteWahrscheinlichkeit();
			for(int i = 1; i <= spielerZahl; i++) {
				listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit.add(spieler);
			}
		}
		return listeMitAnzahlAnSpielernJeNachWahrscheinlichkeit.get(zufallsZahl);
	}
	
	private Spieler ermittleSpielerFuerVerletzung(List<Spieler> spielerDesTeams) {
		Random random = new Random();
		List<Spieler> alleSpielerEinesTeams = spielerDesTeams;
		
		int zufallsZahl = random.nextInt(alleSpielerEinesTeams.size()-1);
		return alleSpielerEinesTeams.get(zufallsZahl);
	}
}
