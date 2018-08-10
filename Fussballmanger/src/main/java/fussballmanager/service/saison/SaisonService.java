package fussballmanager.service.saison;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.TeamService;

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
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	TeamService teamService;
	
	public synchronized void ersteSaisonErstellen() {
		if(findeAlleSaisons().size() < 1) {
			Saison saison = new Saison(1);
			saison.setAktuelleSaison(true);
			legeSaisonAn(saison);
			spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(saison, 0).setAktuellerSpieltag(true);
		}
		LOG.info("aktuelle Saison: {} aktueller Spieltag: {}", findeAktuelleSaison().getSaisonNummer(), 
				spieltagService.findeAktuellenSpieltag().getSpieltagNummer());
	}

	public Saison findeSaison(Long id) {
		return saisonRepository.getOne(id);
	}
	
	public Saison findeLetzteSasion() {
		return findeAlleSaisons().get(findeAlleSaisons().size() -1);
	}
	
	public Saison findeSaisonDurchSaisonNummer(int saisonNummer) {
		return saisonRepository.findBySaisonNummer(saisonNummer);
	}
	
	public List<Saison> findeAlleSaisons() {
		return saisonRepository.findAll();
	}
	
	public Saison findeAktuelleSaison() {
		return saisonRepository.findByAktuelleSaisonTrue();
	}
	
	public void legeSaisonAn(Saison saison) {
		if(findeAlleSaisons().size() >= 1) {
			Saison alteSaison = findeAktuelleSaison();
			alteSaison.setAktuelleSaison(false);
			aktualisiereSaison(alteSaison);
			LOG.info("Saison: {} angelegt und ist: {}", findeSaison(alteSaison.getId()).getSaisonNummer(), findeSaison(alteSaison.getId()).isAktuelleSaison());
			
			spielerService.alleSpielerAltern();
			//teamService.aendereAufUndAbsteigerAllerLigen();
		}
			
		saison.setAktuelleSaison(true);
		saisonRepository.save(saison);
		LOG.info("Saison: {} angelegt und ist: {}", saison.getSaisonNummer(), saison.isAktuelleSaison());
		
		spieltagService.erstelleAlleSpieltageFuerEineSaison(findeLetzteSasion());
		erstelleSpieleFuerEineSaison(saison);
		tabellenEintragService.erstelleTabellenEintragFuerJedesTeam();
	}

	public void aktualisiereSaison(Saison saison) {
		saisonRepository.save(saison);
	}
	
	public void loescheSaison(Saison saison) {
		saisonRepository.delete(saison);
	}
	
	private void erstelleSpieleFuerEineSaison(Saison saison) {
		List<Liga> alleLigen = ligaService.findeAlleLigen();
		for(Liga liga : alleLigen) {
			spielService.erstelleSpieleFuerEineLiga(liga);
			LOG.info("Spiele für die Saison: {} und Liga: {} wurde angelegt.", saison.getSaisonNummer(), liga.getLigaNameTyp().getName());
		}
	}
}
