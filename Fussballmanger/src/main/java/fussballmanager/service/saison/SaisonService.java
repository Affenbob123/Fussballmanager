package fussballmanager.service.saison;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;

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
	
	@Autowired
	SpieltagService spieltagService;
	
	public synchronized void ersteSaisonErstellen() {
		if(findeAlleSaisons().size() < 1) {
			Saison saison = new Saison(findeAlleSaisons().size() + 1);
			legeSaisonAn(saison);
			spieltagService.erstelleAlleSpieltageFuerEineSaison(findeAlleSaisons().get(0));
			erstelleSpieleFuerEineSaison(saison);
		}
	}

	public Saison findeSaison(Long id) {
		return saisonRepository.getOne(id);
	}
	
	public List<Saison> findeAlleSaisons() {
		return saisonRepository.findAll();
	}
	
	public Saison findeAktuelleSaison() {
		return findeAlleSaisons().get(findeAlleSaisons().size()-1);
	}
	
	public void legeSaisonAn(Saison saison) {
		LOG.info("Saison: {} angelegt", saison.getSaisonAnzahl());
		saisonRepository.save(saison);
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
			LOG.info("Spiele für die Saison: {} und Liga: {} wurde angelegt.", saison.getSaisonAnzahl(), liga.getLigaNameTyp().getName());
		}
	}
}
