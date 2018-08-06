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
		List<Spiel> alleSpieleEinesTeams = findeAlleSpieleEinesTeams(team);
		List<Spiel> alleSpieleEinesTeamsInEinerSaison = new ArrayList<>();
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison))
			alleSpieleEinesTeamsInEinerSaison.add(spiel);
		}
		return alleSpieleEinesTeams;
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
	
	public List<Spiel> findeAlleSpieleEinesTeamsNachSpielTypUndSaison(Team team, SpieleTypen spielTyp, Saison saison) {
		List<Spiel> alleSpieleEinesTeamsInEinerSaison = findeAlleSpieleEinesTeamsInEinerSaison(team, saison);
		
		for(Spiel spiel : alleSpieleEinesTeamsInEinerSaison) {
			if(spiel.isVorbei() && spiel.getSpielTyp().equals(spielTyp)) {
				alleSpieleEinesTeamsInEinerSaison.add(spiel);
			}
		}
		return alleSpieleEinesTeamsInEinerSaison;
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

	public void erstelleSpieleFuerEineLiga(Liga liga) {
    	List<Team> alleTeamsEinerLiga = teamService.findeAlleTeamsEinerLiga(liga);
    	Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		for (int i=0; i < alleTeamsEinerLiga.size() - 1; i++) {
			if(i%2 == 0) {
				int spieltagHinspielZahl = 1;
				int spieltagRueckspielZahl = 18;
				
			    for (int j=i+1; j < alleTeamsEinerLiga.size(); j++) {
			    	Spieltag spieltagHinspiel = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, spieltagHinspielZahl);
					Spieltag spieltagRueckspiel = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, spieltagRueckspielZahl);
			    	Spiel spielHinspiel = new Spiel();
			    	Spiel spielRueckspiel = new Spiel();
			    	
			    	//Hinspiel
			    	spielHinspiel.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielHinspiel.setHeimmannschaft(alleTeamsEinerLiga.get(i));
			    	spielHinspiel.setGastmannschaft(alleTeamsEinerLiga.get(j));
			    	spielHinspiel.setSpielort(alleTeamsEinerLiga.get(i).getSpielort());
			    	spielHinspiel.setSpieltag(spieltagHinspiel);
			    	spielHinspiel.setSaison(aktuelleSaison);
			    	
//			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagHinspiel, spielHinspiel.getHeimmannschaft().getName(), 
//			    			spielHinspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielHinspiel);
			    	
			    	//Rückspiel
			    	spielRueckspiel.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielRueckspiel.setHeimmannschaft(alleTeamsEinerLiga.get(j));
			    	spielRueckspiel.setGastmannschaft(alleTeamsEinerLiga.get(i));
			    	spielRueckspiel.setSpielort(alleTeamsEinerLiga.get(j).getSpielort());
			    	spielRueckspiel.setSpieltag(spieltagRueckspiel);
			    	spielRueckspiel.setSaison(aktuelleSaison);
			    	
//			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagRueckspiel, spielRueckspiel.getHeimmannschaft().getName(), 
//			    			spielRueckspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	
			    	legeSpielAn(spielRueckspiel);
			    	spieltagHinspielZahl++;
			    	spieltagRueckspielZahl++;
			    	
			    }
			} else {
				int spieltagHinspielZahl = 17;
				int spieltagRueckspielZahl = 34;
				
			    for (int j=i+1; j < alleTeamsEinerLiga.size(); j++) {
			    	Spieltag spieltagHinspiel = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison,spieltagHinspielZahl);
					Spieltag spieltagRueckspiel = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(aktuelleSaison, spieltagRueckspielZahl);
			    	Spiel spielHinspiel = new Spiel();
			    	Spiel spielRueckspiel = new Spiel();
			    	
			    	//Hinspiel
			    	spielHinspiel.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielHinspiel.setHeimmannschaft(alleTeamsEinerLiga.get(i));
			    	spielHinspiel.setGastmannschaft(alleTeamsEinerLiga.get(j));
			    	spielHinspiel.setSpielort(alleTeamsEinerLiga.get(i).getSpielort());
			    	spielHinspiel.setSpieltag(spieltagHinspiel);
			    	spielHinspiel.setSaison(aktuelleSaison);
			    	
//			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagHinspiel, spielHinspiel.getHeimmannschaft().getName(), 
//			    			spielHinspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielHinspiel);
			    	
			    	//Rückspiel
			    	spielRueckspiel.setSpielTyp(SpieleTypen.LIGASPIEL);
			    	spielRueckspiel.setHeimmannschaft(alleTeamsEinerLiga.get(j));
			    	spielRueckspiel.setGastmannschaft(alleTeamsEinerLiga.get(i));
			    	spielRueckspiel.setSpielort(alleTeamsEinerLiga.get(j).getSpielort());
			    	spielRueckspiel.setSpieltag(spieltagRueckspiel);
			    	spielRueckspiel.setSaison(aktuelleSaison);
			    	
//			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagRueckspiel, spielRueckspiel.getHeimmannschaft().getName(), 
//			    			spielRueckspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	
			    	legeSpielAn(spielRueckspiel);
			    	spieltagHinspielZahl--;
			    	spieltagRueckspielZahl--;
			    	
			    }
			}
		}
	}
	
	public void anzahlToreEinesSpielSetzen(Spiel spiel) {
		int toreHeimmannschaft = 0;
		int toreGastmannschaft = 0;
		int toreHeimmannschaftZurHalbzeit = 0;
		int toreGastmannschaftZurHalbzeit = 0;
		
		for(SpielEreignis spielEreignis : spiel.getSpielEreignisse()) {
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
		spiel.setToreHeimmannschaft(toreHeimmannschaft);
		spiel.setToreGastmannschaft(toreGastmannschaft);
		spiel.setToreHeimmannschaftZurHalbzeit(toreHeimmannschaftZurHalbzeit);
		spiel.setToreGastmannschaftZurHalbzeit(toreGastmannschaftZurHalbzeit);
		aktualisiereSpiel(spiel);
	}
}
