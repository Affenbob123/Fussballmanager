package fussballmanager.service.spieler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.spieler.staerke.Staerke;
import fussballmanager.service.team.FormationsTypen;
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
	
	public List<Spieler> findeAlleSpielerEinesTeamsInAufstellung(Team aktuellesTeam) {
		List<Spieler> alleSpielerEinesTeams =  new ArrayList<>();
		
		for(Spieler spieler : findeAlleSpieler()) {
			if(spieler.getTeam() != null) {
				if(spieler.getTeam().equals(aktuellesTeam)) {
					if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)
							|| spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ))) {
						alleSpielerEinesTeams.add(spieler);
					}
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

			Spieler spieler = new Spieler(nationalitaet, PositionenTypen.DM , aufstellungsPositionsTyp, alter, reinStaerke, staerke, talentwert, team);
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
	
	public void kompletteStaerkeAendern(Spieler spieler, double staerkeAenderungsFaktor) {
		spieler.getStaerke().setDribbeln(spieler.getReinStaerke().getDribbeln() * staerkeAenderungsFaktor);
		spieler.getStaerke().setGeschwindigkeit(spieler.getReinStaerke().getGeschwindigkeit() * staerkeAenderungsFaktor);
		spieler.getStaerke().setPassen(spieler.getReinStaerke().getPassen() * staerkeAenderungsFaktor);
		spieler.getStaerke().setPhysis(spieler.getReinStaerke().getPhysis() * staerkeAenderungsFaktor);
		spieler.getStaerke().setSchiessen(spieler.getReinStaerke().getSchiessen() * staerkeAenderungsFaktor);
		spieler.getStaerke().setVerteidigen(spieler.getReinStaerke().getVerteidigen() * staerkeAenderungsFaktor);
		spieler.getStaerke().setDurchschnittsStaerke(spieler.getReinStaerke().getDurchschnittsStaerke() * staerkeAenderungsFaktor);
		
		aktualisiereSpieler(spieler);
	}
	
	public List<Spieler> spielerEinesTeamsSortiertNachStaerke(Team team) {
		List<Spieler> alleSpielerDesTeams = findeAlleSpielerEinesTeams(team);
		
		Collections.sort(alleSpielerDesTeams, new Comparator<Spieler>() {
			@Override
			public int compare(Spieler s1, Spieler s2) {
				return Double.compare(s1.getStaerke().getDurchschnittsStaerke(), s2.getStaerke().getDurchschnittsStaerke());
			}
		});
		return alleSpielerDesTeams;
	}
	
	public double staerkeFaktorWennAufstellungsPositionNichtPositionIst(Spieler spieler) {
		double staerkeFaktor = 1.0;
		
		PositionenTypen position = spieler.getPosition();
		AufstellungsPositionsTypen aufstellungsPositon = spieler.getAufstellungsPositionsTyp();
		
		boolean spielerIstTorwart = false;
		boolean spielerIstVerteidiger = false;
		boolean spielerIstMittelfeld = false;
		boolean spielerIstAngreifer = false;
		
		boolean spielerIstAufgestelltAlsTorwart = false;
		boolean spielerIstAufgestelltAlsVerteidiger = false;
		boolean spielerIstAufgestelltAlsMittelfeld = false;
		boolean spielerIstAufgestelltAlsAngreifer = false;
		
		//Position des Spielers ermitteln
		if(position.equals(PositionenTypen.TW)) {
			spielerIstTorwart = true;
		}
		
		if(position.equals(PositionenTypen.LV) || position.equals(PositionenTypen.LIV) || position.equals(PositionenTypen.LIB) ||
				position.equals(PositionenTypen.RIV) || position.equals(PositionenTypen.RV)) {
			spielerIstVerteidiger = true;
		}
		
		if(position.equals(PositionenTypen.LM) || position.equals(PositionenTypen.DM) || position.equals(PositionenTypen.RM) ||
				position.equals(PositionenTypen.ZM) || position.equals(PositionenTypen.OM)) {
			spielerIstMittelfeld = true;
		}
		
		if(position.equals(PositionenTypen.LS) || position.equals(PositionenTypen.MS) || position.equals(PositionenTypen.RS)) {
			spielerIstAngreifer = true;
		}
		
		//Aufstellungsposition des Spielers ermitteln
		if(aufstellungsPositon.equals(AufstellungsPositionsTypen.TW)) {
			spielerIstAufgestelltAlsTorwart = true;
		}
		
		if(aufstellungsPositon.equals(AufstellungsPositionsTypen.LV) || aufstellungsPositon.equals(AufstellungsPositionsTypen.LIV) || 
				aufstellungsPositon.equals(AufstellungsPositionsTypen.LIB) ||
				aufstellungsPositon.equals(AufstellungsPositionsTypen.RIV) || aufstellungsPositon.equals(AufstellungsPositionsTypen.RV)) {
			spielerIstAufgestelltAlsVerteidiger = true;
		}
		
		if(aufstellungsPositon.equals(AufstellungsPositionsTypen.LM) || aufstellungsPositon.equals(AufstellungsPositionsTypen.DM) || 
				aufstellungsPositon.equals(AufstellungsPositionsTypen.RM) ||
				aufstellungsPositon.equals(AufstellungsPositionsTypen.ZM) || aufstellungsPositon.equals(AufstellungsPositionsTypen.OM)) {
			spielerIstAufgestelltAlsMittelfeld = true;
		}
		
		if(aufstellungsPositon.equals(AufstellungsPositionsTypen.LS) || aufstellungsPositon.equals(AufstellungsPositionsTypen.MS) || 
				aufstellungsPositon.equals(AufstellungsPositionsTypen.RS)) {
			spielerIstAufgestelltAlsAngreifer = true;
		}
		
		//Starekefaktor ermittlung
		if(spielerIstTorwart && !spielerIstAufgestelltAlsTorwart) {
			staerkeFaktor = 0.5;
			return staerkeFaktor;
		}
		
		if(!spielerIstTorwart && spielerIstAufgestelltAlsTorwart) {
			staerkeFaktor = 0.5;
			return staerkeFaktor;
		}
		
		if(spielerIstVerteidiger && spielerIstAufgestelltAlsMittelfeld) {
			staerkeFaktor = 0.85;
			return staerkeFaktor;
		}
		
		if(!spielerIstVerteidiger && spielerIstAufgestelltAlsAngreifer) {
			staerkeFaktor = 0.7;
			return staerkeFaktor;
		}
		
		if(spielerIstMittelfeld && spielerIstAufgestelltAlsVerteidiger) {
			staerkeFaktor = 0.85;
			return staerkeFaktor;
		}
		
		if(!spielerIstMittelfeld && spielerIstAufgestelltAlsAngreifer) {
			staerkeFaktor = 0.85;
			return staerkeFaktor;
		}
		
		if(spielerIstAngreifer && spielerIstAufgestelltAlsVerteidiger) {
			staerkeFaktor = 0.7;
			return staerkeFaktor;
		}
		
		if(!spielerIstAngreifer && spielerIstAufgestelltAlsMittelfeld) {
			staerkeFaktor = 0.85;
			return staerkeFaktor;
		}
		
		return staerkeFaktor;
	}
}
