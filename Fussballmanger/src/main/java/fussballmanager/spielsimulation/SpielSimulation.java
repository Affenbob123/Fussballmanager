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
			if(!spiel.isVorbei()) {
				spiel.setAktuelleSpielminute(spielminute);
				spielService.aktualisiereSpiel(spiel);
				simuliereSpielminuteEinesSpieles(spiel, spielminute);
			}

		}
		torversuchService.ueberPruefeObTorversucheNochAktuell();
	}
	
	public void simuliereSpielMinuteAllerSpieleZweiteHalbzeit(SpieleTypen spielTyp, int spielminute) {
		List<Spiel> alleSpieleEinesSpieltages = 
				spielService.findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag(), spielTyp);
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			if(!spiel.isVorbei()) {
				spiel.setAktuelleSpielminute(spielminute);
				spielService.aktualisiereSpiel(spiel);
				simuliereSpielminuteEinesSpieles(spiel, spielminute);
			}
		}
		torversuchService.ueberPruefeObTorversucheNochAktuell();
	}
	
	public void simuliereSpielminuteEinesSpieles(Spiel spiel, int spielminute) {
		Spiel s = spiel;
		List<Spieler> spielerHeimmannschaft = spielerService.findeAlleSpielerEinesTeamsInAufstellung(s.getHeimmannschaft());
		List<Spieler> spielerGastmannschaft = spielerService.findeAlleSpielerEinesTeamsInAufstellung(s.getGastmannschaft());
		
		if(spielerHeimmannschaft.size() < 8) {
			s.setToreGastmannschaft(3);
			s.setToreHeimmannschaft(0);
			s.getSpielEreignisse().clear();
			spielService.spielIstVorbei(s);

		}
		
		if(spielerGastmannschaft.size() < 8) {
			s.setToreGastmannschaft(0);
			s.setToreHeimmannschaft(3);
			s.getSpielEreignisse().clear();
			spielService.spielIstVorbei(s);
		}
		
		if((spielerHeimmannschaft.size() >= 8) && (spielerGastmannschaft.size() >= 8)) {
			bestimmeAngreifer(s.getHeimVorteil(), spielerHeimmannschaft, spielerGastmannschaft);
			ermittleObEinTorversuchStattfindet(heimmannschaftAngreifer, s);
			ermittleObEsEineVerletzungGibt(heimmannschaftAngreifer, s);
			ermittleObEsEineGelbeKarteGibt(heimmannschaftAngreifer, s);
			ermittleObEsEineRoteKarteGibt(heimmannschaftAngreifer, s);
		}
	}

	public boolean isHeimmannschaftAngreifer() {
		return heimmannschaftAngreifer;
	}

	public void setHeimmannschaftAngreifer(boolean heimmannschaftAngreifer) {
		this.heimmannschaftAngreifer = heimmannschaftAngreifer;
	}
	
	public void bestimmeAngreifer(double heimVorteil, List<Spieler> spielerHeimmannschaft, List<Spieler> spielerGastmannschaft) {
		Random random = new Random();
		int zufallsZahl = random.nextInt(100);
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
			gesamtStaerkeHeimmannschaft = gesamtStaerkeHeimmannschaft + spieler.getSpielerStaerke().getStaerke();
		}
		
		for(Spieler spieler : spielerGastmannschaft) {
			gesamtStaerkeGastmannschaft = gesamtStaerkeGastmannschaft + spieler.getSpielerStaerke().getStaerke();
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
		//LOG.info("Torwart: {}, Verteidigung: {}, Mittelfeld: {}, Angriff: {}", 
		//		erfolgsWahrscheinlichkeitTorwart, erfolgsWahrscheinlichkeitAbwehr, erfolgsWahrscheinlichkeitMittelfeld, erfolgsWahrscheinlichkeitAngriff);
		//LOG.info("Torversuch: {}", wahrscheinlichkeitTorVersuch);
	}
	
	private void ermittleObEsEineRoteKarteGibt(boolean heimmannschaftAngreifer, Spiel spiel) {
		int wahrscheinlichkeit = 333;
		Random random = new Random();
		int zufallsZahl = random.nextInt(99999);
		if(heimmannschaftAngreifer) {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = 
					(int) (wahrscheinlichkeit * spiel.getGastmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spiel.getAktuelleSpielminute());
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.ROTEKARTE);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				Spieler spieler = ermittleSpielerFuerRoteKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft()));
				spielEreignis.setSpieler(spieler);
				spiel.addSpielEreignis(spielEreignis);
				
				spielerService.spielerErhaeltRoteKarte(spieler);
//				LOG.info("Spielminute: {}, Spielerpositon: {}, SpielEreignis: {}, Team: {}", spielEreignis.getSpielminute(), 
//						spielEreignis.getSpieler().getPosition().getPositionsName(), spielEreignis.getSpielereignisTyp().getBeschreibung(), spielEreignis.getTeam().getName());
			}
		} else {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = 
					(int) (wahrscheinlichkeit * spiel.getHeimmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spiel.getAktuelleSpielminute());
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.ROTEKARTE);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				Spieler spieler = ermittleSpielerFuerRoteKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft()));
				spielEreignis.setSpieler(spieler);
				spiel.addSpielEreignis(spielEreignis);
				
				spielerService.spielerErhaeltRoteKarte(spieler);
//				LOG.info("Spielminute: {}, Spielerpositon: {}, SpielEreignis: {}, Team: {}", spielEreignis.getSpielminute(), 
//						spielEreignis.getSpieler().getPosition().getPositionsName(), spielEreignis.getSpielereignisTyp().getBeschreibung(), spielEreignis.getTeam().getName());
			}
		}
	}
	
	private void ermittleObEsEineGelbeKarteGibt(boolean heimmannschaftAngreifer, Spiel spiel) {
		int wahrscheinlichkeit = 1900;
		Random random = new Random();
		int zufallsZahl = random.nextInt(99999);
		if(heimmannschaftAngreifer) {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = 
					(int) (wahrscheinlichkeit * spiel.getGastmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spiel.getAktuelleSpielminute());
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBEKARTE);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				Spieler spieler = ermittleSpielerFuerGelbeKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft()));
				if(spieler.isGelbeKarte()) {
					spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBROTEKARTE);
				}
				spielEreignis.setSpieler(spieler);
				spiel.addSpielEreignis(spielEreignis);
				
				spielerService.spielerErhaeltGelbeKarte(spieler);
//				LOG.info("Spielminute: {}, Spielerpositon: {}, SpielEreignis: {}, Team: {}", spielEreignis.getSpielminute(), 
//						spielEreignis.getSpieler().getPosition().getPositionsName(), spielEreignis.getSpielereignisTyp().getBeschreibung(), spielEreignis.getTeam().getName());
			}
		} else {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = 
					(int) (wahrscheinlichkeit * spiel.getHeimmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spiel.getAktuelleSpielminute());
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBEKARTE);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				Spieler spieler = ermittleSpielerFuerGelbeKarte(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft()));
				if(spieler.isGelbeKarte()) {
					spielEreignis.setSpielereignisTyp(SpielEreignisTypen.GELBROTEKARTE);
				}
				spielEreignis.setSpieler(spieler);
				spiel.addSpielEreignis(spielEreignis);
				
				spielerService.spielerErhaeltGelbeKarte(spieler);
//				LOG.info("Spielminute: {}, Spielerpositon: {}, SpielEreignis: {}, Team: {}", spielEreignis.getSpielminute(), 
//						spielEreignis.getSpieler().getPosition().getPositionsName(), spielEreignis.getSpielereignisTyp().getBeschreibung(), spielEreignis.getTeam().getName());
			}
		}
	}

	private void ermittleObEsEineVerletzungGibt(boolean heimmannschaftAngreifer, Spiel spiel) {
		int wahrscheinlichkeit = 388;
		Random random = new Random();
		int zufallsZahl = random.nextInt(99999);
		if(heimmannschaftAngreifer) {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = 
					(int) (wahrscheinlichkeit * spiel.getGastmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spiel.getAktuelleSpielminute());
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.VERLETZUNG);
				spielEreignis.setTeam(spiel.getGastmannschaft());
				Spieler spieler = ermittleSpielerFuerVerletzung(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getGastmannschaft()));
				spielEreignis.setSpieler(spieler);
				spiel.addSpielEreignis(spielEreignis);
				
				spielerService.spielerErhaeltVerletzung(spieler);
//				LOG.info("Spielminute: {}, Spielerpositon: {}, SpielEreignis: {}, Team: {}", spielEreignis.getSpielminute(), 
//						spielEreignis.getSpieler().getPosition().getPositionsName(), spielEreignis.getSpielereignisTyp().getBeschreibung(), spielEreignis.getTeam().getName());
			}
		} else {
			int wahrscheinlichkeitGelbeKarteProMinuteMalTausend = 
					(int) (wahrscheinlichkeit * spiel.getHeimmannschaft().getEinsatzTyp().getWahrscheinlichkeitKarteUndVerletzung());
			if(zufallsZahl <= wahrscheinlichkeitGelbeKarteProMinuteMalTausend) {
				SpielEreignis spielEreignis = new SpielEreignis();
				spielEreignis.setSpielminute(spiel.getAktuelleSpielminute());
				spielEreignis.setSpielereignisTyp(SpielEreignisTypen.VERLETZUNG);
				spielEreignis.setTeam(spiel.getHeimmannschaft());
				Spieler spieler = ermittleSpielerFuerVerletzung(spielerService.findeAlleSpielerEinesTeamsInAufstellung(spiel.getHeimmannschaft()));
				spielEreignis.setSpieler(spieler);
				spiel.addSpielEreignis(spielEreignis);
				
				spielerService.spielerErhaeltVerletzung(spieler);
//				LOG.info("Spielminute: {}, Spielerpositon: {}, SpielEreignis: {}, Team: {}", spielEreignis.getSpielminute(), 
//					spielEreignis.getSpieler().getPosition().getPositionsName(), spielEreignis.getSpielereignisTyp().getBeschreibung(), spielEreignis.getTeam().getName());
			}
		}
	}
	
	private void erstellenEinesTorversuches(Team angreifer, Team verteidiger, Spiel spiel) {
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		Spieler spieler = ermittleSpielerFuerTorversuch(spielerService.findeAlleSpielerEinesTeamsInAufstellung(angreifer));
		
		Torversuch torversuch = new Torversuch();
		torversuch.setRichtung(zufaelligeTorversuchRichtung());
		torversuch.setTorschuetze(spieler);
		torversuch.setAngreifer(angreifer);
		torversuch.setTorwart(spielerService.findeTorwartEinesTeams(verteidiger));
		torversuch.setVerteidiger(verteidiger);
		torversuch.setSpiel(spiel);
		torversuch.setSpielminute(spiel.getAktuelleSpielminute());
		torversuch.setErstellZeit(aktuelleUhrzeit);
		
		torversuchService.legeTorversuchAn(torversuch);
		
//		LOG.info("Spielminute: {}, Spielerpositon: {}, Torversuch, Angreifer: {}", torversuch.getSpielminute(), 
//				torversuch.getTorschuetze().getPosition().getPositionsName(), torversuch.getAngreifer().getName());
		//Wenn der Verteidiger keinen Torwart hat, dann wird automatisch ein Tor geschossen
		if(torversuch.getTorwart() == null) {
			torversuchService.erstelleSpielEreignisAusTorversuch(torversuch);
		}
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
