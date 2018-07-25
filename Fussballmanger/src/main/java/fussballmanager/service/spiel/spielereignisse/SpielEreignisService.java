package fussballmanager.service.spiel.spielereignisse;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.spiel.Spiel;


@Service
@Transactional
public class SpielEreignisService {
	
	@Autowired
	SpielEreignisRepository spielEreignisRepository;
	
	public SpielEreignis findeSpielEreignis(Long id) {
		return spielEreignisRepository.getOne(id);
	}
	
	public List<SpielEreignis> findeSpielEreignisse() {
		return spielEreignisRepository.findAll();
	}
	
	public void legeSpielEreignisAn(SpielEreignis spielEreignis) {
		spielEreignisRepository.save(spielEreignis);
	}
	
	public void aktualisiereSpielEreignis(SpielEreignis spielEreignis) {
		spielEreignisRepository.save(spielEreignis);
	}
	
	public void loescheSpielEreignis(SpielEreignis spielEreignis) {
		spielEreignisRepository.delete(spielEreignis);
	}
	
	public int berechneAnzahlTorVersucheHeimmannschaft(Spiel spiel) {
		int toreVersucheAnzahl = 0;
		
		for(SpielEreignis spielEreignis : spiel.getSpielEreignisse()) {
			if(spielEreignis.getTeam().equals(spiel.getHeimmannschaft())) {
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCH)) {
					toreVersucheAnzahl++;
				}
			}

		}
		return toreVersucheAnzahl;
	}
	
	public int berechneAnzahlTorVersucheGastmannschaft(Spiel spiel) {
		int toreVersucheAnzahl = 0;
		
		for(SpielEreignis spielEreignis : spiel.getSpielEreignisse()) {
			if(spielEreignis.getTeam().equals(spiel.getGastmannschaft())) {
				if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCH)) {
					toreVersucheAnzahl++;
				}
			}
		}
		return toreVersucheAnzahl;
	}
}
