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
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.spielsimulation.SpielMinute;

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
	SpielMinute spielMinute;

	public Spiel findeSpiel(Long id) {
		return spielRepository.getOne(id);
	}
	
	public List<Spiel> findeAlleSpiele() {
		return spielRepository.findAll();
	}
	
	public List<Spiel> findeAlleSpieleEinerLiga(Liga liga) {
		List<Spiel> alleSpieleEinerLiga = new ArrayList<>();
		
		for(Spiel spiel : findeAlleSpiele()) {
			if(spiel.getGastmannschaft().getLiga().equals(liga)) {
				alleSpieleEinerLiga.add(spiel);
			}
		}
		return alleSpieleEinerLiga;
	}
	
	public List<Spiel> findeAlleSpieleEinerLigaEinerSaisonEinesSpieltages(Liga liga, Saison saison, Spieltag spieltag) {
		List<Spiel> alleSpieleEinerLigaEinerSaisonEinesSpieltages = new ArrayList<>();
		
		for(Spiel spiel : findeAlleSpiele()) {
			if(spiel.getGastmannschaft().getLiga().equals(liga) && spiel.getSpieltag().getSaison().equals(saison)
					&& spiel.getSpieltag().equals(spieltag)) {
				alleSpieleEinerLigaEinerSaisonEinesSpieltages.add(spiel);
			}
		}
		
		return alleSpieleEinerLigaEinerSaisonEinesSpieltages;
	}
	
	public List<Spiel> findeAlleSpieleEinesSpieltages(Spieltag spieltag) {
		List<Spiel> alleSpieleEinesSpieltages = new ArrayList<>();
		
		for(Spiel spiel : findeAlleSpiele()) {
			if(spiel.getSpieltag().equals(spieltag)) {
				alleSpieleEinesSpieltages.add(spiel);
			}
		}
		return alleSpieleEinesSpieltages;
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
	
	public void spielSimulieren(Spiel spiel) {
		for(int i = 0; i < 30; i++) {
			spielMinute.simuliereSpielminute(spiel, i);
		}
	}

	public void erstelleSpieleFuerEineLiga(Liga liga) {
    	List<Team> alleTeamsEinerLiga = teamService.findeAlleTeamsEinerLiga(liga);
    	Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		for (int i=0; i < alleTeamsEinerLiga.size() - 1; i++) {
			if(i%2 == 0) {
				int spieltagHinspielZahl = 1;
				int spieltagRueckspielZahl = 18;
				
			    for (int j=i+1; j < alleTeamsEinerLiga.size(); j++) {
			    	Spieltag spieltagHinspiel = spieltagService.findeSpieltagDurchSpieltagUndSaison(spieltagHinspielZahl, aktuelleSaison);
					Spieltag spieltagRueckspiel = spieltagService.findeSpieltagDurchSpieltagUndSaison(spieltagRueckspielZahl, aktuelleSaison);
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
			    	Spieltag spieltagHinspiel = spieltagService.findeSpieltagDurchSpieltagUndSaison(spieltagHinspielZahl, aktuelleSaison);
					Spieltag spieltagRueckspiel = spieltagService.findeSpieltagDurchSpieltagUndSaison(spieltagRueckspielZahl, aktuelleSaison);
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
}
