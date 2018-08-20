package fussballmanager.service.team;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.finanzen.Bilanz;
import fussballmanager.service.finanzen.BilanzService;
import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.liga.LigenNamenTypen;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.RollenTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.tabelle.TabellenEintrag;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.stadion.Stadion;
import fussballmanager.service.team.stadion.StadionService;
import fussballmanager.service.user.User;

@Service
@Transactional
public class TeamService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TeamService.class);
		
	@Autowired
	LigaService ligaService;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	BilanzService bilanzService;
	
	@Autowired
	StadionService stadionService;
	
	public Team findeTeam(Long id) {
		return teamRepository.getOne(id);
	}
	
	public Team findeTeamNachTeamName(String teamname) {
		return teamRepository.findByName(teamname);
	}
	
	public Team findeErstesDummyTeam(Land land) {
		return teamRepository.findFirstByLandAndUser(land, null);
	}
	
	public List<Team> findeAlleTeams() {
		return teamRepository.findAll();
	}
	
	public Team findeFreilosTeam() {
		return teamRepository.findByLandAndUser(null, null);
	}
	
	public List<Team> findeAlleTeamsEinesUsers(User user) {
		return teamRepository.findByUser(user);
	}
	
	public List<Team> findeAlleTeamsEinesUsersImLiveticker(User user, boolean imLiveticker) {
		return teamRepository.findByUserAndImLiveticker(user, imLiveticker);
	}
	
	public List<Team> findeAlleTeamsEinerLiga(Liga liga) {		
		return teamRepository.findByLiga(liga);
	}
	
	public void legeTeamAn(Team team) {
		teamRepository.save(team);
		
		spielerService.erstelleStandardSpielerFuerEinTeam(team);
	}
	
	public void aktualisiereTeam(Team team) {
		teamRepository.save(team);
	}
	
	public void loescheTeam(Team team) {
		teamRepository.delete(team);
	}
	
	public void erstelleDummyteams(Liga liga) {
		for(int i = 0; i < liga.getGroeße(); i++) {
			StringBuilder sb = new StringBuilder("Dummy Team");
			sb.append (i);
			String standardName = sb.toString();
			Bilanz bilanz = new Bilanz();
			bilanzService.legeBilanzAn(bilanz);
			Stadion stadion = new Stadion();
			stadionService.legeStadionAn(stadion);

			Team team = new Team(liga.getLand(), standardName, null, liga, bilanz, stadion);
			legeTeamAn(team);
		}
	}

	public void erstelleStandardHauptteamFuerEinenUser(User user) {
		Team team = findeErstesDummyTeam(user.getLand());
		team.setUser(user);
		aktualisiereTeam(team);
		user.setAktuellesTeam(findeAlleTeamsEinesUsers(user).get(0));
		
		LOG.info("Team: {} wurde dem User: {} zugewiesen.", team.getId(), team.getUser().getLogin());
		LOG.info("AktuellesTeam: {}", team.getId(), team.getUser().getLogin());
	}
	
	public void aendereEinsatzEinesTeams(Team team) {
		List<Spieler> alleSpielerDesTeams = spielerService.findeAlleSpielerEinesTeams(team);
		
		for(Spieler spieler : alleSpielerDesTeams) {
			double spielerStaerkeAenderung = team.getEinsatzTyp().getStaerkenFaktor();
			spielerService.kompletteStaerkeAendern(spieler, spielerStaerkeAenderung);
		}
		berechneTeamStaerke(team);
	}

	//TODO Wenn kein SPieler mit der Position vorhanden ist dann spielen zu wenige
	public void aenderFormationEinesTeams(Team team, List<Spieler> alleSpielerDesTeams) {
		staerksteFormationEinesTeams(team, alleSpielerDesTeams);
	}
	
	public void berechneTeamStaerke(Team team) {
		double teamStaerke = 0.0;
		List<Spieler> alleSpielerDesTeams = spielerService.findeAlleSpielerEinesTeams(team);
		
		for(Spieler spieler : alleSpielerDesTeams) {
			if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.VERLETZT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.GESPERRT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRAININGSLAGER))) {
				teamStaerke = teamStaerke + spieler.getSpielerStaerke().getStaerke();
			}
		}
		team.setStaerke(teamStaerke);
		aktualisiereTeam(team);
	}

	//TODO fml
	public void staerksteFormationEinesTeams(Team team, List<Spieler> alleSpieler) {
		FormationsTypen formationsTypDesTeams = team.getFormationsTyp();
		AufstellungsPositionsTypen aufstellung[] = formationsTypDesTeams.getAufstellungsPositionsTypen();
		List<Spieler> spielerDesTeams = alleSpieler;
		Collection<AufstellungsPositionsTypen> fehlendePositionen = new ArrayList<AufstellungsPositionsTypen>(Arrays.asList(aufstellung));
		List<Spieler> spielfaehigeSpielerDesTeams = new ArrayList<>();
		for(Spieler spieler : spielerDesTeams) {
			if(!(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.GESPERRT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.VERLETZT) ||
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRAININGSLAGER))) {
				spielfaehigeSpielerDesTeams.add(spieler);
			}
		}
		
		for(Spieler spieler : spielfaehigeSpielerDesTeams) {
			spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
			for(AufstellungsPositionsTypen a : aufstellung) {
				if(spieler.getPosition().getPositionsName().equals(a.getPositionsName()) && fehlendePositionen.contains(a)) {
					spieler.setAufstellungsPositionsTyp(a);
					fehlendePositionen.remove(a);
					spielerService.aktualisiereSpieler(spieler);
					break;
				}
			}
		}
			
		if(fehlendePositionen.size() > 0) {
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielfaehigeSpielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(a.getRollenTyp())) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(a.getRollenTyp())) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(a.getRollenTyp())) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielfaehigeSpielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.MITTELFELD)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.ANGREIFER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielfaehigeSpielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.ANGREIFER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.MITTELFELD)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielfaehigeSpielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.TORWART)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.TORWART)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.TORWART)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielfaehigeSpielerDesTeams.remove(spieler);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielfaehigeSpielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.TORWART)) {
							spieler.setAufstellungsPositionsTyp(a);
							fehlendePositionen.remove(a);
							spielfaehigeSpielerDesTeams.remove(spieler);
							spielerService.aktualisiereSpieler(spieler);
							break;
						}
					}
				}
			}
		}
		berechneTeamStaerke(team);
	}
	
	public int siegeEinesTeamsInEinerSaison(Team team, Saison saison) {
		int siege = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(team.equals(spiel.getHeimmannschaft())) {
				if(spiel.getToreHeimmannschaft() > spiel.getToreGastmannschaft()) {
					siege++;
				}
			} else {
				if(spiel.getToreGastmannschaft() > spiel.getToreHeimmannschaft()) {
					siege++;
				}
			}	
		}
		return siege;
	}

	public int unentschiedenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int unentschieden = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		int aktuellerSpieltagNummer = spieltagService.findeAktuellenSpieltag().getSpieltagNummer();
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSpieltag().getSpieltagNummer() <= aktuellerSpieltagNummer) {
				if(spiel.isAngefangen()) {
					if(spiel.getToreHeimmannschaft() == spiel.getToreGastmannschaft()) {
						unentschieden++;
					}
				}

			}
		}
		return unentschieden;
	}
	
	public int niederlagenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int niederlagen = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(team.equals(spiel.getHeimmannschaft())) {
				if(spiel.getToreHeimmannschaft() < spiel.getToreGastmannschaft()) {
					niederlagen++;
				}
			} else {
				if(spiel.getToreGastmannschaft() < spiel.getToreHeimmannschaft()) {
					niederlagen++;
				}
			}	
		}
		return niederlagen;
	}
	
	public int toreEinesTeamsInEinerSaison(Team team, Saison saison) {
		int tore = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(team.equals(spiel.getHeimmannschaft())) {
				tore = tore + spiel.getToreHeimmannschaft();
			} else {
				tore = tore + spiel.getToreGastmannschaft();
			}	
		}
		return tore;
	}
	
	public int gegenToreEinesTeamsInEinerSaison(Team team, Saison saison) {
		int gegenTore = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(team.equals(spiel.getHeimmannschaft())) {
				gegenTore = gegenTore + spiel.getToreGastmannschaft();
			} else {
				gegenTore = gegenTore + spiel.getToreHeimmannschaft();
			}	
		}
		return gegenTore;
	}
	
	public int punkteEinesTeamsInEinerSaison(Team team, Saison saison) {
		int punkte = 0;
		
		punkte = punkte + (siegeEinesTeamsInEinerSaison(team, saison) * 3);
		punkte = punkte + (unentschiedenEinesTeamsInEinerSaison(team, saison) * 1);
		
		return punkte;
	}
	
	public int gelbeKartenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int gelbeKarten = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			for(SpielEreignis spielEreignis :spiel.getSpielEreignisse()) {
				if(spielEreignis.getTeam() != null) {
					if(spielEreignis.getTeam().equals(team)) {
						if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.GELBEKARTE)) {
							gelbeKarten++;
						}
					}
				}
			}
		}
		return gelbeKarten;
	}
	
	public int gelbeRoteKartenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int gelbeRoteKarten = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			for(SpielEreignis spielEreignis :spiel.getSpielEreignisse()) {
				if(spielEreignis.getTeam() != null) {
					if(spielEreignis.getTeam().equals(team)) {
						if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.GELBROTEKARTE)) {
							gelbeRoteKarten++;
						}
					}
				}
			}
		}
		return gelbeRoteKarten;
	}
	
	public int roteKartenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int roteKarten = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenUndAngefangenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			for(SpielEreignis spielEreignis :spiel.getSpielEreignisse()) {
				if(spielEreignis.getTeam() != null) {
					if(spielEreignis.getTeam().equals(team)) {
						if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.ROTEKARTE)) {
							roteKarten++;
						}
					}
				}
			}
		}
		return roteKarten;
	}

	public void aufgabenBeiSpieltagWechsel() {
		List<Team> alleTeams = findeAlleTeams();
		for(Team team : alleTeams) {
			List<Spieler> alleSpielerDesTeams = spielerService.sortiereSpielerNachStaerke(spielerService.findeAlleSpielerEinesTeams(team));
			aenderFormationEinesTeams(team, alleSpielerDesTeams);
			bilanzService.erfasseEinUndAusgabenEinesTeamsAmSpieltagEnde(team, alleSpielerDesTeams);
		}
	}

	public void aendereLigenAllerAufUndAbsteiger() {
		List<Liga> alleLigen = ligaService.findeAlleLigen();
		for(Liga liga : alleLigen) {
			List<Team> alleTeamsEinerLiga = findeAlleTeamsEinerLiga(liga);
			if(liga.getLigaNameTyp().equals(LigenNamenTypen.ERSTELIGA)) {
				for(Team team : alleTeamsEinerLiga) {
					TabellenEintrag tabellenEintrag = tabellenEintragService.findeTabellenEintragDurchTeamUndSaison(team, saisonService.findeVorletzteSasion());
					if(tabellenEintrag.getPlatzierung() == 1) {
						teamIstMeisterGeworden(team);
					}
					
					if(tabellenEintrag.getPlatzierung() == 2) {
						teamIstVizeMeisterGeworden(team);
					}
					
					if(tabellenEintrag.getPlatzierung() == 3 || tabellenEintrag.getPlatzierung() == 4) {
						teamIstBeimWeltpokalDabei(team);
					}
					
					if(tabellenEintrag.getPlatzierung() == 15 || tabellenEintrag.getPlatzierung() == 16) {
						teamSteigtAbFuenfUndSechszehnter(team);
					}
					
					if(tabellenEintrag.getPlatzierung() == 17 || tabellenEintrag.getPlatzierung() == 18) {
						teamSteigtAbSiebenUndAchtzehnter(team);
					}
					tabellenEintrag.setPlatzierung(0);
				}
			} else {
				for(Team team : alleTeamsEinerLiga) {
					TabellenEintrag tabellenEintrag = tabellenEintragService.findeTabellenEintragDurchTeamUndSaison(team, saisonService.findeVorletzteSasion());
					if(tabellenEintrag.getPlatzierung() == 1 || tabellenEintrag.getPlatzierung() == 2) {
						teamSteigtAuf(team);
					}
					
					if(tabellenEintrag.getPlatzierung() == 15 || tabellenEintrag.getPlatzierung() == 16) {
						if(liga.getLigaNameTyp().getLigaRangfolge() < 16) {
							teamSteigtAbFuenfUndSechszehnter(team);
						}
					}
					
					if(tabellenEintrag.getPlatzierung() == 17 || tabellenEintrag.getPlatzierung() == 18) {
						if(liga.getLigaNameTyp().getLigaRangfolge() < 16) {
							teamSteigtAbSiebenUndAchtzehnter(team);
						}
					}
					tabellenEintrag.setPlatzierung(0);
				}
			}
		}
		
	}

	public void teamIstMeisterGeworden(Team team) {
		
	}
	
	public void teamIstVizeMeisterGeworden(Team team) {
		
	}
	
	public void teamIstBeimWeltpokalDabei(Team team) {
		
	}
	
	public void teamSteigtAuf(Team team) {
		int alteLigaNummer = team.getLiga().getLigaNameTyp().getLigaRangfolge();
		int neueLigaNummer = alteLigaNummer / 2;
		
		for(LigenNamenTypen ligaNameTyp : LigenNamenTypen.values()) {
			if(ligaNameTyp.getLigaRangfolge() == neueLigaNummer) {
				team.setLiga(ligaService.findeLiga(team.getLand().getLandNameTyp().getName(), ligaNameTyp.getName()));
				aktualisiereTeam(team);
				break;
			}
		}
	}
	
	public void teamSteigtAbFuenfUndSechszehnter(Team team) {
		int alteLigaNummer = team.getLiga().getLigaNameTyp().getLigaRangfolge();
		int neueLigaNummer = alteLigaNummer * 2;
		
		for(LigenNamenTypen ligaNameTyp : LigenNamenTypen.values()) {
			if(ligaNameTyp.getLigaRangfolge() == neueLigaNummer) {
				team.setLiga(ligaService.findeLiga(team.getLand().getLandNameTyp().getName(), ligaNameTyp.getName()));
				aktualisiereTeam(team);
				break;
			}
		}
	}
	
	public void teamSteigtAbSiebenUndAchtzehnter(Team team) {
		int alteLigaNummer = team.getLiga().getLigaNameTyp().getLigaRangfolge();
		int neueLigaNummer = alteLigaNummer * 2 + 1;
		
		for(LigenNamenTypen ligaNameTyp : LigenNamenTypen.values()) {
			if(ligaNameTyp.getLigaRangfolge() == neueLigaNummer) {
				team.setLiga(ligaService.findeLiga(team.getLand().getLandNameTyp().getName(), ligaNameTyp.getName()));
				aktualisiereTeam(team);
				break;
			}
		}
	}
	
	public void aufgabenWennSaisonVorbei() {
		aendereLigenAllerAufUndAbsteiger();
	}
	
	public long berechneGehaelterEinesTeams(Team team, List<Spieler> alleSpielerEinesTeams) {
		long gehaelter = 0;
		
		for(Spieler spieler :  alleSpielerEinesTeams) {
			gehaelter = gehaelter + spieler.getGehalt();
		}
		return gehaelter;
	}

	public void sperreTeamsAufgrundZuWenigerProtage(User user) {
		// TODO Auto-generated method stub
		
	}
}
