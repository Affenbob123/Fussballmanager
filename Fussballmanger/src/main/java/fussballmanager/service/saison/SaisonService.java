package fussballmanager.service.saison;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.user.UserService;

@Service
@Transactional
public class SaisonService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SaisonService.class);
	
	@Autowired
	SaisonRepository saisonRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	LigaService ligaService;

	public Saison findeSaison(Long id) {
		return saisonRepository.getOne(id);
	}
	
	public List<Saison> findeAlleSaisons() {
		return saisonRepository.findAll();
	}
	
	public void legeSaisonAn(Saison saison) {
		saison.setId(findeAlleSaisons().size() + 1);
		saisonRepository.save(saison);
		
		erstelleSpieleFuerEineSaison(findeSaison(saison.getId()));
	}

	public void aktualisiereSaison(Saison saison) {
		saisonRepository.save(saison);
	}
	
	public void loescheSaison(Saison saison) {
		saisonRepository.delete(saison);
	}
	
	private void erstelleSpieleFuerEineSaison(Saison saison) {
		for(Liga liga : ligaService.findeAlleLigen()) {
			spielService.erstelleSpieleFuerEineLiga(liga);
			LOG.info("Spiele für die Saison: {} und Liga: {} wurde angelegt.", saison.getId(), liga.getLigaNameTyp().getName());
		}
	}
}
