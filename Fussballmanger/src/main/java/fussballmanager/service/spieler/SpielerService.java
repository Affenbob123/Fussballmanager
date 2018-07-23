package fussballmanager.service.spieler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
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
	
	public List<Spieler> findeAlleSpielerEinesTeams(Team aktuellesTeam) {
		List<Spieler> alleSpielerEinesTeams =  new ArrayList<>();
		
		for(Spieler spieler : findeAlleSpieler()) {
			if(spieler.getTeam() != null) {
				if(spieler.getTeam().equals(aktuellesTeam)) {
					alleSpielerEinesTeams.add(spieler);
				}
			}
		}
		Collections.sort(alleSpielerEinesTeams);
		return alleSpielerEinesTeams;
	}
	
	public void legeSpielerAn(Spieler spieler) {
		spielerRepository.save(spieler);
//		LOG.info("Spieler mit Talentwert: {} und der Position: {} im Team: {} wurde angelegt.", spieler.getTalentwert(), 
//			spieler.getPosition().getPositionsName(), spieler.getTeam().getName());
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
		Land nationalitaet = team.getLiga().getLand();
		
		for(PositionenTypen positionenTyp : PositionenTypen.values()) {
			int talentwert = erzeugeZufaelligenTalentwert();
			legeSpielerAn(new Spieler(nationalitaet, positionenTyp, alter, staerke, talentwert, team));
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
