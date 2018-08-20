package fussballmanager.service.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
	
	@OneToOne
	private Land land;
	
	@OneToOne
	private Team aktuellesTeam;
	
	private boolean isAdmin;
	
	private String profilBild;
	
	private int protage;
	
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

	public Land getLand() {
		return land;
	}

	public void setLand(Land land) {
		this.land = land;
	}

	public Team getAktuellesTeam() {
		return aktuellesTeam;
	}

	public void setAktuellesTeam(Team aktuellesTeam) {
		this.aktuellesTeam = aktuellesTeam;
	}

	public int getProtage() {
		return protage;
	}

	public void setProtage(int protage) {
		this.protage = protage;
	}

	@Override
	public int compareTo(User compareTo) {
		String compareString = ((User)compareTo).getUsername();
		return this.username.compareTo(compareString);
	}
}

