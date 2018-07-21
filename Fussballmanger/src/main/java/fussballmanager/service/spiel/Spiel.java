package fussballmanager.service.spiel;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.team.Team;

@Entity
public class Spiel {
	
	@Id
	private long id;
	
	@ManyToOne
	private Team Heimmannschaft;
	
	@ManyToOne
	private Team Gastmannschaft;
	
	private int toreHeimMannschaft;
	
	private int toreGastMannschaft;
	
	private LocalDateTime spielbeginn;
	
	private String spielort;

	public Spiel(long id, Team heimmannschaft, Team gastmannschaft, int toreHeimMannschaft, int toreGastMannschaft,
			LocalDateTime spielbeginn, String spielort) {
		this.id = id;
		Heimmannschaft = heimmannschaft;
		Gastmannschaft = gastmannschaft;
		this.toreHeimMannschaft = toreHeimMannschaft;
		this.toreGastMannschaft = toreGastMannschaft;
		this.spielbeginn = spielbeginn;
		this.spielort = spielort;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Team getHeimmannschaft() {
		return Heimmannschaft;
	}

	public void setHeimmannschaft(Team heimmannschaft) {
		Heimmannschaft = heimmannschaft;
	}

	public Team getGastmannschaft() {
		return Gastmannschaft;
	}

	public void setGastmannschaft(Team gastmannschaft) {
		Gastmannschaft = gastmannschaft;
	}

	public int getToreHeimMannschaft() {
		return toreHeimMannschaft;
	}

	public void setToreHeimMannschaft(int toreHeimMannschaft) {
		this.toreHeimMannschaft = toreHeimMannschaft;
	}

	public int getToreGastMannschaft() {
		return toreGastMannschaft;
	}

	public void setToreGastMannschaft(int toreGastMannschaft) {
		this.toreGastMannschaft = toreGastMannschaft;
	}

	public LocalDateTime getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(LocalDateTime spielbeginn) {
		this.spielbeginn = spielbeginn;
	}

	public String getSpielort() {
		return spielort;
	}

	public void setSpielort(String spielort) {
		this.spielort = spielort;
	}
}
