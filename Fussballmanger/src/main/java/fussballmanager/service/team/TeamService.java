package fussballmanager.service.team;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.team.Team;

@Service
@Transactional
public class TeamService {
	
	@Autowired
	TeamRepository teamRepository;
	
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
	
	public List<Team> findeAlleTeams() {
		return teamRepository.findAll();
	}
	
	public void legeTeamAn(Team team) {
		teamRepository.save(team);
	}
	
	public void aktualisiereTeam(Team team) {
		teamRepository.save(team);
	}
	
	public void loescheTeam(Team team) {
		teamRepository.delete(team);
	}
}
