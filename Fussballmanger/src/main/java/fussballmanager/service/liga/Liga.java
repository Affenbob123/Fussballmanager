package fussballmanager.service.liga;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import fussballmanager.service.team.Team;

@Entity
public class Liga {
	
	@Id
	private long id;
	
	@OneToMany
	private Set<Team> teams = new HashSet<>();

	public Liga(long id, Set<Team> teams) {
		this.id = id;
		this.teams = teams;
	}
	
	public Liga() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}
	
	public void addTeam(Team team) {
		getTeams().add(team);
	}
}
