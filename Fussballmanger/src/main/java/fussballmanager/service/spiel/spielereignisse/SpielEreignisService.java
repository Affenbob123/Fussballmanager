package fussballmanager.service.spiel.spielereignisse;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SpielEreignisService {
	
	@Autowired
	SpielEreignisRepository spielEreignisRepository;
	
	public SpielEreignis findeSpielEreignis(Long id) {
		return spielEreignisRepository.getOne(id);
	}
	
	public List<SpielEreignis> findeSpielEreignisse() {
		return spielEreignisRepository.findAll();
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
