package fussballmanager.service.land;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandRepository;
import fussballmanager.service.liga.LigaService;

@Service
@Transactional
public class LandService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LandService.class);
	
	@Autowired
	LandRepository landRepository;
	
	@Autowired
	LigaService ligaService;
	
	@PostConstruct
	public synchronized void erstelleLaender() {
		if(landRepository.count() == 0) {
			for(LaenderNamenTypen laenderNamenTypen : LaenderNamenTypen.values()) {
				legeLandAn(new Land(laenderNamenTypen));
				ligaService.legeHauptteamLigenAn(findeLand(laenderNamenTypen));
			}
		}
	}

	public Land findeLand(LaenderNamenTypen laenderNamenTypen) {
		return landRepository.getOne(laenderNamenTypen);
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
