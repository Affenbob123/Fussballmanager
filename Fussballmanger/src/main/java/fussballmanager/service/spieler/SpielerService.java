package fussballmanager.service.spieler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.spieler.staerke.Staerke;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.startelf.FormationsTypen;

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
	
	public void erstelleStandardSpielerFuerEinTeam(Team team) {
		int alter = 17;
		
		Land nationalitaet = team.getLiga().getLand();
		
		for(PositionenTypen positionenTyp : PositionenTypen.values()) {
			int talentwert = erzeugeZufaelligenTalentwert();
			Staerke staerke = new Staerke(200.0, 200.0, 200.0, 200.0, 200.0, 200.0);
			Staerke reinStaerke = new Staerke(200.0, 200.0, 200.0, 200.0, 200.0, 200.0);
			AufstellungsPositionsTypen aufstellungsPositionsTyp = AufstellungsPositionsTypen.ERSATZ;
			FormationsTypen formationsTypTeam = team.getFormationsTyp();
			
			for(AufstellungsPositionsTypen a : formationsTypTeam.getAufstellungsPositionsTypen()) {
				if(positionenTyp.getPositionsName().equals(a.getPositionsName())) {
					aufstellungsPositionsTyp = a;
				}
			}

			Spieler spieler = new Spieler(nationalitaet, positionenTyp, aufstellungsPositionsTyp, alter, reinStaerke, staerke, talentwert, team);
			legeSpielerAn(spieler);
			LOG.info("Spielerstaerke: {}", spieler.getStaerke().getDurchschnittsStaerke());
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
