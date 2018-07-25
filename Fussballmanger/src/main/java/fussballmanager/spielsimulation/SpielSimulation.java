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
		int wahrscheinlichkeitTorVersuch;
		
		int erfolgsWahrscheinlichkeitAbwehr;
		int erfolgsWahrscheinlichkeitMittelfeld;
		int erfolgsWahrscheinlichkeiAngriff;
		int wahrscheinlicheitSchussAufTor = 45;
		bestimmeAngreifer(staerkeFaktor);
		
		if(heimmannschaftAngreifer) {
			erfolgsWahrscheinlichkeitAbwehr = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAbwehrGegenAngriff(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeitMittelfeld = torVersuchWahrscheinlichkeit.wahrscheinlichkeitMittelfeldGegenMittelfeld(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			erfolgsWahrscheinlichkeiAngriff = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAngriffGegenAbwehr(spielerHeimmannschaft, spielerGastmannschaft, staerkeFaktor);
			
			wahrscheinlichkeitTorVersuch = erfolgsWahrscheinlichkeitAbwehr * erfolgsWahrscheinlichkeitMittelfeld * erfolgsWahrscheinlichkeiAngriff * wahrscheinlicheitSchussAufTor;
			
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
//					erfolgsWahrscheinlichkeitMittelfeld, erfolgsWahrscheinlichkeiAngriff, wahrscheinlichkeitTorVersuch);		
			} else {
				erfolgsWahrscheinlichkeitAbwehr = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAbwehrGegenAngriff(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
				erfolgsWahrscheinlichkeitMittelfeld = torVersuchWahrscheinlichkeit.wahrscheinlichkeitMittelfeldGegenMittelfeld(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
				erfolgsWahrscheinlichkeiAngriff = torVersuchWahrscheinlichkeit.wahrscheinlichkeitAngriffGegenAbwehr(spielerGastmannschaft, spielerHeimmannschaft, staerkeFaktor);
				
				wahrscheinlichkeitTorVersuch = erfolgsWahrscheinlichkeitAbwehr * erfolgsWahrscheinlichkeitMittelfeld * erfolgsWahrscheinlichkeiAngriff * wahrscheinlicheitSchussAufTor;
				
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
				
//				LOG.info("{} Abwehr: {}, Mittelfeld: {}, Angriff: {}, Torversuch: {}", "Gastmannschaft", erfolgsWahrscheinlichkeitAbwehr, 
//					erfolgsWahrscheinlichkeitMittelfeld, erfolgsWahrscheinlichkeiAngriff, wahrscheinlichkeitTorVersuch);
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
		// 0.0000001 um geteilt durch null zu verhindern
		double gesamtStaerkeHeimmannschaft = 0.0000001;
		double gesamtStaerkeGastmannschaft = 0.0000001;
		double tatsaechlicherFaktor = 1.0;
		
		for(Spieler spieler : spielerHeimmannschaft) {
			gesamtStaerkeHeimmannschaft = gesamtStaerkeHeimmannschaft + spieler.getStaerke().getDurchschnittsStaerke();
		}
		
		for(Spieler spieler : spielerGastmannschaft) {
			gesamtStaerkeGastmannschaft = gesamtStaerkeGastmannschaft + spieler.getStaerke().getDurchschnittsStaerke();
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
