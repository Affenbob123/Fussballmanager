package fussballmanager.service.spieler;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.team.Team;

@Service
@Transactional
public class SpielerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielerService.class);
	
	int minTalentwert = 0;
	int maxTalentwert = 100;
		
	@Autowired
	SpielerRepository spielerRepository;

	public Spieler findeSpieler(Long id) {
		return spielerRepository.getOne(id);
	}
	
	public List<Spieler> findeAlleSpieler() {
		return spielerRepository.findAll();
	}
	
	public void legeSpielerAn(Spieler spieler) {
		spielerRepository.save(spieler);
		LOG.info("Spieler mit Talentwert: {} und der Position: {} im Team: {} wurde angelegt.", spieler.getTalentwert(), 
			spieler.getPositionenTypen().getPosition(), spieler.getTeam().getName());
	}
	
	public void aktualisiereSpieler(Spieler spieler) {
		spielerRepository.save(spieler);
	}
	
	public void loescheSpieler(Spieler spieler) {
		spielerRepository.delete(spieler);
	}
	
	public void erstelleStandardSpieler(Team team) {
		int alter = 17;
		double staerke = 200.0;
		
		
		for(PositionenTypen positionenTyp : PositionenTypen.values()) {
			int talentwert = erzeugeZufaelligenTalentwert();
			legeSpielerAn(new Spieler(positionenTyp, alter, staerke, talentwert, team));
		}
	}
	
	public int erzeugeZufaelligenTalentwert() {
		if (minTalentwert >= maxTalentwert) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((maxTalentwert - minTalentwert) + 1) + minTalentwert;
	}
}
