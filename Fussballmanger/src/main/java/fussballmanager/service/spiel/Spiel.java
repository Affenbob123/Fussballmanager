package fussballmanager.service.spiel;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.team.Team;

@Entity
public class Spiel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@ManyToOne
	private Team heimmannschaft;
	
	@ManyToOne
	private Team gastmannschaft;
	
	private int toreHeimMannschaft;
	
	private int toreGastMannschaft;
	
	private LocalDateTime spielbeginn;
	
	private String spielort;

	public Spiel(Team heimmannschaft, Team gastmannschaft, int toreHeimMannschaft, int toreGastMannschaft,
			LocalDateTime spielbeginn, String spielort) {
		this.heimmannschaft = heimmannschaft;
		this.gastmannschaft = gastmannschaft;
		this.toreHeimMannschaft = toreHeimMannschaft;
		this.toreGastMannschaft = toreGastMannschaft;
		this.spielbeginn = spielbeginn;
		this.spielort = spielort;
	}
	
	public Spiel() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Team getHeimmannschaft() {
		return heimmannschaft;
	}

	public void setHeimmannschaft(Team heimmannschaft) {
		this.heimmannschaft = heimmannschaft;
	}

	public Team getGastmannschaft() {
		return gastmannschaft;
	}

	public void setGastmannschaft(Team gastmannschaft) {
		this.gastmannschaft = gastmannschaft;
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
