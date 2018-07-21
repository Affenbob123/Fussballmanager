package fussballmanager.service.team;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
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
	
	public Team findeErstesDummyTeam() {
		for(Team team : findeAlleTeams()) {
			if(team.getUser().equals(null)) {
				return team;
			}
		}
		//TODO Land ist voll
		return null;
	}
	
	public List<Team> findeAlleTeams() {
		return teamRepository.findAll();
	}
	
	public void legeTeamAn(Team team) {
		teamRepository.save(team);
		
		spielerService.erstelleStandardSpieler(team);
	}
	
	public void aktualisiereTeam(Team team) {
		teamRepository.save(team);
	}
	
	public void loescheTeam(Team team) {
		teamRepository.delete(team);
	}
	
	public void dummyHauptteamsErstellen(Liga liga) {
		String standardName = "Dummy Team";
		
		for(int i = 0; i < liga.getGroeße(); i++) {
			legeTeamAn(new Team(standardName, null, liga));
			LOG.info("DummyHauptTeam: {} wurde in der Liga: {} angelegt.", standardName, liga.getLigaName());
		}
	}

	public void standardHauptteamfuerUserErstellen(User user) {
		Team team = findeErstesDummyTeam();
		team.setPunkte(0);
		team.setUser(user);
		aktualisiereTeam(team);
		
		LOG.info("Team: {} wurde dem User: {} zugewiesen.", team.getId(), team.getUser().getLogin());
	}
}
