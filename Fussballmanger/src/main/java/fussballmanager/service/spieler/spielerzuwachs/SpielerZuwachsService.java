package fussballmanager.service.spieler.spielerzuwachs;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;

@Service
@Transactional
public class SpielerZuwachsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielerZuwachsService.class);

	@Autowired
	SpielerZuwachsRepository spielerZuwachsRepository;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	public SpielerZuwachs findeSpielerZuwachs(Long id) {
		return spielerZuwachsRepository.getOne(id);
	}
	
	public List<SpielerZuwachs> findeSpielerZuwaechse() {
		return spielerZuwachsRepository.findAll();
	}
	
	public void legeSpielerZuwachsAn(SpielerZuwachs spielerZuwachs) {
		spielerZuwachsRepository.save(spielerZuwachs);
	}
	
	public void aktualisiereSpielerZuwachs(SpielerZuwachs spielerZuwachs) {
		spielerZuwachsRepository.save(spielerZuwachs);
	}
	
	public void loescheSpielerZuwachs(SpielerZuwachs spielerZuwachs) {
		spielerZuwachsRepository.delete(spielerZuwachs);
	}
//	
//	public void loescheAlleSpielerZuwaechseEinesSpielers(Spieler spieler) {
//		spielerZuwachsRepository.deleteAll(spieler.getSpielerZuwaechse());
//	}
//	
//	public void legeSpielerZuwachsFuerEinenSpielerAn(Spieler spieler, Saison saison, Spieltag spieltag) {
//		SpielerZuwachs spielerZuwachs = new SpielerZuwachs(saison, spieltag);
//		spieler.addSpielerZuwaechse(spielerZuwachs);
//		spielerZuwachs.setZuwachs(spielerZuwachs.berechneSpielerZuwachsFuerEinenSpieler(spieler));
//		legeSpielerZuwachsAn(spielerZuwachs);
//		spielerService.kompletteReinStaerkeAendern(spieler, spielerZuwachs.getZuwachs());
//	}
}
