package fussballmanager.service.spiel;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.spielsimulation.SpielSimulation;

@Service
@Transactional
public class SpielService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielService.class);

	@Autowired
	SpielRepository spielRepository;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	SpielSimulation spielSimulation;
	
	@Autowired
	TabellenEintragService tabellenEintragService;

	public Spiel findeSpiel(Long id) {
		return spielRepository.getOne(id);
	}
	
	public Spiel findeSpielEinesTeamsInSaisonUndSpieltagUndSpielTyp(Team team, Saison saison, Spieltag spieltag, SpieleTypen spielTyp) {
		List<Spiel> spiele = findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(saison, spieltag, spielTyp);
		for(Spiel spiel : spiele) {
			if(spiel.getHeimmannschaft().equals(team) || spiel.getGastmannschaft().equals(team)) {
				return spiel;
			}
		}
		return null;
	}
	
	public List<Spiel> findeAlleSpiele() {
		return spielRepository.findAll();
	}
		
	public List<Spiel> findeAlleSpieleEinesTeams(Team team) {
		List<Spiel> alleSpieleEinesTeams = spielRepository.findByGastmannschaft(team);
		alleSpieleEinesTeams.addAll(spielRepository.findByHeimmannschaft(team));
		
		return alleSpieleEinesTeams;
	}
	
	public List<Spiel> findeAlleSpieleEinesTeamsInEinerSaison(Team team, Saison saison) {
		List<Spiel> alleSpieleEinerSaison = spielRepository.findBySaison(saison);
		List<Spiel> alleSpieleEinesTeamsInEinerSaison = new ArrayList<>();
		for(Spiel spiel : alleSpieleEinerSaison) {
			if(spiel.getHeimmannschaft().equals(team) || spiel.getGastmannschaft().equals(team))
			alleSpieleEinesTeamsInEinerSaison.add(spiel);
		}
		return alleSpieleEinesTeamsInEinerSaison;
	}
	
	public List<Spiel> findeAlleSpieleEinerLiga(Liga liga) {
		List<Spiel> alleSpieleEinerLiga = new ArrayList<>();
		List<Team> alleTeamsDerLiga = teamService.findeAlleTeamsEinerLiga(liga);
		
		for(Team team : alleTeamsDerLiga) {
			alleSpieleEinerLiga.addAll(findeAlleSpieleEinesTeams(team));
		}
		return alleSpieleEinerLiga;
	}
	
	public List<Spiel> findeAlleSpieleEinerLigaUndSaisonUndSpieltag(Liga liga, Saison saison, Spieltag spieltag) {
		List<Spiel> alleSpieleEinerSaisonEinesSpieltages = spielRepository.findBySaisonAndSpieltag(saison, spieltag);
		List<Spiel> alleSpieleEinerLigaEinerSaisonEinesSpieltages = new ArrayList<>();
		
		for(Spiel spiel : alleSpieleEinerSaisonEinesSpieltages) {
			if(spiel.getGastmannschaft().getLiga().equals(liga) && spiel.getHeimmannschaft().getLiga().equals(liga)) {
				alleSpieleEinerLigaEinerSaisonEinesSpieltages.add(spiel);
			}
		}
		return alleSpieleEinerLigaEinerSaisonEinesSpieltages;
	}
	
	public List<Spiel> findeAlleSpieleEinerSaisonUndSpieltages(Saison saison, Spieltag spieltag) {
		return spielRepository.findBySaisonAndSpieltag(saison, spieltag);
	}
	
	public List<Spiel> findeAlleSpieleEinerSaisonUndSpieltagesNachSpielTyp(Saison saison, Spieltag spieltag, SpieleTypen spielTyp) {
		return spielRepository.findBySaisonAndSpieltagAndSpielTyp(saison, spieltag, spielTyp);
	}
	
	public List<Spiel> findeAlleSpieleEinerSaisonUndSpieltagesEinesTeams(Saison saison, Spieltag spieltag, Team team) {
		List<Spiel> alleSpieleEinesSpieltagesEinesTeams = new ArrayList<>();
		
		for(Spiel spiel : findeAlleSpieleEinesTeams(team)) {
			if(spiel.getHeimmannschaft().equals(team) || spiel.getGastmannschaft().equals(team)) {
				alleSpieleEinesSpieltagesEinesTeams.add(spiel);
			}
		}
		return alleSpieleEinesSpieltagesEinesTeams;
	}
	
	public List<Spiel> findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(Team team, SpieleTypen spielTyp, Saison saison) {
		List<Spiel> alleSpieleEinerSaison = findeAlleSpieleEinesTeamsInEinerSaison(team, saison);
		List<Spiel> alleAbgeschlossenenUndAngefangenenSpieleEinesTeamsInEinerSaison = new ArrayList<>();
		for(Spiel spiel : alleSpieleEinerSaison) {
			if((spiel.isAngefangen()) && spiel.getSpielTyp().equals(spielTyp)) {
				alleAbgeschlossenenUndAngefangenenSpieleEinesTeamsInEinerSaison.add(spiel);
			}
		}
		return alleAbgeschlossenenUndAngefangenenSpieleEinesTeamsInEinerSaison;
	}
	
	public void legeSpielAn(Spiel spiel) {
		spielRepository.save(spiel);
	}
	
	public void aktualisiereSpiel(Spiel spiel) {
		spielRepository.save(spiel);
	}
	
	public void loescheSpiel(Spiel spiel) {
		spielRepository.delete(spiel);
	}

	/*
	 * https://www.inf-schule.de/algorithmen/algorithmen/bedeutung/fallstudie_turnierplanung/station_algorithmen
	 */
	public void erstelleSpieleFuerEineLiga(Liga liga) {
		List<Team> alleTeamsEinerLiga = teamService.findeAlleTeamsEinerLiga(liga);
		Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		
		int anzahlTeams = alleTeamsEinerLiga.size();
		int spieltag = 0;
		while(spieltag < anzahlTeams - 1) {
			Spiel spielHinspiel = new Spiel();
	    	Spiel spielRueckspiel = new Spiel();
	    	
	    	//Hinspiel
	    	spielHinspiel.setSpielTyp(SpieleTypen.LIGASPIEL);
	    	spielHinspiel.setHeimmannschaft(alleTeamsEinerLiga.get(anzahlTeams - 1));
	    	spielHinspiel.setGastmannschaft(alleTeamsEinerLiga.get(spieltag));
	    	spielHinspiel.setSpielort(alleTeamsEinerLiga.get(anzahlTeams - 1).getStadion().getName());
	    	spielHinspiel.setSaison(aktuelleSaison);
	    	spielHinspiel.setSpieltag(spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, spieltag + 1));
	    	
//	    	LOG.info("Spieltag: {}, {} : {}, Liga: {}", spielHinspiel.getSpieltag().getSpieltagNummer(), spielHinspiel.getHeimmannschaft().getName(), 
//	    			spielHinspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
	    	legeSpielAn(spielHinspiel);
	    	
	    	//Rückspiel
	    	spielRueckspiel.setSpielTyp(SpieleTypen.LIGASPIEL);
	    	spielRueckspiel.setHeimmannschaft(alleTeamsEinerLiga.get(spieltag));
	    	spielRueckspiel.setGastmannschaft(alleTeamsEinerLiga.get(anzahlTeams - 1));
	    	spielRueckspiel.setSpielort(alleTeamsEinerLiga.get(spieltag).getStadion().getName());
	    	spielRueckspiel.setSaison(aktuelleSaison);
	    	spielRueckspiel.setSpieltag(spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, 17 + spieltag + 1));
	    	
//	    	LOG.info("Spieltag: {}, {} : {}, Liga: {}", spielRueckspiel.getSpieltag().getSpieltagNummer(), spielRueckspiel.getHeimmannschaft().getName(), 
//	    			spielRueckspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
	    	legeSpielAn(spielRueckspiel);
	    	
	    	int j = 1;
	    	
	    	// Modulo damit die teams abwechselnd heim und auswärts spielen
	    	while(j < anzahlTeams/2) {
	    		if(j % 2 == 0) {
	    			int a = spieltag - j;
		    		int b = spieltag + j;
		    		
		    		if(a < 0) {
		    			a = a + (anzahlTeams - 1);
		    		}
		    		
		    		if(b > anzahlTeams - 2) {
		    			b = b - (anzahlTeams - 1);
		    		}
		    		
					Spiel spielHinspiel2 = new Spiel();
			    	Spiel spielRueckspiel2 = new Spiel();
			    	
			    	//Hinspiel
			    	spielHinspiel2.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielHinspiel2.setHeimmannschaft(alleTeamsEinerLiga.get(b));
			    	spielHinspiel2.setGastmannschaft(alleTeamsEinerLiga.get(a));
			    	spielHinspiel2.setSpielort(alleTeamsEinerLiga.get(b).getStadion().getName());
			    	spielHinspiel2.setSaison(aktuelleSaison);
			    	spielHinspiel2.setSpieltag(spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, spieltag + 1));
			    	
			    	LOG.info("Spieltag: {}, {} : {}, Liga: {}", spielHinspiel2.getSpieltag().getSpieltagNummer(), spielHinspiel2.getHeimmannschaft().getName(), 
			    			spielHinspiel2.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielHinspiel2);
			    	
			    	//Rückspiel
			    	spielRueckspiel2.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielRueckspiel2.setHeimmannschaft(alleTeamsEinerLiga.get(a));
			    	spielRueckspiel2.setGastmannschaft(alleTeamsEinerLiga.get(b));
			    	spielRueckspiel2.setSpielort(alleTeamsEinerLiga.get(a).getStadion().getName());
			    	spielRueckspiel2.setSaison(aktuelleSaison);
			    	spielRueckspiel2.setSpieltag(spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, 17 + spieltag + 1));
			    	
			    	LOG.info("Spieltag: {}, {} : {}, Liga: {}", spielRueckspiel2.getSpieltag().getSpieltagNummer(), spielRueckspiel2.getHeimmannschaft().getName(), 
			    			spielRueckspiel2.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielRueckspiel2);
	    		} else {
	    			int a = spieltag - j;
		    		int b = spieltag + j;
		    		
		    		if(a < 0) {
		    			a = a + (anzahlTeams - 1);
		    		}
		    		
		    		if(b > anzahlTeams - 2) {
		    			b = b - (anzahlTeams - 1);
		    		}
		    		
					Spiel spielHinspiel2 = new Spiel();
			    	Spiel spielRueckspiel2 = new Spiel();
			    	
			    	//Hinspiel
			    	spielHinspiel2.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielHinspiel2.setHeimmannschaft(alleTeamsEinerLiga.get(a));
			    	spielHinspiel2.setGastmannschaft(alleTeamsEinerLiga.get(b));
			    	spielHinspiel2.setSpielort(alleTeamsEinerLiga.get(a).getStadion().getName());
			    	spielHinspiel2.setSaison(aktuelleSaison);
			    	spielHinspiel2.setSpieltag(spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, spieltag + 1));
			    	
//			    	LOG.info("Spieltag: {}, {} : {}, Liga: {}", spielHinspiel2.getSpieltag().getSpieltagNummer(), spielHinspiel2.getHeimmannschaft().getName(), 
//			    			spielHinspiel2.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielHinspiel2);
			    	
			    	//Rückspiel
			    	spielRueckspiel2.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielRueckspiel2.setHeimmannschaft(alleTeamsEinerLiga.get(b));
			    	spielRueckspiel2.setGastmannschaft(alleTeamsEinerLiga.get(a));
			    	spielRueckspiel2.setSpielort(alleTeamsEinerLiga.get(b).getStadion().getName());
			    	spielRueckspiel2.setSaison(aktuelleSaison);
			    	spielRueckspiel2.setSpieltag(spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, 17 + spieltag + 1));
			    	
			    	LOG.info("Spieltag: {}, {} : {}, Liga: {}", spielRueckspiel2.getSpieltag().getSpieltagNummer(), spielRueckspiel2.getHeimmannschaft().getName(), 
			    			spielRueckspiel2.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielRueckspiel2);
	    		}
	    		
	    		
	    		j = j + 1;
	    	}
	    	spieltag = spieltag + 1;
		}
	}

	public void anzahlToreEinesSpielSetzen(Spiel spiel) {
		int toreHeimmannschaft = 0;
		int toreGastmannschaft = 0;
		int toreHeimmannschaftZurHalbzeit = 0;
		int toreGastmannschaftZurHalbzeit = 0;
		
		if(!(spiel.getSpielEreignisse().isEmpty() && (spiel.getToreHeimmannschaft() > 0 || spiel.getToreGastmannschaft() > 0))) {
			for(SpielEreignis spielEreignis : spiel.getSpielEreignisse()) {
				if(spielEreignis.getAngreifer() != null) {
					if(spielEreignis.getAngreifer().equals(spiel.getHeimmannschaft())) {
						if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGETROFFEN)) {
							toreHeimmannschaft++;
							if(spielEreignis.getSpielminute() <= 45) {
								toreHeimmannschaftZurHalbzeit++;
							}
						}
					} else {
						if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGETROFFEN)) {
							toreGastmannschaft++;
							if(spielEreignis.getSpielminute() <= 45) {
								toreGastmannschaftZurHalbzeit++;
							}
						}
					}
				}
			}
			spiel.setToreHeimmannschaft(toreHeimmannschaft);
			spiel.setToreGastmannschaft(toreGastmannschaft);
			spiel.setToreHeimmannschaftZurHalbzeit(toreHeimmannschaftZurHalbzeit);
			spiel.setToreGastmannschaftZurHalbzeit(toreGastmannschaftZurHalbzeit);
			aktualisiereSpiel(spiel);
			tabellenEintragService.einenTabellenEintragAktualisieren(
					tabellenEintragService.findeTabellenEintragDurchTeamUndSaison(spiel.getHeimmannschaft(), saisonService.findeAktuelleSaison()));
			tabellenEintragService.einenTabellenEintragAktualisieren(
					tabellenEintragService.findeTabellenEintragDurchTeamUndSaison(spiel.getGastmannschaft(), saisonService.findeAktuelleSaison()));
		}
	}
	
	public void spielIstVorbei(Spiel spiel) {
		Team heimTeam = spiel.getHeimmannschaft();
		Team gastTeam = spiel.getGastmannschaft();
		
		anzahlToreEinesSpielSetzen(spiel);
		spiel.setVorbei(true);
		heimTeam.setAnzahlAuswechselungen(3);
		gastTeam.setAnzahlAuswechselungen(3);
		
		aktualisiereSpiel(spiel);
		teamService.aktualisiereTeam(heimTeam);
		teamService.aktualisiereTeam(gastTeam);
	}

	public void aufgabenNachSpiel() {
		List<Spiel> alleSpieleEinesSpieltages = findeAlleSpieleEinerSaisonUndSpieltages(saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag());
		
		for(Spiel spiel : alleSpieleEinesSpieltages) {
			spielIstVorbei(spiel);
		}
	}
}
