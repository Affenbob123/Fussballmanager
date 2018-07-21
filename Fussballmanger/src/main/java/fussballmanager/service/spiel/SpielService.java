package fussballmanager.service.spiel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpielService {

	@Autowired
	SpielRepository spielRepository;

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
}
