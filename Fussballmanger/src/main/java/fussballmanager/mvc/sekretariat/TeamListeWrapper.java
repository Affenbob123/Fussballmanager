package fussballmanager.mvc.sekretariat;

import java.util.List;

import fussballmanager.service.team.Team;

public class TeamListeWrapper {

	private List<Team> teamList;
	   
	public List<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}
}
