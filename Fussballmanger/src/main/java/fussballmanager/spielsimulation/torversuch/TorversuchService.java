package fussballmanager.spielsimulation.torversuch;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.mvc.liveticker.LiveTickerController;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.team.Team;

@Service
@Transactional
public class TorversuchService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TorversuchService.class);

	@Autowired
	TorversuchRepository torversuchRepository;
	
	@Autowired
	SpielEreignisService spielEreignisService;
	
	@Autowired
	SpielService spielService;
	
	public Torversuch findeTorversuch(Long id) {
		return torversuchRepository.getOne(id);
	}
	
	public List<Torversuch> findeErhalteneTorversucheEinesTeams(Team verteidiger) {
		return torversuchRepository.findByVerteidiger(verteidiger);
	}
	
	public List<Torversuch> findeTorversuche() {
		return torversuchRepository.findAll();
	}
	
	public void legeTorversuchAn(Torversuch torversuch) {
		torversuchRepository.save(torversuch);
	}
	
	public void aktualisiereTorversuch(Torversuch torversuch) {
		torversuchRepository.save(torversuch);
	}
	
	public void loescheTorversuch(Torversuch torversuch) {
		torversuchRepository.delete(torversuch);
	}

	public void versucheTorversuchHalten(Torversuch torversuch) {
		Spiel spiel = torversuch.getSpiel();
		SpielEreignis spielEreignis = new SpielEreignis();
		
		spielEreignis.setTorwart(torversuch.getTorwart());
		spielEreignis.setTorschuetze(torversuch.getTorschuetze());
		spielEreignis.setAngreifer(torversuch.getAngreifer());
		spielEreignis.setVerteidiger(torversuch.getVerteidiger());
		spielEreignis.setSpielminute(torversuch.getSpielminute());
		
		if(torversuch.getRichtung().equals(torversuch.getRichtungVomUser())) {
			spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCHGEHALTEN);
		} else {
			spielEreignis.setSpielereignisTyp(SpielEreignisTypen.TORVERSUCHGETROFFEN);
		}
		loescheTorversuch(torversuch);
		
		spiel.addSpielEreignis(spielEreignis);
		
		spielService.aktualisiereSpiel(spiel);
		spielEreignisService.legeSpielEreignisAn(spielEreignis);

	}
}
