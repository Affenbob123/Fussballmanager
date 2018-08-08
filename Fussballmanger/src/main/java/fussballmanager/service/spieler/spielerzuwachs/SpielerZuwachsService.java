package fussballmanager.service.spieler.spielerzuwachs;

import java.util.List;

import javax.transaction.Transactional;

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
	
	public void loescheAlleSpielerZuwaechseEinesSpielers(Spieler spieler) {
		spielerZuwachsRepository.deleteAll(spieler.getSpielerZuwaechse());
	}
	
	public void legeSpielerZuwachsFuerAlleSpielerAn() {
		List<Spieler> alleSpieler = spielerService.findeAlleSpielerMitTeam();
		Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		Spieltag aktuellerSpieltag = spieltagService.findeAktuellenSpieltag();
		for(Spieler spieler : alleSpieler) {
			SpielerZuwachs spielerZuwachs = new SpielerZuwachs(aktuelleSaison, aktuellerSpieltag);
			spieler.addSpielerZuwaechse(spielerZuwachs);
			spielerZuwachs.setZuwachs(spielerZuwachs.berechneSpielerZuwachsFuerEinenSpieler(spieler));
			legeSpielerZuwachsAn(spielerZuwachs);
			
			spielerService.kompletteReinStaerkeAendern(spieler, spielerZuwachs.getZuwachs());
		}
	}
}
