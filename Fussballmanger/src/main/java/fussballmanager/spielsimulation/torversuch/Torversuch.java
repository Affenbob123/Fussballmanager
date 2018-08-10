package fussballmanager.spielsimulation.torversuch;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.team.Team;

@Entity
public class Torversuch {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	Long id;
	
	int spielminute;
	
	@OneToOne
	Spieler torschuetze;
	
	@OneToOne
	Spieler torwart;
	
	@OneToOne
	Team angreifer;
	
	@OneToOne
	Team verteidiger;
	
	@OneToOne
	Spiel spiel;
	
	TorversuchTypen richtung;
	
	TorversuchTypen richtungVomUser;
	
	LocalTime erstellZeit;
	
	public Torversuch() {
		
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

	public TorversuchTypen getRichtung() {
		return richtung;
	}

	public void setRichtung(TorversuchTypen richtung) {
		this.richtung = richtung;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel spiel) {
		this.spiel = spiel;
	}

	public TorversuchTypen getRichtungVomUser() {
		return richtungVomUser;
	}

	public void setRichtungVomUser(TorversuchTypen richtungVomUser) {
		this.richtungVomUser = richtungVomUser;
	}

	public LocalTime getErstellZeit() {
		return erstellZeit;
	}

	public void setErstellZeit(LocalTime erstellZeit) {
		this.erstellZeit = erstellZeit;
	}
}
