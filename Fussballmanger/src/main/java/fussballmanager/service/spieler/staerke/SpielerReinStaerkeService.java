package fussballmanager.service.spieler.staerke;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
public class SpielerReinStaerkeService {

	private static final Logger LOG = LoggerFactory.getLogger(SpielerReinStaerkeService.class);

	@Autowired
	SpielerReinStaerkeRepository spielerStaerkeRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	public SpielerReinStaerke findeSpielerStaerke(Long id) {
		return spielerStaerkeRepository.getOne(id);
	}
	
	public List<SpielerReinStaerke> findeAlleSpielerStaerken() {
		return spielerStaerkeRepository.findAll();
	}
	
	public List<SpielerReinStaerke> findeStaerksteSpielerNachSeite(PageRequest pageRequest) {
		return spielerStaerkeRepository.findAllByOrderByReinStaerkeDesc(pageRequest);
	}
	
	public void legeSpielerStaerkeAn(SpielerReinStaerke spielerStaerke) {
		spielerStaerkeRepository.save(spielerStaerke);
	}
	
	public void aktualisiereSpielerStaerke(SpielerReinStaerke spielerStaerke) {
		spielerStaerkeRepository.save(spielerStaerke);
	}
	
	public void loescheSpielerStaerke(SpielerReinStaerke spielerStaerke) {
		spielerStaerkeRepository.delete(spielerStaerke);
	}
}
