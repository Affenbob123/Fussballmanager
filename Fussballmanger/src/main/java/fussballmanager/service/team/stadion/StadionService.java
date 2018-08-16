package fussballmanager.service.team.stadion;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spieler.staerke.SpielerStaerkeService;
import fussballmanager.service.tabelle.TabellenEintragService;

@Service
@Transactional
public class StadionService {

	private static final Logger LOG = LoggerFactory.getLogger(SpielerStaerkeService.class);

	@Autowired
	StadionRepository stadionRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	public Stadion findeStadion(Long id) {
		return stadionRepository.getOne(id);
	}
	
	public List<Stadion> findeAlleStadien() {
		return stadionRepository.findAll();
	}
	
	public List<Stadion> findeAlleStadienDieAusgebautWerden() {
		return stadionRepository.findByAktuellAusgebauterTypNotNull();
	}
	
	public List<Stadion> findeAlleStadienDieSitzplaetzeAusbauen() {
		return stadionRepository.findBySitzplatzAusbauTageGreaterThan(0);
	}
	
	public void legeStadionAn(Stadion stadion) {
		stadionRepository.save(stadion);
	}
	
	public void aktualisiereStadion(Stadion stadion) {
		stadionRepository.save(stadion);
	}
	
	public void loescheStadion(Stadion stadion) {
		stadionRepository.delete(stadion);
	}
	
	public void aufgabenBeiSpieltagWechsel() {
		reduziereSitzplatztage();
		reduziereStadionAusbautage();
	}

	public void reduziereStadionAusbautage() {
		List<Stadion> alleStadionMitStadionAusbau = findeAlleStadienDieAusgebautWerden();
		
		for(Stadion stadion : alleStadionMitStadionAusbau) {
			stadion.setUebrigeAusbauTage(stadion.getUebrigeAusbauTage() -1);
			if(stadion.getUebrigeAusbauTage() <= 0) {
				if(stadion.getAktuellAusgebauterTyp().equals(stadion.getTrainingsGelaende())) {
					stadion.getTrainingsGelaende().setAktuelleStufe(stadion.getTrainingsGelaende().getAktuelleStufe() + 1);
				}
				if(stadion.getAktuellAusgebauterTyp().equals(stadion.getImbiss())) {
					stadion.getImbiss().setAktuelleStufe(stadion.getImbiss().getAktuelleStufe() + 1);
				}
				if(stadion.getAktuellAusgebauterTyp().equals(stadion.getErsatzbank())) {
					stadion.getErsatzbank().setAktuelleStufe(stadion.getErsatzbank().getAktuelleStufe() + 1);
				}
				if(stadion.getAktuellAusgebauterTyp().equals(stadion.getJugendInternat())) {
					stadion.getJugendInternat().setAktuelleStufe(stadion.getJugendInternat().getAktuelleStufe() + 1);
				}
				if(stadion.getAktuellAusgebauterTyp().equals(stadion.getVipLounge())) {
					stadion.getVipLounge().setAktuelleStufe(stadion.getVipLounge().getAktuelleStufe() + 1);
				}
				if(stadion.getAktuellAusgebauterTyp().equals(stadion.getWerbebanden())) {
					stadion.getWerbebanden().setAktuelleStufe(stadion.getWerbebanden().getAktuelleStufe() + 1);
				}
				stadion.setAktuellAusgebauterTyp(null);
			}
		}
	}

	public void reduziereSitzplatztage() {
		List<Stadion> alleStadionMitSitzplatzAusbau = findeAlleStadienDieSitzplaetzeAusbauen();
		
		for(Stadion stadion : alleStadionMitSitzplatzAusbau) {
			if(stadion.getSitzplaetze() >= 100.000) {
				stadion.setSitzplatzAusbauTage(0);
			} else {
				stadion.setSitzplatzAusbauTage(stadion.getSitzplatzAusbauTage() - 1);
				stadion.setSitzplaetze(stadion.getSitzplaetze() + 1000);
			}

			aktualisiereStadion(stadion);
		}
	}
}
