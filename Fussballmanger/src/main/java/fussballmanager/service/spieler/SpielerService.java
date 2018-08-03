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

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.land.Land;
import fussballmanager.service.spieler.staerke.Staerke;
import fussballmanager.service.team.FormationsTypen;
import fussballmanager.service.team.Team;

@Service
@Transactional
public class SpielerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielerService.class);
	
	private final int  minTalentwert = 0;
	private final int maxTalentwert = 100;
	private final int anzahlSpielerProPositionUndAlter = 5;
		
	@Autowired
	SpielerRepository spielerRepository;

	public Spieler findeSpieler(Long id) {
		return spielerRepository.getOne(id);
	}
	
	public List<Spieler> findeAlleSpieler() {
		return spielerRepository.findAll();
	}
	
	public List<Spieler> findeAlleSpielerEinesTeams(Team team) {
		List<Spieler> alleSpielerEinesTeams =  spielerRepository.findByTeam(team);
		
		Collections.sort(alleSpielerEinesTeams);
		return alleSpielerEinesTeams;
	}
	
	public List<Spieler> findeAlleSpielerNachAufstellungsPositionsTyp(AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		return spielerRepository.findByAufstellungsPositionsTyp(aufstellungsPositionsTyp);
	}
	
	public List<Spieler> findeAlleSpielerEinesTeamsInAufstellung(Team team) {
		List<Spieler> alleSpielerEinesTeams =  new ArrayList<>();
		
		for(Spieler spieler : findeAlleSpielerEinesTeams(team)) {
			if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)
					|| spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ))) {
				alleSpielerEinesTeams.add(spieler);
			}

		}
		Collections.sort(alleSpielerEinesTeams);
		return alleSpielerEinesTeams;
	}
	
	public List<Spieler> findeAlleSpielerEinesTeamsAufErsatzbank(Team team) {
		List<Spieler> alleSpielerEinesTeamsAufErsatzbank =  new ArrayList<>();
		
		for(Spieler spieler : findeAlleSpielerEinesTeams(team)) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) {
				alleSpielerEinesTeamsAufErsatzbank.add(spieler);
			}
		}
		return alleSpielerEinesTeamsAufErsatzbank;
	}
	
	public void legeSpielerAn(Spieler spieler) {
		spielerRepository.save(spieler);
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
			double anfangsStaerke = 200.0;
			int talentwert = erzeugeZufaelligenTalentwert();
			Staerke staerke = new Staerke(anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke);
			Staerke reinStaerke = new Staerke(anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke);
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

	public void erstelleSpielerFuerTransfermarkt() {
		double anfangsStaerke = 50.0;
		
		for(PositionenTypen positionenTyp : PositionenTypen.values()) {
			for(int i = 0; i < getAnzahlSpielerProPositionUndAlter(); i++) {
				for(int alter = 13; alter < 19; alter++) {
					int staerkeFaktor = alter - 12;
					double anfangsStaerkeMitFaktor = anfangsStaerke * staerkeFaktor;
					double preis = anfangsStaerkeMitFaktor * 100;
					
					Staerke reinStaerke = new Staerke(anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, 
							anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor);
					Staerke staerke = new Staerke(anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, 
							anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor);
					Spieler spieler = new Spieler(null, positionenTyp, AufstellungsPositionsTypen.TRANSFERMARKT, 
							alter, reinStaerke, staerke, erzeugeZufaelligenTalentwert(), null);
					spieler.setPreis(preis);
					
					legeSpielerAn(spieler);
					LOG.info("Alter: {}, Position: {}, Team: {}, Aufstellungspos.: {}, Preis: {}", spieler.getAlter(), spieler.getPosition().getPositionsName(), 
							spieler.getTeam(), spieler.getAufstellungsPositionsTyp().getPositionsName(), spieler.getPreis());
				}	
			}
		}
	}

	public int getMinTalentwert() {
		return minTalentwert;
	}

	public int getMaxTalentwert() {
		return maxTalentwert;
	}

	public int getAnzahlSpielerProPositionUndAlter() {
		return anzahlSpielerProPositionUndAlter;
	}

	public void loescheSpielerVomTransfermarkt() {
		spielerRepository.deleteInBatch(spielerRepository.findByNationalitaetAndTeam(null, null));
	}

	public void spielerVomTransfermarktKaufen(Spieler spieler, Team team) {
		if(spieler.getNationalitaet() == null) {
			spieler.setNationalitaet(team.getLand());
			spieler.setTeam(team);
			spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
		} else {
			spieler.setTeam(team);
			spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
		}
		
		aktualisiereSpieler(spieler);
	}
	
	public List<Spieler> findeAlleSpielerAnhandDerSuchEingaben(PositionenTypen position, LaenderNamenTypen land,
			int minimalesAlter, int maximalesAlter, double minimaleStaerke, double maximaleStaerke,
			double minimalerPreis, double maximalerPreis) {
		List<Spieler> endResultat = new ArrayList<>();
		List<Spieler> zwischenResultat = spielerRepository.findByAufstellungsPositionsTypAndAlterBetweenAndPreisBetween(AufstellungsPositionsTypen.TRANSFERMARKT,
				minimalesAlter, maximalesAlter, minimalerPreis, maximalerPreis);
		
		for(Spieler spieler : zwischenResultat) {
			if(spieler.getStaerke().getDurchschnittsStaerke() <= maximaleStaerke && 
					spieler.getStaerke().getDurchschnittsStaerke() >= minimaleStaerke) {
				if(position == null) {
					if(land == null || spieler.getNationalitaet() == null) {
						endResultat.add(spieler);
						continue;
					}
					
					if(spieler.getNationalitaet().getLandNameTyp().equals(land)) {
						endResultat.add(spieler);
						continue;
					}
				}
				
				if(spieler.getPosition().equals(position)) {
					if(land == null || spieler.getNationalitaet() == null) {
						endResultat.add(spieler);
						continue;
					}
					
					if(spieler.getNationalitaet().getLandNameTyp().equals(land)) {
						endResultat.add(spieler);
						continue;
					}
				}
			}
		}
		return endResultat;
	}
//		List<Spieler> alleSpielerAufTransfermarkt = findeAlleSpielerNachAufstellungsPositionsTyp(AufstellungsPositionsTypen.TRANSFERMARKT);
//		List<Spieler> alleSpielerNachSuche =  new ArrayList<>();
//		
//		for(Spieler spieler : alleSpielerAufTransfermarkt) {
//			if(position == null) {
//				if(land == null) {
//					if(spieler.getAlter() >= minimalesAlter && spieler.getAlter() <= maximalesAlter) {
//						if(spieler.getStaerke().getDurchschnittsStaerke() >= minimaleStaerke && 
//								spieler.getStaerke().getDurchschnittsStaerke() <= maximaleStaerke) {
//							if(spieler.getPreis() >= minimalerPreis && spieler.getPreis() <= maximalerPreis) {
//								alleSpielerNachSuche.add(spieler);
//							}
//						}
//					}
//				} else {
//					if(spieler.getNationalitaet().getLandNameTyp().equals(land)) {
//						if(spieler.getAlter() >= minimalesAlter && spieler.getAlter() <= maximalesAlter) {
//							if(spieler.getStaerke().getDurchschnittsStaerke() >= minimaleStaerke && 
//									spieler.getStaerke().getDurchschnittsStaerke() <= maximaleStaerke) {
//								if(spieler.getPreis() >= minimalerPreis && spieler.getPreis() <= maximalerPreis) {
//									alleSpielerNachSuche.add(spieler);
//								}
//							}
//						}
//					}	
//				}
//			}
//			
//			if(spieler.getPosition().equals(position)) {
//				if(land == null) {
//					if(spieler.getAlter() >= minimalesAlter && spieler.getAlter() <= maximalesAlter) {
//						if(spieler.getStaerke().getDurchschnittsStaerke() >= minimaleStaerke && 
//								spieler.getStaerke().getDurchschnittsStaerke() <= maximaleStaerke) {
//							if(spieler.getPreis() >= minimalerPreis && spieler.getPreis() <= maximalerPreis) {
//								alleSpielerNachSuche.add(spieler);
//							}
//						}
//					}
//				} else {
//					if(spieler.getNationalitaet().getLandNameTyp().equals(land)) {
//						if(spieler.getAlter() >= minimalesAlter && spieler.getAlter() <= maximalesAlter) {
//							if(spieler.getStaerke().getDurchschnittsStaerke() >= minimaleStaerke && 
//									spieler.getStaerke().getDurchschnittsStaerke() <= maximaleStaerke) {
//								if(spieler.getPreis() >= minimalerPreis && spieler.getPreis() <= maximalerPreis) {
//									alleSpielerNachSuche.add(spieler);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		return alleSpielerNachSuche;
//	}
}
