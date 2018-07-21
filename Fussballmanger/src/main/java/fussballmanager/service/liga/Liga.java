package fussballmanager.service.liga;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import fussballmanager.service.land.Land;
import fussballmanager.service.team.Team;

@Entity
public class Liga {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private LigenNamenTypen ligaName;
	
	@ManyToOne
	private Land land;
	
	@OneToMany
	private Set<Team> teams = new HashSet<>();

	public Liga(LigenNamenTypen ligaName, Land land) {
		this.ligaName = ligaName;
		this.land = land;
	}
	
	public Liga() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LigenNamenTypen getLigaName() {
		return ligaName;
	}

	public void setLigaName(LigenNamenTypen ligaName) {
		this.ligaName = ligaName;
	}

	public Land getLand() {
		return land;
	}

	public void setLand(Land land) {
		this.land = land;
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
