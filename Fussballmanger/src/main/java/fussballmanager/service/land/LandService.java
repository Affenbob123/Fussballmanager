package fussballmanager.service.land;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandRepository;

@Service
@Transactional
public class LandService {
	@Autowired
	LandRepository landRepository;

	public Land findeLand(Long id) {
		return landRepository.getOne(id);
	}
	
	public List<Land> findeAlleLaender() {
		return landRepository.findAll();
	}
	
	public void legeLandAn(Land land) {
		landRepository.save(land);
	}
	
	public void aktualisiereLand(Land land) {
		landRepository.save(land);
	}
	
	public void loescheLand(Land land) {
		landRepository.delete(land);
	}
}
