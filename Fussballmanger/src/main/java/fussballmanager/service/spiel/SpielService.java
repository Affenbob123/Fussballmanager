package fussballmanager.service.spiel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class SpielService {

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
		for (int i=0; i <= teamService.findeAlleTeamsEinerLiga(liga).size() - 2; i++)
		    for (int j=i+1; j <= teamService.findeAlleTeamsEinerLiga(liga).size() - 1; j++) {
		    	Spiel spiel = new Spiel();
		    	spiel.setHeimmannschaft(teamService.findeAlleTeamsEinerLiga(liga).get(i));
		    	spiel.setGastmannschaft(teamService.findeAlleTeamsEinerLiga(liga).get(j));
		    	spiel.setSpielort(teamService.findeAlleTeamsEinerLiga(liga).get(i).getSpielort());
		    	
		    	legeSpielAn(spiel);
		    }
	}
}
