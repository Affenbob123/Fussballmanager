package fussballmanager.service.spielereignisse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.team.Team;

@Entity
public class SpielEreignis {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private int Spielminute;
	
	@OneToOne
	private Spieler spieler;
	
	@OneToOne
	private Team team;
	
	SpielEreignisTypen spielereignisTyp;
	
	public SpielEreignis(int spielminute, Spieler spieler, Team team , SpielEreignisTypen spielereignisTyp) {
		Spielminute = spielminute;
		this.spieler = spieler;
		this.team = team;
		this.spielereignisTyp = spielereignisTyp;
	}

	public SpielEreignis() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSpielminute() {
		return Spielminute;
	}

	public void setSpielminute(int spielminute) {
		Spielminute = spielminute;
	}

	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public SpielEreignisTypen getSpielereignisTyp() {
		return spielereignisTyp;
	}

	public void setSpielereignisTyp(SpielEreignisTypen spielereignisTyp) {
		this.spielereignisTyp = spielereignisTyp;
	}
}
