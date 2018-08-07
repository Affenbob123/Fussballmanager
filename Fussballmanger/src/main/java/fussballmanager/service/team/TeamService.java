package fussballmanager.service.team;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.Saison;
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
import fussballmanager.service.tabelle.TabellenEintragService;
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
	
	public void dummyHauptteamsErstellen(Liga liga) {
		for(int i = 0; i < liga.getGroeße(); i++) {
			StringBuilder sb = new StringBuilder("Dummy Team");
			sb.append (i);
			String standardName = sb.toString();
			
			legeTeamAn(new Team(liga.getLand(), standardName, null, liga));
		}
	}

	public void standardHauptteamfuerUserErstellen(User user) {
		Team team = findeErstesDummyTeam(user.getLand());
		team.setUser(user);
		aktualisiereTeam(team);
		user.setAktuellesTeam(findeAlleTeamsEinesUsers(user).get(0));
		
		LOG.info("Team: {} wurde dem User: {} zugewiesen.", team.getId(), team.getUser().getLogin());
		LOG.info("AktuellesTeam: {}", team.getId(), team.getUser().getLogin());
	}
	
	public void aendereEinsatzEinesTeams(Team team) {
		aktualisiereTeam(team);
		List<Spieler> alleSpielerDesTeams = spielerService.findeAlleSpielerEinesTeams(team);
		
		for(Spieler spieler : alleSpielerDesTeams) {
			double spielerStaerkeAenderung = team.getEinsatzTyp().getStaerkenFaktor();
			spielerService.kompletteStaerkeAendern(spieler, spielerStaerkeAenderung);
		}
	}

	//TODO Wenn kein SPieler mit der Position vorhanden ist dann spielen zu wenige
	public void aenderFormationEinesTeams(Team team) {
		aktualisiereTeam(team);
		FormationsTypen formationsTypDesTeams = team.getFormationsTyp();
		AufstellungsPositionsTypen aufstellung[] = formationsTypDesTeams.getAufstellungsPositionsTypen();
		Collection<AufstellungsPositionsTypen> fehlendePositionen = new ArrayList<AufstellungsPositionsTypen>(Arrays.asList(aufstellung));
	
		staerksteFormationEinesTeams(team);
	}
	
	//TODO fml
	public void staerksteFormationEinesTeams(Team team) {
		FormationsTypen formationsTypDesTeams = team.getFormationsTyp();
		AufstellungsPositionsTypen aufstellung[] = formationsTypDesTeams.getAufstellungsPositionsTypen();
		List<Spieler> spielerDesTeams = spielerService.spielerEinesTeamsSortiertNachStaerke(team);
		Collection<AufstellungsPositionsTypen> fehlendePositionen = new ArrayList<AufstellungsPositionsTypen>(Arrays.asList(aufstellung));
				
		for(Spieler spieler : spielerDesTeams) {
			spieler.setAufstellungsPositionsTyp(AufstellungsPositionsTypen.ERSATZ);
			for(AufstellungsPositionsTypen a : aufstellung) {
				if(spieler.getPosition().getPositionsName().equals(a.getPositionsName()) && fehlendePositionen.contains(a)) {
					spieler.setAufstellungsPositionsTyp(a);
					fehlendePositionen.remove(a);
					LOG.info("{}", fehlendePositionen);
					spielerService.aktualisiereSpieler(spieler);
					break;
				}
			}
		}
		
		if(fehlendePositionen.size() > 0) {
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(a.getRollenTyp())) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(a.getRollenTyp())) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(a.getRollenTyp())) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.MITTELFELD)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.ANGREIFER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.ANGREIFER)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.MITTELFELD)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.ANGREIFER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.TORWART)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.MITTELFELD)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.TORWART)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
						
						if(a.getRollenTyp().equals(RollenTypen.VERTEIDIGER)) {
							if(spieler.getPosition().getRollenTyp().equals(RollenTypen.TORWART)) {
								spieler.setAufstellungsPositionsTyp(a);
								fehlendePositionen.remove(a);
								spielerDesTeams.remove(spieler);
								LOG.info("{}", fehlendePositionen);
								spielerService.aktualisiereSpieler(spieler);
								break;
							}
						}
					}
				}
			}
			
			for(AufstellungsPositionsTypen a : aufstellung) {
				for(Spieler spieler: spielerDesTeams) {
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ)) && fehlendePositionen.contains(a)) {
						if(a.getRollenTyp().equals(RollenTypen.TORWART)) {
							spieler.setAufstellungsPositionsTyp(a);
							fehlendePositionen.remove(a);
							spielerDesTeams.remove(spieler);
							LOG.info("{}", fehlendePositionen);
							spielerService.aktualisiereSpieler(spieler);
							break;
						}
					}
				}
			}
		}
	}
	
	public int siegeEinesTeamsInEinerSaison(Team team, Saison saison) {
		int siege = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
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
		}
		return siege;
	}

	public int unentschiedenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int unentschieden = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		int aktuellerSpieltagNummer = spieltagService.findeAktuellenSpieltag().getSpieltagNummer();
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
				if(spiel.getSpieltag().getSpieltagNummer() <= aktuellerSpieltagNummer) {
					if(spiel.getSpielTyp().getSpielBeginn().isBefore(LocalTime.now(ZoneId.of("Europe/Berlin")))) {
						if(spiel.getToreHeimmannschaft() == spiel.getToreGastmannschaft()) {
							unentschieden++;
						}
					}

				}
			}
		}
		return unentschieden;
	}
	
	public int niederlagenEinesTeamsInEinerSaison(Team team, Saison saison) {
		int niederlagen = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
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
		}
		return niederlagen;
	}
	
	public int toreEinesTeamsInEinerSaison(Team team, Saison saison) {
		int tore = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
				if(team.equals(spiel.getHeimmannschaft())) {
					tore = tore + spiel.getToreHeimmannschaft();
				} else {
					tore = tore + spiel.getToreGastmannschaft();
				}	
			}
		}
		return tore;
	}
	
	public int gegenToreEinesTeamsInEinerSaison(Team team, Saison saison) {
		int gegenTore = 0;
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
				if(team.equals(spiel.getHeimmannschaft())) {
					gegenTore = gegenTore + spiel.getToreGastmannschaft();
				} else {
					gegenTore = gegenTore + spiel.getToreHeimmannschaft();
				}	
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
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
				for(SpielEreignis spielEreignis :spiel.getSpielEreignisse()) {
					if(spielEreignis.getVerteidiger().equals(team)) {
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
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
				for(SpielEreignis spielEreignis :spiel.getSpielEreignisse()) {
					if(spielEreignis.getVerteidiger().equals(team)) {
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
		List<Spiel> alleSpieleEinesTeams = spielService.findeAlleAbgeschlossenenSpieleEinesTeamsNachSpielTypUndSaison(team, SpieleTypen.LIGASPIEL, saison);
		
		for(Spiel spiel : alleSpieleEinesTeams) {
			if(spiel.getSaison().equals(saison)) {
				for(SpielEreignis spielEreignis :spiel.getSpielEreignisse()) {
					if(spielEreignis.getVerteidiger().equals(team)) {
						if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.ROTEKARTE)) {
							roteKarten++;
						}
					}

				}
			}
		}
		return roteKarten;
	}
}
