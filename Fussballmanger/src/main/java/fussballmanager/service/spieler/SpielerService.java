package fussballmanager.service.spieler;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.helper.SpielstatusHelper;
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
import fussballmanager.service.spieler.staerke.SpielerReinStaerke;
import fussballmanager.service.spieler.staerke.SpielerReinStaerkeService;
import fussballmanager.service.team.FormationsTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class SpielerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielerService.class);
	
	private final int  minTalentwert = 0;
	private final int maxTalentwert = 200;
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
	SpielerReinStaerkeService spielerStaerkeService;
	
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
	
	public List<Spieler> findeFuenfzehnSpielerNachSortierTyp(StatistikFormular statistikFormular, int seite) {
		PageRequest statistikSeite = PageRequest.of(seite-1, 15);
		List<Spieler> spielerListe = new ArrayList<>();
		Land land;
		if(statistikFormular.getLandNameTyp() == null) {
			land = landService.findeLandDurchLandName("");
		} else {
			land = landService.findeLandDurchLandName(statistikFormular.getLandNameTyp().getName());
		}
		
		if(statistikFormular.getSortierTyp().equals(SortierTypen.STAERKE)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderBySpielerReinStaerkeReinStaerkeDesc(statistikSeite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderBySpielerReinStaerkeReinStaerkeDesc(land, statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderBySpielerReinStaerkeReinStaerkeDesc( statistikFormular.getAlter(), statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderBySpielerReinStaerkeReinStaerkeDesc(statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderBySpielerReinStaerkeReinStaerkeDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderBySpielerReinStaerkeReinStaerkeDesc(land, 
						statistikFormular.getAlter(), statistikSeite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderBySpielerReinStaerkeReinStaerkeDesc(land, 
						statistikFormular.getPosition(), statistikSeite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderBySpielerReinStaerkeReinStaerkeDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), statistikSeite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.ERFAHRUNG)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByErfahrungDesc(statistikSeite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByErfahrungDesc(land, statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByErfahrungDesc( statistikFormular.getAlter(), statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByErfahrungDesc(statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByErfahrungDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByErfahrungDesc(land, 
						statistikFormular.getAlter(), statistikSeite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByErfahrungDesc(land, 
						statistikFormular.getPosition(), statistikSeite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderByErfahrungDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), statistikSeite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.TORE)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByToreDesc(statistikSeite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByToreDesc(land, statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByToreDesc( statistikFormular.getAlter(), statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByToreDesc(statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByToreDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByToreDesc(land, 
						statistikFormular.getAlter(), statistikSeite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByToreDesc(land, 
						statistikFormular.getPosition(), statistikSeite);
			} else {
				spielerListe = spielerRepository.findAllByNationalitaetAndAlterAndPositionOrderByToreDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), statistikSeite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.GELBEKARTEN)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByGelbeKartenDesc(statistikSeite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByGelbeKartenDesc(land, statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByGelbeKartenDesc( statistikFormular.getAlter(), statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByGelbeKartenDesc(statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByGelbeKartenDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByGelbeKartenDesc(land, 
						statistikFormular.getAlter(), statistikSeite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByGelbeKartenDesc(land, 
						statistikFormular.getPosition(), statistikSeite);
			} else {
				spielerListe = spielerRepository.findAllByNationalitaetAndAlterAndPositionOrderByGelbeKartenDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), statistikSeite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.GELBROTEKARTEN)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByGelbRoteKartenDesc(statistikSeite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByGelbRoteKartenDesc(land, statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByGelbRoteKartenDesc( statistikFormular.getAlter(), statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByGelbRoteKartenDesc(statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByGelbRoteKartenDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByGelbRoteKartenDesc(land, 
						statistikFormular.getAlter(), statistikSeite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByGelbRoteKartenDesc(land, 
						statistikFormular.getPosition(), statistikSeite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderByGelbRoteKartenDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), statistikSeite);
			}
		}
		if(statistikFormular.getSortierTyp().equals(SortierTypen.ROTEKARTEN)) {
			if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByOrderByRoteKartenDesc(statistikSeite);
			} else if((statistikFormular.getAlter() < 0) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByNationalitaetOrderByRoteKartenDesc(land, statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getPosition() == null)) {
				spielerListe = spielerRepository.findByAlterOrderByRoteKartenDesc( statistikFormular.getAlter(), statistikSeite);
			} else if((statistikFormular.getLandNameTyp() == null) && (statistikFormular.getAlter() < 0)) {
				spielerListe = spielerRepository.findByPositionOrderByRoteKartenDesc(statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getLandNameTyp() == null) {
				spielerListe = spielerRepository.findByAlterAndPositionOrderByRoteKartenDesc(statistikFormular.getAlter(), 
						statistikFormular.getPosition(), statistikSeite);
			} else if(statistikFormular.getPosition() == null) {
				spielerListe = spielerRepository.findByNationalitaetAndAlterOrderByRoteKartenDesc(land, 
						statistikFormular.getAlter(), statistikSeite);
			} else if(statistikFormular.getAlter() < 0) {
				spielerListe = spielerRepository.findByNationalitaetAndPositionOrderByRoteKartenDesc(land, 
						statistikFormular.getPosition(), statistikSeite);
			} else {
				spielerListe = spielerRepository.findByNationalitaetAndAlterAndPositionOrderByRoteKartenDesc(land, 
						statistikFormular.getAlter(), statistikFormular.getPosition(), statistikSeite);
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
			SpielerReinStaerke staerke = new SpielerReinStaerke(anfangsStaerke, anfangsStaerke, anfangsStaerke, 
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
	
	public List<Spieler> sortiereSpielerNachStaerke(List<Spieler> spieler) {
		List<Spieler> spielerListe = spieler;
		
		Collections.sort(spielerListe, new Comparator<Spieler>() {
			@Override
			public int compare(Spieler s1, Spieler s2) {
				return Double.compare(s1.getSpielerStaerke(), s2.getSpielerStaerke());
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
					SpielerReinStaerke staerke = new SpielerReinStaerke(anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, anfangsStaerkeMitFaktor, 
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
	
	public List<Spieler> findeFuenfzehnSpielerAnhandDerSuchEingabenVomTransfermarkt(PositionenTypen position, LaenderNamenTypen landNameTyp,
			int minimalesAlter, int maximalesAlter, double minimaleStaerke, double maximaleStaerke, long minimalerPreis,
			long maximalerPreis, int seite) {
		PageRequest tranfermarktSeite = PageRequest.of(seite-1, 15);
		Land land = null;
		if(landNameTyp != null) {
			land = landService.findeLand(landNameTyp);
		}
		
		if(position == null) {
			if(land == null) {
				return spielerRepository.findByTransfermarktAndAlterBetweenAndPreisBetweenOrderBySpielerReinStaerkeReinStaerkeDesc(true,
						minimalesAlter, maximalesAlter, minimalerPreis, maximalerPreis, tranfermarktSeite);
			}
			return spielerRepository.findByTransfermarktAndNationalitaetAndAlterBetweenAndPreisBetweenOrderBySpielerReinStaerkeReinStaerkeDesc(true, land,
					minimalesAlter, maximalesAlter, minimalerPreis, maximalerPreis, tranfermarktSeite);
		}
		if(land == null) {
			return spielerRepository.findByTransfermarktAndPositionAndAlterBetweenAndPreisBetweenOrderBySpielerReinStaerkeReinStaerkeDesc(true, position,
					minimalesAlter, maximalesAlter, minimalerPreis, maximalerPreis, tranfermarktSeite);
		}
		return spielerRepository.
			findByTransfermarktAndPositionAndNationalitaetAndAlterBetweenAndSpielerReinStaerkeReinStaerkeBetweenAndPreisBetweenOrderBySpielerReinStaerkeReinStaerkeDesc(
				true, position, land, minimalesAlter, maximalesAlter, minimaleStaerke, maximaleStaerke, minimalerPreis, maximalerPreis, tranfermarktSeite);
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
		spieler.setPreis((long) (spieler.getSpielerReinStaerke().getReinStaerke() * 1000));
		spieler.setTransfermarkt(false);
		aktualisiereSpieler(spieler);
	}

	public void spielerEntlassen(Spieler spieler) {
		spieler.setTransfermarkt(true);
		spieler.setTeam(null);
		aktualisiereSpieler(spieler);		
	}

	public void wechsleSpielerEin(Spieler einzuwechselnderSpieler, Spieler auszuwechselnderSpieler) {
		Team team = teamService.findeTeam(einzuwechselnderSpieler.getTeam().getId());
		SpielstatusHelper spielstatusHelper = new SpielstatusHelper();

		if(auszuwechselnderSpieler != null) {
			auszuwechselnderSpieler.setAufstellungsPositionsTyp(einzuwechselnderSpieler.getAufstellungsPositionsTyp());
		}
		einzuwechselnderSpieler.setAufstellungsPositionsTyp(auszuwechselnderSpieler.getAufstellungsPositionsTyp());
		aktualisiereSpieler(einzuwechselnderSpieler);
		aktualisiereSpieler(auszuwechselnderSpieler);
		
		if(spielstatusHelper.getAktuellenSpielstatus() != "") {
			team.setAnzahlAuswechselungen(team.getAnzahlAuswechselungen() - 1);
		}
		teamService.aktualisiereTeam(team);

	}

	public void wechsleSpielerEin(Spieler einzuwechselnderSpieler, AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		Team team = teamService.findeTeam(einzuwechselnderSpieler.getTeam().getId());
		SpielstatusHelper spielstatusHelper = new SpielstatusHelper();
		List<Spieler> alleSpielerEinesTeams = findeAlleSpielerEinesTeams(team);
		
		for(AufstellungsPositionsTypen aufstellungsPosition : team.getFormationsTyp().getAufstellungsPositionsTypen()) {
			if(aufstellungsPosition.equals(aufstellungsPositionsTyp)) {
				for(Spieler auszuwechselnderSpieler : alleSpielerEinesTeams) {
					if(auszuwechselnderSpieler.getAufstellungsPositionsTyp().equals(aufstellungsPosition)) {
						aenderPositionenEinesSpielers(auszuwechselnderSpieler, einzuwechselnderSpieler.getAufstellungsPositionsTyp());
						break;
					}
				}
				aenderPositionenEinesSpielers(einzuwechselnderSpieler, aufstellungsPosition);
				break;
			}
		}
		if(spielstatusHelper.getAktuellenSpielstatus() != "") {
			team.setAnzahlAuswechselungen(team.getAnzahlAuswechselungen() - 1);
		}
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
			ueberpruefeUndBucheTrainingslager(spieler);
			setSpielerZuwachs(spieler);
			reduziereVerletzungSperreTrainingslager(spieler);
		}		
	}
	
	private void setSpielerZuwachs(Spieler spieler) {
		SpielerReinStaerke spielerReinStaerke = spieler.getSpielerReinStaerke();
		Random random = new Random();
		double festeZuwachsRateDesSpielers = berechneSpielerZuwachsFuerEinenSpieler(spieler);
		double spielerZuwachsSumme = 0.0;
		
		//Geschwindigkeit
		double zuwachsGeschwindigkeit;
		int zufallsZahlGeschwindigkeit = random.nextInt(40);
		
		zufallsZahlGeschwindigkeit = 20 - zufallsZahlGeschwindigkeit;
		zufallsZahlGeschwindigkeit = zufallsZahlGeschwindigkeit + 100;
		zuwachsGeschwindigkeit = (festeZuwachsRateDesSpielers * zufallsZahlGeschwindigkeit) / 100;
		spielerZuwachsSumme = spielerZuwachsSumme + zuwachsGeschwindigkeit;
		
		spielerReinStaerke.setGeschwindigkeit(spieler.getSpielerReinStaerke().getGeschwindigkeit() + zuwachsGeschwindigkeit);
		
		
		//Schiessen
		double zuwachsSchiessen;
		int zufallsZahlSchiessen = random.nextInt(40);
		
		zufallsZahlSchiessen = 20 - zufallsZahlSchiessen;
		zufallsZahlSchiessen = zufallsZahlSchiessen + 100;
		zuwachsSchiessen = (festeZuwachsRateDesSpielers * zufallsZahlSchiessen) / 100;
		spielerZuwachsSumme = spielerZuwachsSumme + zuwachsSchiessen;
		
		spielerReinStaerke.setSchiessen(spieler.getSpielerReinStaerke().getSchiessen() + zuwachsSchiessen);
		
		
		//Passen
		double zuwachsPassen;
		int zufallsZahlPassen = random.nextInt(40);
		
		zufallsZahlPassen = 20 - zufallsZahlPassen;
		zufallsZahlPassen = zufallsZahlPassen + 100;
		zuwachsPassen = (festeZuwachsRateDesSpielers * zufallsZahlPassen) / 100;
		spielerZuwachsSumme = spielerZuwachsSumme + zuwachsPassen;
		
		spielerReinStaerke.setPassen(spieler.getSpielerReinStaerke().getPassen() + zuwachsPassen);
		
		
		//Dribbeln
		double zuwachsDribbeln;
		int zufallsZahlDribbeln = random.nextInt(40);
		
		zufallsZahlDribbeln = 20 - zufallsZahlDribbeln;
		zufallsZahlDribbeln = zufallsZahlDribbeln + 100;
		zuwachsDribbeln = (festeZuwachsRateDesSpielers * zufallsZahlDribbeln) / 100;
		spielerZuwachsSumme = spielerZuwachsSumme + zuwachsDribbeln;
		
		spielerReinStaerke.setDribbeln(spieler.getSpielerReinStaerke().getDribbeln() + zuwachsDribbeln);
		
		
		//Verteidigen
		double zuwachsVerteidigen;
		int zufallsZahlVerteidigen = random.nextInt(40);
		
		zufallsZahlVerteidigen = 20 - zufallsZahlVerteidigen;
		zufallsZahlVerteidigen = zufallsZahlVerteidigen + 100;
		zuwachsVerteidigen = (festeZuwachsRateDesSpielers * zufallsZahlVerteidigen) / 100;
		spielerZuwachsSumme = spielerZuwachsSumme + zuwachsVerteidigen;
		
		spielerReinStaerke.setVerteidigen(spieler.getSpielerReinStaerke().getVerteidigen() + zuwachsVerteidigen);
		
		
		//Verteidigen
		double zuwachsPhysis;
		int zufallsZahlPhysis = random.nextInt(40);
		
		zufallsZahlPhysis = 20 - zufallsZahlPhysis;
		zufallsZahlPhysis = zufallsZahlPhysis + 100;
		zuwachsPhysis = (festeZuwachsRateDesSpielers * zufallsZahlPhysis) / 100;
		spielerZuwachsSumme = spielerZuwachsSumme + zuwachsPhysis;
		
		spielerReinStaerke.setPhysis(spieler.getSpielerReinStaerke().getPhysis() + zuwachsPhysis);
		
		double spielerZuwachs = spielerZuwachsSumme / 6;
		spieler.setSpielerZuwachs(spielerZuwachs);
		spielerReinStaerke.setReinStaerke(spieler.getSpielerReinStaerke().getReinStaerke() + spielerZuwachs);
		spielerStaerkeService.aktualisiereSpielerStaerke(spielerReinStaerke);
	}

	public long berechneGehalt(Spieler spieler) {
		long gehalt;
		gehalt = (long) (spieler.getSpielerReinStaerke().getReinStaerke() * 100); 
				
		return gehalt;
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
		double defaultZuwachs = 1.0;
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

	public List<Spieler> findeAlleSpielerEinesTeamsDieImTrainingslagerSind(Team team) {
		return spielerRepository.findByTeamAndAufstellungsPositionsTyp(team, AufstellungsPositionsTypen.TRAININGSLAGER);
	}

	public List<Spieler> findeAlleSpielerEinesTeamsDieNichtImTrainingslagerSindUndNochTLTageFreiHaben(
			Team team) {
		return spielerRepository.findByTeamAndTrainingslagerLikeAndUebrigeTrainingslagerTageGreaterThan(team, Trainingslager.KEIN_TRAININGSLAGER, 0);
	}

	public List<Spieler> alleSpielerFuerDieDasTrainingsalgerGebuchtWerdenSoll(Team team) {
		return spielerRepository.findByTeamAndAufstellungsPositionsTypNotLikeAndTrainingslagerTageGreaterThan(team, AufstellungsPositionsTypen.TRAININGSLAGER, 0);
	}
}
