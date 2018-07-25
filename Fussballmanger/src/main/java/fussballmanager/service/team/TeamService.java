package fussballmanager.service.team;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
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
			spieler.getStaerke().setDribbeln(spieler.getReinStaerke().getDribbeln() * team.getEinsatzTyp().getStaerkenFaktor());
			spieler.getStaerke().setGeschwindigkeit(spieler.getReinStaerke().getGeschwindigkeit() * team.getEinsatzTyp().getStaerkenFaktor());
			spieler.getStaerke().setPassen(spieler.getReinStaerke().getPassen() * team.getEinsatzTyp().getStaerkenFaktor());
			spieler.getStaerke().setPhysis(spieler.getReinStaerke().getPhysis() * team.getEinsatzTyp().getStaerkenFaktor());
			spieler.getStaerke().setSchießen(spieler.getReinStaerke().getSchießen() * team.getEinsatzTyp().getStaerkenFaktor());
			spieler.getStaerke().setVerteidigen(spieler.getReinStaerke().getVerteidigen() * team.getEinsatzTyp().getStaerkenFaktor());
			spieler.getStaerke().setDurchschnittsStaerke(spieler.getReinStaerke().getDurchschnittsStaerke() * team.getEinsatzTyp().getStaerkenFaktor());
		}
	}
}
