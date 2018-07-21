package fussballmanager.service.liga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaRepository;

@Service
@Transactional
public class LigaService {

	@Autowired
	LigaRepository ligaRepository;

	public Liga findeLiga(Long id) {
		return ligaRepository.getOne(id);
	}
	
	public List<Liga> findeAlleLigen() {
		return ligaRepository.findAll();
	}
	
	public void legeLigaAn(Liga liga) {
		ligaRepository.save(liga);
	}
	
	public void aktualisiereLiga(Liga liga) {
		ligaRepository.save(liga);
	}
	
	public void loescheLiga(Liga liga) {
		ligaRepository.delete(liga);
	}
}
