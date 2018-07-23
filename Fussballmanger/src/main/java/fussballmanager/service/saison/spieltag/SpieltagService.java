package fussballmanager.service.saison.spieltag;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.saison.Saison;

@Service
@Transactional
public class SpieltagService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpieltagService.class);
	
	@Autowired
	SpieltagRepository spieltagRepository;

	public Spieltag findeSpieltag(Long id) {
		return spieltagRepository.getOne(id);
	}
	
	public Spieltag findeSpieltagDurchSpieltagUndSaison(int spieltag, Saison saison) {
		for(Spieltag s : findeAlleSpieltage()) {
			if(s.getSaison().equals(saison)) {
				if(s.getSpieltagNummer() == (spieltag)) {
					return s;
				}
			}
		}
		return null;
	}
	
	public List<Spieltag> findeAlleSpieltage() {
		return spieltagRepository.findAll();
	}
	
	public Spieltag findeAktuellenSpieltag() {
		for(Spieltag spieltage : findeAlleSpieltage()) {
			if(spieltage.isAktuellerSpieltag()) {
				return spieltage;
			}
		}
		LOG.error("FEHLER BEIM FINDEN DER AKTUELLEN SPIELTAGES!!!");
		return findeAlleSpieltage().get(findeAlleSpieltage().size() - 1 );
	}
	
	public void legeSpieltagAn(Spieltag spieltag) {
		spieltagRepository.save(spieltag);	
	}

	public void aktualisiereSpieltag(Spieltag spieltag) {
		spieltagRepository.save(spieltag);
	}
	
	public void loescheSpieltag(Spieltag spieltag) {
		spieltagRepository.delete(spieltag);
	}

	public void erstelleAlleSpieltageFuerEineSaison(Saison saison) {
		for(int i = 0; i <= saison.getSpieltage(); i++) {
			legeSpieltagAn(new Spieltag(i, saison));
			LOG.info("Spieltag: {}, Saison: {}", i, saison.getSaisonNummer());
		}
	}
	
	public void checkAktuellerSpieltag() {
		
	}
}
