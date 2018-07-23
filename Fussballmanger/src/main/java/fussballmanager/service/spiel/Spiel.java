package fussballmanager.service.spiel;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.saison.spieltag.Spieltag;
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
	
	@ManyToOne
	private Spieltag spieltag;
	
	private LocalTime spielbeginn;
	
	private String spielort;

	public Spiel(Team heimmannschaft, Team gastmannschaft, LocalTime spielbeginn, Spieltag spieltag,
			String spielort) {
		this.heimmannschaft = heimmannschaft;
		this.gastmannschaft = gastmannschaft;
		this.spielbeginn = spielbeginn;
		this.spieltag = spieltag;
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

	public LocalTime getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(LocalTime spielbeginn) {
		this.spielbeginn = spielbeginn;
	}

	public Spieltag getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(Spieltag spieltag) {
		this.spieltag = spieltag;
	}

	public String getSpielort() {
		return spielort;
	}

	public void setSpielort(String spielort) {
		this.spielort = spielort;
	}
}
