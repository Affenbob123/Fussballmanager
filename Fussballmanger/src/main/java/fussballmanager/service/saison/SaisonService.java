package fussballmanager.service.saison;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaisonService {
	
	@Autowired
	SaisonRepository saisonRepository;

	public Saison findeSaison(Long id) {
		return saisonRepository.getOne(id);
	}
	
	public List<Saison> findeAlleSaisone() {
		return saisonRepository.findAll();
	}
	
	public void legeSaisonAn(Saison saison) {
		saisonRepository.save(saison);
	}
	
	public void aktualisiereSaison(Saison saison) {
		saisonRepository.save(saison);
	}
	
	public void loescheSaison(Saison saison) {
		saisonRepository.delete(saison);
	}
}
