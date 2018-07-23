package fussballmanager.service.spielereignisse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;

@Service
@Transactional
public class SpielEreignisService {

	@Autowired
	SpielEreignisRepository spielEreignisRepository;
	
	@Autowired
	SpielService spielService;
	
	public SpielEreignis findeSpielEreignis(Long id) {
		return spielEreignisRepository.getOne(id);
	}
	
	public List<SpielEreignis> findeAlleSpielEreignisse() {
		return spielEreignisRepository.findAll();
	}
	
	public List<SpielEreignis> findeAlleSpielEreignisseEinerLiga(Liga liga) {
		List<SpielEreignis> alleSpielEreignisseEinerLiga = new ArrayList<>();
		
		for(SpielEreignis spielEreignis : findeAlleSpielEreignisse()) {
			for(Spiel spiel : spielService.findeAlleSpieleEinerLiga(liga)) {
				if(spielEreignis.getSpiel().equals(spiel)) {
					alleSpielEreignisseEinerLiga.add(spielEreignis);
				}
			}
		}
		return alleSpielEreignisseEinerLiga;
	}
	
	public void legeSpielEreignisAn(SpielEreignis spielEreignis) {
		spielEreignisRepository.save(spielEreignis);
	}
	
	public void aktualisiereSpielEreignis(SpielEreignis spielEreignis) {
		spielEreignisRepository.save(spielEreignis);
	}
	
	public void loescheSpielEreignis(SpielEreignis spielEreignis) {
		spielEreignisRepository.delete(spielEreignis);
	}
}
