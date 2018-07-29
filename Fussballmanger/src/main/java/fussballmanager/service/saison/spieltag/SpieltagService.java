package fussballmanager.service.saison.spieltag;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;

@Service
@Transactional
public class SpieltagService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpieltagService.class);
	
	@Autowired
	SpieltagRepository spieltagRepository;

	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpielService spielService;
	
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
	
	public Spieltag findeNaechstenSpieltag() {
		return findeSpieltagDurchSpieltagUndSaison(findeAktuellenSpieltag().getSpieltagNummer() + 1, saisonService.findeAktuelleSaison());
	}
	
	public List<Spieltag> findeAlleSpieltageEinerSaison(Saison saison) {
		List<Spieltag> alleSpieltageEinerSaison = new ArrayList<>();
		for(Spieltag spieltag : findeAlleSpieltage()) {
			if(spieltag.getSaison().equals(saison)) {
				alleSpieltageEinerSaison.add(spieltag);
			}
		}
		
		return alleSpieltageEinerSaison;
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
		//Erster Spieltag der Saison
		Spieltag ersterSpieltagDerSaison = new Spieltag(0, saison);
		ersterSpieltagDerSaison.setAktuellerSpieltag(true);
		
		legeSpieltagAn(ersterSpieltagDerSaison);
		LOG.info("Spieltag: {}, Saison: {}, Status: {}", 0, saison.getSaisonNummer(), findeSpieltagDurchSpieltagUndSaison(0, saison).isAktuellerSpieltag());
		
		for(int i = 1; i < saison.getSpieltage(); i++) {
			legeSpieltagAn(new Spieltag(i, saison));
			LOG.info("Spieltag: {}, Saison: {}, Status: {}", i, saison.getSaisonNummer(), findeSpieltagDurchSpieltagUndSaison(i, saison).isAktuellerSpieltag());
		}
	}
	
	public void wechsleAktuellenSpieltag() {
		Spieltag alterSpieltag = findeAktuellenSpieltag();
		Spieltag neuerSpieltag = findeNaechstenSpieltag();
		neuerSpieltag.setAktuellerSpieltag(true);
		aktualisiereSpieltag(neuerSpieltag);
		LOG.info("Neuer Spieltag: {}, Status: {}", neuerSpieltag.getSpieltagNummer(), neuerSpieltag.isAktuellerSpieltag());
		
		
		
		alterSpieltag.setAktuellerSpieltag(false);
		aktualisiereSpieltag(alterSpieltag);
		LOG.info("Alter Spieltag: {}, Status: {}", alterSpieltag.getSpieltagNummer(), alterSpieltag.isAktuellerSpieltag());
	}
	
	//@Scheduled(fixedDelay=1000)
	public void checkAktuellerSpieltag() {
		if(findeAktuellenSpieltag().getSpieltagNummer() >= saisonService.findeAktuelleSaison().getSpieltage()) {
			// aktuellen Spieltag der alten Saison auf false setzten
			Spieltag aktuellerSpieltagDerAltenSaison = findeAktuellenSpieltag();
			aktuellerSpieltagDerAltenSaison.setAktuellerSpieltag(false);
			aktualisiereSpieltag(aktuellerSpieltagDerAltenSaison);
			
			saisonService.legeSaisonAn(new Saison(saisonService.findeLetzteSasion().getSaisonNummer() + 1));
		} else {
			wechsleAktuellenSpieltag();
		}
	}
}
