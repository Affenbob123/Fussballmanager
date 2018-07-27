package fussballmanager.service.land;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandRepository;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;

@Service
@Transactional
public class LandService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LandService.class);
	
	@Autowired
	LandRepository landRepository;
	
	@Autowired
	LigaService ligaService;
	
	@Autowired
	SaisonService saisonService;

	public Land findeLand(LaenderNamenTypen laenderNamenTypen) {
		return landRepository.getOne(laenderNamenTypen);
	}
	
	public Land findeLandDurchLandName(String landName) {
		for(Land land : findeAlleLaender()) {
			if(land.getLandNameTyp().getName().equals(landName)) {
				return land;
			}
		}
		return null;
	}
	
	public List<Land> findeAlleLaender() {
		return landRepository.findAll();
	}
	
	public void legeLandAn(Land land) {
		landRepository.save(land);
		LOG.info("Land: {} ist erstellt worden", land.getLandNameTyp().getName());
	}
	
	public void aktualisiereLand(Land land) {
		landRepository.save(land);
	}
	
	public void loescheLand(Land land) {
		landRepository.delete(land);
	}
}
