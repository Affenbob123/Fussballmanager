package fussballmanager.service.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import fussballmanager.service.land.Land;
import fussballmanager.service.team.Team;

@Entity
public class User implements Comparable<User> {
	
	@Id
	@NotNull
	private String login;
	
	@NotNull
	private String password;
	
	@NotNull
	private String username;
	
	private String email;
	
	@OneToMany
	private Set<Land> laender = new HashSet<>();
	
	@OneToMany
	private Set<Team> teams = new HashSet<>();
	
	private boolean isAdmin;
	
	String profilBild;
	
	public User(@NotNull String login, @NotNull String password, boolean isAdmin, @NotNull String username, String email) {
		this.login = login;
		this.password = password;
		this.isAdmin = isAdmin;
		this.username = username;
	}

	public User() {
		// Do not remove!
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String anzeigeName) {
		this.username = anzeigeName;
	}

	public String getProfilBild() {
		return profilBild;
	}

	public void setProfilBild(String profilBild) {
		this.profilBild = profilBild;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Land> getLaender() {
		return laender;
	}

	public void setLaender(Set<Land> laender) {
		this.laender = laender;
	}
	
	public void addLaender(Land land) {
		getLaender().add(land);
	}

	public Set<Team> getTeams() {
		return teams;
	}

	public void setTeams(Set<Team> teams) {
		this.teams = teams;
	}
	
	public void addTeams(Team team) {
		getTeams().add(team);
	}

	@Override
	public int compareTo(User compareTo) {
		String compareString = ((User)compareTo).getUsername();
		return this.username.compareToIgnoreCase(compareString);
	}
}

