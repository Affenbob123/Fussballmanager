package fussballmanager.spielsimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.AusrichtungsTypen;

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
	TorVersuchWahrscheinlicheit torVersuchWahrscheinlichkeit;
	
	Random random = new Random();
	
	private List<Spieler> spielerHeimmannschaft;
	private List<Spieler> spielerGastmannschaft;
	private boolean heimmannschaftAngreifer;
	
	public SpielSimulation() {
		
	}
	
	public synchronized List<SpielEreignis> simuliereSpiel(Spiel spiel) {
		spielerHeimmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getHeimmannschaft());
		spielerGastmannschaft = spielerService.findeAlleSpielerEinesTeams(spiel.getGastmannschaft());
	
		int zufallsZahlHeimVorteil = random.nextInt(100 - 30 + 1) + 30;
		List<SpielEreignis> spielEreignisse = new ArrayList<>();
		double heimVorteil = berechneHeimVorteil(zufallsZahlHeimVorteil);
		double staerkeFaktor = staerkeFaktorHeimmannschaft(spielerHeimmannschaft, spielerGastmannschaft, heimVorteil);

		for(int i = 0; i < 90; i++) {
			spielEreignisse.add(simuliereSpielminute(spiel, staerkeFaktor, i));
		}		
		return spielEreignisse;
	}
	
	public SpielEreignis simuliereSpielminute(Spiel spiel, double staerkeFaktor, int spielminute) {
		SpielEreignis spielEreignis = new SpielEreignis();
		int zufallsZahl = random.nextInt(100*100*100*100 - 1 + 1) + 1;
		double wahrscheinlichkeitTorVersuch;
		
		AusrichtungsTypen ausrichtungsTypAngreifer;
		AusrichtungsTypen ausrichtungsTypVerteidiger;
		
		double erfolgsWahrscheinlichkeitTorwart;
		double erfolgsWahrscheinlichkeitAbwehr;
		double erfolgsWahrscheinlichkeitMittelfeld;
		double erfolgsWahrscheinlichkeitAngriff;

		bestimmeAngreifer(staerkeFaktor);
		
		if(heimmannschaftAngreifer) {
			ausrichtungsTypAngreifer = spiel.getHeimmannschaft().getAusrichtungsTyp();
			ausrichtungsTypVerteidiger = spiel.getGastmannschaft().getAusrichtungsTyp();
			
			erfolgsWahrscheinlichkeitTorwart = torVersuchWahrscheinlichkeit.wahrscheinlichkeitTorwartGegenTorwart(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitAbwehr = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAbwehrGegenAngriff(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitMittelfeld = torVersuchWahrscheinlichkeit.wahrscheinlichkeitMittelfeldGegenMittelfeld(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitAngriff = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAngriffGegenAbwehr(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			
			wahrscheinlichkeitTorVersuch = erfolgsWahrscheinlichkeitAbwehr * erfolgsWahrscheinlichkeitMittelfeld * erfolgsWahrscheinlichkeitAngriff * erfolgsWahrscheinlichkeitTorwart;
			LOG.info("Wahrscheinlichkeit vor Ausrichtung:", wahrscheinlichkeitTorVersuch);
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * ausrichtungsTypAngreifer.getWahrscheinlichkeitTorZuErzielen() * 
					ausrichtungsTypVerteidiger.getWahrscheinlichkeitTorZuKassieren();
			LOG.info("Wahrscheinlichkeit nach Ausrichtung:", wahrscheinlichkeitTorVersuch);
			
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * 100;
			
			if(zufallsZahl > wahrscheinlichkeitTorVersuch) {
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.NIX);
			} else {
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCH);
				spielEreignis.setSpieler(null);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				spielEreignis.setSpielminute(spielminute);
				
				spiel.addSpielEreignis(spielEreignis);
				spielService.aktualisiereSpiel(spiel);
			}
//			LOG.info("{} Abwehr: {}, Mittelfeld: {}, Angriff: {}, Torversuch: {}", "Gastmannschaft", erfolgsWahrscheinlichkeitAbwehr, 
//					erfolgsWahrscheinlichkeitMittelfeld, erfolgsWahrscheinlichkeitAngriff, wahrscheinlichkeitTorVersuch);		
		} else {
			ausrichtungsTypAngreifer = spiel.getGastmannschaft().getAusrichtungsTyp();
			ausrichtungsTypVerteidiger = spiel.getHeimmannschaft().getAusrichtungsTyp();
			
			erfolgsWahrscheinlichkeitTorwart = torVersuchWahrscheinlichkeit.wahrscheinlichkeitTorwartGegenTorwart(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitAbwehr = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAbwehrGegenAngriff(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitMittelfeld = torVersuchWahrscheinlichkeit.wahrscheinlichkeitMittelfeldGegenMittelfeld(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitAngriff = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAngriffGegenAbwehr(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
			
			wahrscheinlichkeitTorVersuch = erfolgsWahrscheinlichkeitAbwehr * erfolgsWahrscheinlichkeitMittelfeld * erfolgsWahrscheinlichkeitAngriff * erfolgsWahrscheinlichkeitTorwart;
			LOG.info("Wahrscheinlichkeit vor Ausrichtung: {}", wahrscheinlichkeitTorVersuch);
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * ausrichtungsTypAngreifer.getWahrscheinlichkeitTorZuErzielen() * 
					ausrichtungsTypVerteidiger.getWahrscheinlichkeitTorZuKassieren();
			LOG.info("Wahrscheinlichkeit nach Ausrichtung: {}", wahrscheinlichkeitTorVersuch);
			
			wahrscheinlichkeitTorVersuch = wahrscheinlichkeitTorVersuch * 100;
			
			if(zufallsZahl > wahrscheinlichkeitTorVersuch) {
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.NIX);
			} else {
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCH);
				spielEreignis.setSpieler(null);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				spielEreignis.setSpielminute(spielminute);
				
				spiel.addSpielEreignis(spielEreignis);
				spielService.aktualisiereSpiel(spiel);
			}
			
//			LOG.info("{} Abwehr: {}, Mittelfeld: {}, Angriff: {}, Torversuch: {}", "Gastmannschaft", erfolgsWahrscheinlichkeitAbwehr, 
//				erfolgsWahrscheinlichkeitMittelfeld, erfolgsWahrscheinlichkeitAngriff, wahrscheinlichkeitTorVersuch);
		}
		return spielEreignis;
	}
	
	public double berechneHeimVorteil(int zufallsZahlHeimVorteil) {
		double doubleZufallsZahl = (double)zufallsZahlHeimVorteil;
		//LOG.info("Zufallszahlheimvorteil : {}", doubleZufallsZahl);
		return (doubleZufallsZahl + 100.00) / 100.00;
	}

	public boolean isHeimmannschaftAngreifer() {
		return heimmannschaftAngreifer;
	}

	public void setHeimmannschaftAngreifer(boolean heimmannschaftAngreifer) {
		this.heimmannschaftAngreifer = heimmannschaftAngreifer;
	}
	
	public void bestimmeAngreifer(double heimVorteil) {
		int zufallsZahl = ThreadLocalRandom.current().nextInt(1, 100);
		
		if(torVersuchWahrscheinlichkeit.wahrscheinlichkeitDasHeimmannschaftImAngriffIst(spielerHeimmannschaft, spielerGastmannschaft, heimVorteil) > zufallsZahl) {
			setHeimmannschaftAngreifer(true);
		} else {
			setHeimmannschaftAngreifer(false);
		}
	}
	
	/*
	 * Nimmt summe der Stärken der teams und vergleicht diese. Anschließend wird mit der fuenften wurzel der stärkefaktor der heimmanschaft berechnet
	 */
	public double staerkeFaktorHeimmannschaft(List<Spieler> spielerHeimmannschaft, List<Spieler> spielerGastmannschaft, double heimVorteil) {
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
		
		if(gesamtStaerkeHeimmannschaft <= 0.0) {
			gesamtStaerkeHeimmannschaft = 0.01;
		}
		
		if(gesamtStaerkeGastmannschaft <= 0.0) {
			gesamtStaerkeGastmannschaft = 0.01;
		}
		
		if((gesamtStaerkeHeimmannschaft / gesamtStaerkeGastmannschaft) >= 1.0) {
			tatsaechlicherFaktor = gesamtStaerkeHeimmannschaft / gesamtStaerkeGastmannschaft;
			
			if(tatsaechlicherFaktor > 100) {
				tatsaechlicherFaktor = 100;
			}
			staerkeFaktor = Math.pow(tatsaechlicherFaktor, 1.0/5);
		} else {
			tatsaechlicherFaktor = gesamtStaerkeGastmannschaft / gesamtStaerkeHeimmannschaft;
			
			if(tatsaechlicherFaktor > 100) {
				tatsaechlicherFaktor = 100;
			}
			staerkeFaktor = Math.pow(tatsaechlicherFaktor, 1.0/5);
			staerkeFaktor = staerkeFaktor * -1;
		}
		LOG.info("Heimmannschaft: {}, Gastmannschaft: {}, Stärkefaktor: {}", gesamtStaerkeHeimmannschaft, gesamtStaerkeGastmannschaft, staerkeFaktor);
		
		return staerkeFaktor;
	}
}
