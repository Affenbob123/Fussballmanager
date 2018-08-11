package fussballmanager.service.spieler.staerke;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.Team;
import fussballmanager.spielsimulation.torversuch.Torversuch;
import fussballmanager.spielsimulation.torversuch.TorversuchRepository;
import fussballmanager.spielsimulation.torversuch.TorversuchService;

@Service
@Transactional
public class SpielerStaerkeService {

	private static final Logger LOG = LoggerFactory.getLogger(SpielerStaerkeService.class);

	@Autowired
	SpielerStaerkeRepository spielerStaerkeRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	public SpielerStaerke findeSpielerStaerke(Long id) {
		return spielerStaerkeRepository.getOne(id);
	}
	
	public List<SpielerStaerke> findeAlleSpielerStaerken() {
		return spielerStaerkeRepository.findAll();
	}
	
	public void legeSpielerStaerkeAn(SpielerStaerke spielerStaerke) {
		spielerStaerkeRepository.save(spielerStaerke);
	}
	
	public void aktualisiereSpielerStaerke(SpielerStaerke spielerStaerke) {
		spielerStaerkeRepository.save(spielerStaerke);
	}
	
	public void loescheSpielerStaerke(SpielerStaerke spielerStaerke) {
		spielerStaerkeRepository.delete(spielerStaerke);
	}

}
