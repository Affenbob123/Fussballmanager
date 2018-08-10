package fussballmanager.service.spiel.spielereignisse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.team.Team;

@Entity
public class SpielEreignis {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	private int spielminute;
	
	@OneToOne
	private Spieler torschuetze;
	
	@OneToOne
	private Spieler torwart;
	
	@OneToOne
	private Team angreifer;
	
	@OneToOne
	private Team verteidiger;
	
	@OneToOne
	Spieler spieler;
	
	@OneToOne
	Team team;
	
	SpielEreignisTypen spielereignisTyp;
	
	public SpielEreignis() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSpielminute() {
		return spielminute;
	}

	public void setSpielminute(int spielminute) {
		this.spielminute = spielminute;
	}

	public Spieler getTorschuetze() {
		return torschuetze;
	}

	public void setTorschuetze(Spieler torschuetze) {
		this.torschuetze = torschuetze;
	}

	public Spieler getTorwart() {
		return torwart;
	}

	public void setTorwart(Spieler torwart) {
		this.torwart = torwart;
	}

	public Team getAngreifer() {
		return angreifer;
	}

	public void setAngreifer(Team angreifer) {
		this.angreifer = angreifer;
	}

	public Team getVerteidiger() {
		return verteidiger;
	}

	public void setVerteidiger(Team verteidiger) {
		this.verteidiger = verteidiger;
	}

	public SpielEreignisTypen getSpielereignisTyp() {
		return spielereignisTyp;
	}

	public void setSpielereignisTyp(SpielEreignisTypen spielereignisTyp) {
		this.spielereignisTyp = spielereignisTyp;
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
}
