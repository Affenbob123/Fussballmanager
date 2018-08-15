package fussballmanager.service.spieler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.mvc.sekretariat.statistik.SortierTypen;
import fussballmanager.mvc.sekretariat.statistik.StatistikFormular;
import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsService;
import fussballmanager.service.spieler.spielerzuwachs.Trainingslager;
import fussballmanager.service.spieler.spielerzuwachs.ZuwachsFaktorAlter;
import fussballmanager.service.spieler.staerke.SpielerStaerke;
import fussballmanager.service.spieler.staerke.SpielerStaerkeService;
import fussballmanager.service.team.FormationsTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class SpielerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielerService.class);
	
	private final int  minTalentwert = 0;
	private final int maxTalentwert = 100;
	private final int anzahlSpielerProPositionUndAlter = 5;
		
	@Autowired
	SpielerRepository spielerRepository;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SpielerZuwachsService spielerZuwachsService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	SpielerStaerkeService spielerStaerkeService;
	
	@Autowired
	LandService landService;

	public Spieler findeSpieler(Long id) {
		return spielerRepository.getOne(id);
	}
	
	public List<Spieler> findeAlleSpieler() {
		return spielerRepository.findAll();
	}
	
	public List<Spieler> findeAlleSpielerEinesTeams(Team team) {
		List<Spieler> alleSpielerEinesTeams = spielerRepository.findByTeam(team);
		Collections.sort(alleSpielerEinesTeams);
		return alleSpielerEinesTeams;
	}
	
	public List<Spieler> findeAlleSpielerMitTeam() {
		return spielerRepository.findByTeamIsNotNull();
	}
	
	public Spieler findeTorwartEinesTeams(Team team) {
		List<Spieler> alleSpielerEinesTeams = findeAlleSpielerEinesTeams(team);
		
		for(Spieler spieler : alleSpielerEinesTeams) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TW)) {
				return spieler;
			}
		}
		return null;
	}
	
	public List<Spieler> findeAlleSpielerNachAufstellungsPositionsTyp(AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		return spielerRepository.findByAufstellungsPositionsTyp(aufstellungsPositionsTyp);
	}
	
	public List<Spieler> findeAlleSpielerEinesTeamsInAufstellung(Team team) {
		List<Spieler> alleSpielerEinesTeams =  new ArrayList<>();
		
		for(Spieler spieler : findeAlleSpielerEinesTeams(team)) {
			if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.GESPERRT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.VERLETZT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRAININGSLAGER))) {
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
	
	public List<Spieler> findeZwanzigSpielerNachSortierTyp(StatistikFormular statistikFormular) {
		PageRequest seite = PageRequest.of(statistikFormular.getSeitenNummer(), 15);
		List<Spieler> spielerListe = new ArrayList<>();
		Land land;
		if(statistikFormular.getLandNameTyp() == null) {
			land = landService.findeLandDurchLandName("");
		} else {
			land = landService.findeLandDurchLandName(statistikFormular.getLandNameTyp().getName());
		}
		
		if(statistikFormular.getSortierTyp().equals(SortierTypen.STAERKE)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderBySpielerStaerkeReinStaerkeDesc(seite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderBySpielerStaerkeReinStaerkeDesc(land, seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderBySpielerStaerkeReinStaerkeDesc( statistikFormular.getAlter(), seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderBySpielerStaerkeReinStaerkeDesc(statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderBySpielerStaerkeReinStaerkeDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderBySpielerStaerkeReinStaerkeDesc(land, 
						statistikFormular.getAlter(), seite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderBySpielerStaerkeReinStaerkeDesc(land, 
						statistikFormular.getPosition(), seite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderBySpielerStaerkeReinStaerkeDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), seite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.ERFAHRUNG)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByErfahrungDesc(seite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByErfahrungDesc(land, seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByErfahrungDesc( statistikFormular.getAlter(), seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByErfahrungDesc(statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByErfahrungDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByErfahrungDesc(land, 
						statistikFormular.getAlter(), seite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByErfahrungDesc(land, 
						statistikFormular.getPosition(), seite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderByErfahrungDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), seite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.TORE)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByToreDesc(seite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByToreDesc(land, seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByToreDesc( statistikFormular.getAlter(), seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByToreDesc(statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByToreDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByToreDesc(land, 
						statistikFormular.getAlter(), seite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByToreDesc(land, 
						statistikFormular.getPosition(), seite);
			} else {
				spielerListe = spielerRepository.findAllByNationalitaetAndAlterAndPositionOrderByToreDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), seite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.GELBEKARTEN)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByGelbeKartenDesc(seite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByGelbeKartenDesc(land, seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByGelbeKartenDesc( statistikFormular.getAlter(), seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByGelbeKartenDesc(statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByGelbeKartenDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByGelbeKartenDesc(land, 
						statistikFormular.getAlter(), seite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByGelbeKartenDesc(land, 
						statistikFormular.getPosition(), seite);
			} else {
				spielerListe = spielerRepository.findAllByNationalitaetAndAlterAndPositionOrderByGelbeKartenDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), seite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.GELBROTEKARTEN)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByGelbRoteKartenDesc(seite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByGelbRoteKartenDesc(land, seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByGelbRoteKartenDesc( statistikFormular.getAlter(), seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByGelbRoteKartenDesc(statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByGelbRoteKartenDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByGelbRoteKartenDesc(land, 
						statistikFormular.getAlter(), seite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByGelbRoteKartenDesc(land, 
						statistikFormular.getPosition(), seite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderByGelbRoteKartenDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), seite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.ROTEKARTEN)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByRoteKartenDesc(seite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByRoteKartenDesc(land, seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByRoteKartenDesc( statistikFormular.getAlter(), seite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByRoteKartenDesc(statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByRoteKartenDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), seite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByRoteKartenDesc(land, 
						statistikFormular.getAlter(), seite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByRoteKartenDesc(land, 
						statistikFormular.getPosition(), seite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderByRoteKartenDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), seite);
			}
		}
		return spielerListe;
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
		int alter = 16;
		
		Land nationalitaet = team.getLiga().getLand();
		
		for(PositionenTypen positionenTyp : PositionenTypen.values()) {
			double anfangsStaerke = 200.0;
			int talentwert = erzeugeZufaelligenTalentwert();
			SpielerStaerke staerke = new SpielerStaerke(anfangsStaerke, anfangsStaerke, anfangsStaerke, 
					anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke, anfangsStaerke);
			spielerStaerkeService.legeSpielerStaerkeAn(staerke);
			AufstellungsPositionsTypen aufstellungsPositionsTyp = AufstellungsPositionsTypen.ERSATZ;
			FormationsTypen formationsTypTeam = team.getFormationsTyp();
			
			for(AufstellungsPositionsTypen a : formationsTypTeam.getAufstellungsPositionsTypen()) {
				if(positionenTyp.getPositionsName().equals(a.getPositionsName())) {
					aufstellungsPositionsTyp = a;
				}
			}
			Spieler spieler = new Spieler(nationalitaet, positionenTyp, aufstellungsPositionsTyp, alter, staerke, talentwert, team);
			legeSpielerAn(spieler);
		}
		teamService.berechneTeamStaerke(team);
	}
	
	public int erzeugeZufaelligenTalentwert() {
		Random r = new Random();
		return r.nextInt((maxTalentwert - minTalentwert) + 1) + minTalentwert;
	}
	
	public void kompletteStaerkeAendern(Spieler spieler, double staerkeAenderungsFaktor) {
		spieler.getSpielerStaerke().setDribbeln(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		spieler.getSpielerStaerke().setGeschwindigkeit(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		spieler.getSpielerStaerke().setPassen(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		spieler.getSpielerStaerke().setPhysis(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		spieler.getSpielerStaerke().setSchiessen(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		spieler.getSpielerStaerke().setVerteidigen(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		spieler.getSpielerStaerke().setStaerke(spieler.getSpielerStaerke().getReinStaerke() * staerkeAenderungsFaktor);
		
		aktualisiereSpieler(spieler);
	}
	
	public void reinStaerkeAendern(Spieler spieler, double zuwachs) {
		spieler.getSpielerStaerke().setReinStaerke(spieler.getSpielerStaerke().getReinStaerke() + zuwachs);
		aktualisiereSpieler(spieler);
	}
	
	public List<Spieler> sortiereSpielerNachStaerke(List<Spieler> spieler) {
		List<Spieler> spielerListe = spieler;
		
		Collections.sort(spielerListe, new Comparator<Spieler>() {
			@Override
			public int compare(Spieler s1, Spieler s2) {
				return Double.compare(s1.getSpielerStaerke().getStaerke(), s2.getSpielerStaerke().getStaerke());
			}
		});
		return spielerListe;
		
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
				for(int alter = 14; alter < 20; alter++) {
					int staerkeFaktor = alter - 13;
					double anfangsStaerkeMitFaktor = anfangsStaerke * staerkeFaktor;
					long preis = (long) (anfangsStaerkeMitFaktor * 1000);
					SpielerStaerke staerke = new SpielerStaerke(anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, 
							anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, 
							anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor);
					spielerStaerkeService.legeSpielerStaerkeAn(staerke);
					Spieler spieler = new Spieler(null, positionenTyp, AufstellungsPositionsTypen.ERSATZ, 
							alter, staerke, erzeugeZufaelligenTalentwert(), null);
					spieler.setTransfermarkt(true);
					spieler.setPreis(preis);
					
					legeSpielerAn(spieler);
//					LOG.info("Alter: {}, Position: {}, Team: {}, Aufstellungspos.: {}, Preis: {}", spieler.getAlter(), spieler.getPosition().getPositionsName(), 
//							spieler.getTeam(), spieler.getAufstellungsPositionsTyp().getPositionsName(), spieler.getPreis());
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
		}
		
		spieler.setTeam(team);
		spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
		spieler.setTransfermarkt(false);
		aktualisiereSpieler(spieler);
	}
	
	public List<Spieler> findeAlleSpielerAnhandDerSuchEingaben(PositionenTypen position, LaenderNamenTypen land,
			int minimalesAlter, int maximalesAlter, double minimaleStaerke, double maximaleStaerke,
			long minimalerPreis, long maximalerPreis) {
		List<Spieler> endResultat = new ArrayList<>();
		List<Spieler> zwischenResultat = spielerRepository.findByTransfermarktAndAlterBetweenAndPreisBetween(true,
				minimalesAlter, maximalesAlter, minimalerPreis, maximalerPreis);
		
		for(Spieler spieler : zwischenResultat) {
			if(spieler.getSpielerStaerke().getStaerke() <= maximaleStaerke && 
					spieler.getSpielerStaerke().getStaerke() >= minimaleStaerke) {
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

	public void ermittleTalentwert(Spieler spieler) {
		spieler.setTalentwertErmittelt(true);
		aktualisiereSpieler(spieler);
	}

	public void spielerAufTransfermarktStellen(Spieler spieler, long preis) {
		spieler.setPreis(preis);
		spieler.setTransfermarkt(true);
		aktualisiereSpieler(spieler);
	}

	public void spielerVonTransfermarktNehmen(Spieler spieler) {
		spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
		spieler.setPreis((long) (spieler.getSpielerStaerke().getReinStaerke() * 1000));
		spieler.setTransfermarkt(false);
		aktualisiereSpieler(spieler);
	}

	public void spielerEntlassen(Spieler spieler) {
		spieler.setTransfermarkt(true);
		spieler.setTeam(null);
		aktualisiereSpieler(spieler);		
	}

	public void wechsleSpielerEin(Spieler einzuwechselnderSpieler, Spieler auszuwechselnderSpieler) {
		if(auszuwechselnderSpieler != null) {
			auszuwechselnderSpieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
			aktualisiereSpieler(auszuwechselnderSpieler);
		}
		
		einzuwechselnderSpieler.setAufstellungsPositionsTyp(einzuwechselnderSpieler.getAufstellungsPositionsTyp());
		aktualisiereSpieler(einzuwechselnderSpieler);
	}

	public void wechsleSpielerEin(Spieler einzugewechselterSpieler, AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		Team team = teamService.findeTeam(einzugewechselterSpieler.getTeam().getId());
		List<Spieler> spielerInAufstellung = findeAlleSpielerEinesTeamsInAufstellung(team);
		
		for(AufstellungsPositionsTypen position : team.getFormationsTyp().getAufstellungsPositionsTypen()) {
			if(position.equals(aufstellungsPositionsTyp)) {
				for(Spieler auszuwechselnderSpieler : spielerInAufstellung) {
					if(auszuwechselnderSpieler.getAufstellungsPositionsTyp().equals(position)) {
						aenderPositionenEinesSpielers(auszuwechselnderSpieler, AufstellungsPositionsTypen.ERSATZ);
						break;
					}
				}
				aenderPositionenEinesSpielers(einzugewechselterSpieler, position);
				break;
			}
		}
		team.setAnzahlAuswechselungen(team.getAnzahlAuswechselungen() - 1);
		teamService.aktualisiereTeam(team);
	}
	
	public void aenderPositionenEinesSpielers(Spieler spieler, AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		Spieler s = spieler;
		s.setAufstellungsPositionsTyp(aufstellungsPositionsTyp);
		
		aktualisiereSpieler(s);
	}

	public void alleSpielerAltern() {
		List<Spieler> alleSpieler = findeAlleSpieler();
		
		for(Spieler spieler : alleSpieler) {
			spieler.setAlter(spieler.getAlter() + 1);
			if(spieler.getAlter() >= 35) {
				loescheSpieler(spieler);
			}
		}
	}
	
	public void spielerErzieltTor(Spieler spieler) {
		spieler.setTore(spieler.getTore() + 1);
		aktualisiereSpieler(spieler);
	}
	
	public void spielerErhaeltGelbeKarte(Spieler spieler) {
		if(spieler.isGelbeKarte()) {
			spielerErhaeltGelbRoteKarte(spieler);
		} else {
			spieler.setGelbeKarte(true);
		}
		spieler.setGelbeKarten(spieler.getGelbeKarten() + 1);
		//TODO Gesperrt bei 5 gelben karten
		aktualisiereSpieler(spieler);
	}
	
	public void spielerErhaeltGelbRoteKarte(Spieler spieler) {
		spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.GESPERRT);
		spieler.setGelbRoteKarten(spieler.getGelbRoteKarten() + 1);
		spieler.setGesperrteTage(2);
		aktualisiereSpieler(spieler);
	}
	
	public void spielerErhaeltRoteKarte(Spieler spieler) {
		spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.GESPERRT);
		spieler.setRoteKarten(spieler.getRoteKarten() + 1);
		spieler.setGesperrteTage(3);
		aktualisiereSpieler(spieler);
	}
	
	public void spielerErhaeltVerletzung(Spieler spieler) {
		Spieler eingewechselterSpieler = findeStaerkstenSpielerFuerVerletztenSpieler(spieler);
		if(eingewechselterSpieler != null) {
			eingewechselterSpieler.setAufstellungsPositionsTyp(spieler.getAufstellungsPositionsTyp());
			aktualisiereSpieler(eingewechselterSpieler);
		}
		Random random = new Random();
		int zufallsZahl = random.nextInt(4);
		spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.VERLETZT);
		spieler.setVerletzungsTage(zufallsZahl + 1);
		aktualisiereSpieler(spieler);
		
	}
	
	public Spieler findeStaerkstenSpielerFuerVerletztenSpieler(Spieler spieler) {
		List<Spieler> alleSpielerAufErsatzbank = findeAlleSpielerEinesTeamsAufErsatzbank(spieler.getTeam());
		alleSpielerAufErsatzbank = sortiereSpielerNachStaerke(alleSpielerAufErsatzbank);
		
		if(alleSpielerAufErsatzbank.isEmpty()) {
			return null;
		}
		return alleSpielerAufErsatzbank.get(0);
	}
	
	public void setGelbeKartenZurueck() {
		List<Spieler> alleSpielerMitGelberKarte = spielerRepository.findByGelbeKarteTrue();
		
		for(Spieler spieler : alleSpielerMitGelberKarte) {
			spieler.setGelbeKarte(false);
			aktualisiereSpieler(spieler);
		}
	}

	public void aufgabenBeiSpieltagWechsel() {
		List<Spieler> alleSpielerMitTeam = findeAlleSpielerMitTeam();
		
		for(Spieler spieler: alleSpielerMitTeam) {
			spieler.setSpielerZuwachs(berechneSpielerZuwachsFuerEinenSpieler(spieler));
			reinStaerkeAendern(spieler, spieler.getSpielerZuwachs());
			ueberpruefeUndBucheTrainingslager(spieler);
			reduziereVerletzungSperreTrainingslager(spieler);
		}		
	}
	
	public void ueberpruefeUndBucheTrainingslager(Spieler spieler) {
		if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRAININGSLAGER) || 
				spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.VERLETZT) ||
				spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.GESPERRT)) && spieler.getTrainingslagerTage() > 0) {
			spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.TRAININGSLAGER);
			aktualisiereSpieler(spieler);
		}
	}

	public double berechneSpielerZuwachsFuerEinenSpieler(Spieler spieler) {
		double defaultZuwachs = 2.0;
		double maximaleErfahrung = 75;
		
		int alter = spieler.getAlter();
		int talentwert = spieler.getTalentwert();
		int erfahrung = spieler.getErfahrung();
		int anzahlDerSaisonsDesSpielers = alter - 13;
		double zuwachsFaktorNachAlterDesSpielers = 1.0;
		for(ZuwachsFaktorAlter zFA : ZuwachsFaktorAlter.values()) {
			if(zFA.getAlter() == spieler.getAlter()) {
				zuwachsFaktorNachAlterDesSpielers = zFA.getZuwachsFaktor();
			}
		}
		
		double erfahrungsFaktorRechnungEins  = erfahrung * 1.0 / (maximaleErfahrung * anzahlDerSaisonsDesSpielers);
		double erfahrungsFaktor = (erfahrungsFaktorRechnungEins + 1) / 2;
		double zuwachsOhneErfahrung = defaultZuwachs * zuwachsFaktorNachAlterDesSpielers * (100 + (talentwert * 2)) / 100;
		double zuwachsMitErfahrung = zuwachsOhneErfahrung * erfahrungsFaktor;
		
		if(!spieler.getTrainingslager().equals(Trainingslager.KEIN_TRAININGSLAGER)) {
			zuwachsMitErfahrung = zuwachsMitErfahrung * spieler.getTrainingslager().getInternatFaktor();
		}
		return zuwachsMitErfahrung;
	}

	public void reduziereVerletzungSperreTrainingslager(Spieler spieler) {
		if(spieler.getGesperrteTage() > 0) {
			spieler.setGesperrteTage(spieler.getGesperrteTage() - 1);
			if(spieler.getGesperrteTage() == 0) {
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
			}
		}
		if(spieler.getVerletzungsTage() > 0) {
			spieler.setVerletzungsTage(spieler.getVerletzungsTage() - 1);
			if(spieler.getVerletzungsTage() == 0) {
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
			}
		}
		if(spieler.getTrainingslagerTage() > 0) {
			spieler.setTrainingslagerTage(spieler.getTrainingslagerTage() - 1);
			spieler.setUebrigeTrainingslagerTage(spieler.getUebrigeTrainingslagerTage() - 1);
			if(spieler.getTrainingslagerTage() == 0) {
				spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
				spieler.setTrainingsLager(Trainingslager.KEIN_TRAININGSLAGER);
			}
		}
	}
	
	public void aufgabenNachSpiel() {
		List<Spieler> alleSpieler = findeAlleSpieler();

		for(Spieler spieler : alleSpieler) {
			if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.VERLETZT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.GESPERRT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRAININGSLAGER))) {
				spieler.setErfahrung(spieler.getErfahrung() + 1);
				aktualisiereSpieler(spieler);
			}
		}
		setGelbeKartenZurueck();
	}

	public List<Spieler> findeAlleSpielerEinesTeamsMitTrainingslagerTagen(Team team) {
		return spielerRepository.findByTeamAndUebrigeTrainingslagerTageGreaterThan(team, 0);
	}
}
