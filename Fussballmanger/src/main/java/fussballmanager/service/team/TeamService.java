package fussballmanager.service.team;

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
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.PositionenTypen;
import fussballmanager.service.spieler.RollenTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
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
	
	public Team findeTeam(Long id) {
		return teamRepository.getOne(id);
	}
	
	public Team findeTeamNachTeamName(String teamname) {
		for(Team team : findeAlleTeams()) {
			if(team.getName().equals(teamname)) {
				return team;
			}
		}
		return null;
	}
	
	public Team findeErstesDummyTeam(Land land) {
		for(Team team : findeAlleTeams()) {
			if(team.getLand().equals(land)) {
				if(team.getUser() == null) {
					return team;
				}
			}
		}
		//TODO Land ist voll
		return null;
	}
	
	public List<Team> findeAlleTeams() {
		return teamRepository.findAll();
	}
	
	public List<Team> findeAlleTeamsEinesUsers(User aktuellerUser) {
		List<Team> alleTeamsEinesUsers =  new ArrayList<>();
		
		for(Team team : findeAlleTeams()) {
			if(team.getUser() != null) {
				if(team.getUser().equals(aktuellerUser)) {
					alleTeamsEinesUsers.add(team);
				}
			}
		}
		return alleTeamsEinesUsers;
	}
	
	public List<Team> findeAlleTeamsEinerLiga(Liga liga) {		
		List<Team> alleTeamsEinerLiga =  new ArrayList<>();
		
		for(Team team : findeAlleTeams()) {
			if(team.getLiga().equals(liga)) {
				alleTeamsEinerLiga.add(team);
			}
		}
		return alleTeamsEinerLiga;
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
			//LOG.info("DummyHauptTeam: {} wurde in der Liga: {} angelegt.", standardName, liga.getLigaNameTyp());
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
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
							spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)) && fehlendePositionen.contains(a)) {
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
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
							spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)) && fehlendePositionen.contains(a)) {
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
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
							spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)) && fehlendePositionen.contains(a)) {
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
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
							spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)) && fehlendePositionen.contains(a)) {
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
					if((spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ERSATZ) || 
							spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRANSFERMARKT)) && fehlendePositionen.contains(a)) {
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
}
