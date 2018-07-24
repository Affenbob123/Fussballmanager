package fussballmanager.spielsimulation;

import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spielereignisse.SpielEreignis;
import fussballmanager.service.spielereignisse.SpielEreignisService;
import fussballmanager.service.spielereignisse.SpielEreignisTypen;

@Service
@Transactional
public class SpielMinute {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielMinute.class);
	
	@Autowired
	SpielEreignisService spielEreignisService;
	
	@Autowired
	SpielService spielService;
	
	public SpielMinute() {
		
	}
	
	public SpielEreignis simuliereSpielminute(Spiel spiel, int spielminute) {
		int zufallsZahl = ThreadLocalRandom.current().nextInt(0, 100);
		SpielEreignis spielEreignis = new SpielEreignis();
				
		if(zufallsZahl > 10) {
			spielEreignis.setSpielereignisTyp(SpielEreignisTypen.NIX);
			LOG.info("Spielereignis: {}", SpielEreignisTypen.NIX);
		} else {
			spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCHGETROFFEN);
			spielEreignis.setSpieler(null);
			spielEreignis.setSpielminute(spielminute);
			
			spiel.addSpielEreignis(spielEreignis);
			spielService.aktualisiereSpiel(spiel);
			LOG.info("Spielereignis: {}, Spieler: {}, Spielminute: {}", 
					spielEreignis.getSpielereignisTyp(), spielEreignis.getSpieler(), spielEreignis.getSpielminute());
		}
		return spielEreignis;
	}
}
