package fussballmanager.spielsimulation;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.AusrichtungsTypen;
import fussballmanager.service.team.TeamService;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.lang.Math.toIntExact;

@Service
@Transactional
public class SpielSimulation {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielSimulation.class);
	
	@Autowired
	SpielEreignisService spielEreignisService;
	
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
	
	Random random = new Random();
	
	private boolean heimmannschaftAngreifer;
	
	int counter = 0;
	int spielminute;
	
	public SpielSimulation() {
		
	}
	
	/*
	 * TODO Wahrscheinlichkeit senken wenn ein team schon ein tor geschossen hat
	 * TODO Torwahrscheinlichket am ende erhöhen dafür am anfang senken
	 * TODO Aufstellung miteinbeziehen
	 */
	
	public void simuliereSpielMinuteAllerSpieleErsteHalbzeit(SpieleTypen spielTyp) {	
		List<Spiel> alleSpieleEinesSpieltages = spielService.
				findeAlleSpieleEinesSpieltagesNachSpielTyp(spieltagService.findeAktuellenSpieltag(), spielTyp);
		LocalTime localTimeStart = spielTyp.getSpielBeginn().plusMinutes(15);
		LocalTime localTime  = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		int spielminute = toIntExact(localTimeStart.until(localTime, MINUTES));
		counter++;
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			simuliereSpielminuteEinesSpieles(spiel,spielminute);
			spielService.anzahlToreEinesSpielSetzen(spiel);
		}
	}
	
	
	public void simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen spielTyp) {
		counter++;
		LocalTime localTimeStart = spielTyp.getSpielBeginn().plusHours(1).plusMinutes(15);
		LocalTime localTime  = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		int spielminute = toIntExact(localTimeStart.until(localTime, MINUTES)) + 45 + 1;
		List<Spiel> alleSpieleEinesSpieltages = spielService.findeAlleSpieleEinesSpieltages(spieltagService.findeAktuellenSpieltag());
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			simuliereSpielminuteEinesSpieles(spiel,spielminute);
			spielService.anzahlToreEinesSpielSetzen(spiel);
		}
	}
	
	public void simuliereSpielminuteEinesSpieles(Spiel spiel, int spielminute) {
		List<Spieler> spielerHeimmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getHeimmannschaft());
		List<Spieler> spielerGastmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getGastmannschaft());
		
		spiel = spielService.findeSpiel(spiel.getId());
		
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
		
		bestimmeAngreifer(spiel.getHeimVorteil(), spielerHeimmannschaft, spielerGastmannschaft);
		
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
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCH);
				spielEreignis.setSpieler(spielerService.findeAlleSpielerEinesTeams(spiel.getHeimmannschaft()).get(0));
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				spielEreignis.setSpielminute(spielminute);
				
				spiel.addSpielEreignis(spielEreignis);
				spielService.aktualisiereSpiel(spiel);
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
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCH);
				spielEreignis.setSpieler(spielerService.findeAlleSpielerEinesTeams(spiel.getHeimmannschaft()).get(0));
				spielEreignis.setTeam(spiel.getGastmannschaft());
				spielEreignis.setSpielminute(spielminute);
				
				spiel.addSpielEreignis(spielEreignis);
				spielService.aktualisiereSpiel(spiel);
			} 
		}
		
		//LOG.info("Heimmannscahftangreifer: {}, zufallszahl: {}, tvwahrscheinlichkeit: {}", heimmannschaftAngreifer, zufallsZahl, wahrscheinlichkeitTorVersuch);
		//LOG.info("Spiel: {} : {}, Ausrichtung: {} : {}, Einsatz: {} : {}", spiel.getHeimmannschaft().getName(), spiel.getGastmannschaft().getName(), 
		//		spiel.getHeimmannschaft().getAusrichtungsTyp(), spiel.getGastmannschaft().getAusrichtungsTyp(), spiel.getHeimmannschaft().getEinsatzTyp(),
		//		spiel.getGastmannschaft().getEinsatzTyp());
		//LOG.info("Heimmannschaft Angreifer: {}, Torwart: {}, Abwehr: {}, Mittelfeld: {}, Angriff: {}, Torversuch: {}", heimmannschaftAngreifer, 
		//		erfolgsWahrscheinlichkeitTorwart, erfolgsWahrscheinlichkeitAbwehr, erfolgsWahrscheinlichkeitMittelfeld, erfolgsWahrscheinlichkeitAngriff,
		//		wahrscheinlichkeitTorVersuch);
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
}
