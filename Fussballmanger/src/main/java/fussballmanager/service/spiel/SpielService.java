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
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class SpielService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielService.class);

	@Autowired
	SpielRepository spielRepository;
	
	@Autowired
	TeamService teamService;

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
		for (int i=0; i < alleTeamsEinerLiga.size() - 1; i++) {
			if(i%2 == 0) {
				int spieltagHinspiel = 1;
				int spieltagRueckspiel = 18;
				
			    for (int j=i+1; j < alleTeamsEinerLiga.size(); j++) {
			    	Spiel spielHinspiel = new Spiel();
			    	Spiel spielRueckspiel = new Spiel();
			    	//Hinspiel
			    	spielHinspiel.setHeimmannschaft(alleTeamsEinerLiga.get(i));
			    	spielHinspiel.setGastmannschaft(alleTeamsEinerLiga.get(j));
			    	spielHinspiel.setSpielort(alleTeamsEinerLiga.get(i).getSpielort());
			    	spielHinspiel.setSpieltag(spieltagHinspiel);
			    	
			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagHinspiel, spielHinspiel.getHeimmannschaft().getName(), 
			    			spielHinspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielHinspiel);
			    	
			    	//Rückspiel
			    	spielRueckspiel.setHeimmannschaft(alleTeamsEinerLiga.get(j));
			    	spielRueckspiel.setGastmannschaft(alleTeamsEinerLiga.get(i));
			    	spielRueckspiel.setSpielort(alleTeamsEinerLiga.get(j).getSpielort());
			    	spielRueckspiel.setSpieltag(spieltagRueckspiel);
			    	
			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagRueckspiel, spielRueckspiel.getHeimmannschaft().getName(), 
			    			spielRueckspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	
			    	legeSpielAn(spielRueckspiel);
			    	spieltagHinspiel++;
			    	spieltagRueckspiel++;
			    	
			    }
			} else {
				int spieltagHinspiel = 17;
				int spieltagRueckspiel = 34;
				
			    for (int j=i+1; j < teamService.findeAlleTeamsEinerLiga(liga).size(); j++) {
			    	Spiel spielHinspiel = new Spiel();
			    	Spiel spielRueckspiel = new Spiel();
			    	//Hinspiel
			    	spielHinspiel.setHeimmannschaft(alleTeamsEinerLiga.get(i));
			    	spielHinspiel.setGastmannschaft(alleTeamsEinerLiga.get(j));
			    	spielHinspiel.setSpielort(alleTeamsEinerLiga.get(i).getSpielort());
			    	spielHinspiel.setSpieltag(spieltagHinspiel);
			    	
			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagHinspiel, spielHinspiel.getHeimmannschaft().getName(), 
			    			spielHinspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	legeSpielAn(spielHinspiel);
			    	
			    	//Rückspiel
			    	spielRueckspiel.setHeimmannschaft(alleTeamsEinerLiga.get(j));
			    	spielRueckspiel.setGastmannschaft(alleTeamsEinerLiga.get(i));
			    	spielRueckspiel.setSpielort(alleTeamsEinerLiga.get(j).getSpielort());
			    	spielRueckspiel.setSpieltag(spieltagRueckspiel);
			    	
			    	LOG.info("Spieltag: {}, Heimmannschaft: {}, Gastmannschaft: {}, Liga: {}", spieltagRueckspiel, spielRueckspiel.getHeimmannschaft().getName(), 
			    			spielRueckspiel.getGastmannschaft().getName(), liga.getLigaNameTyp().getName());
			    	
			    	legeSpielAn(spielRueckspiel);
			    	spieltagHinspiel--;
			    	spieltagRueckspiel--;
			    	
			    }
			}
		}
	}
}
