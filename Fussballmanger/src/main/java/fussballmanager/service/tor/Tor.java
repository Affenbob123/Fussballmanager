package fussballmanager.service.tor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spieler.Spieler;

@Entity
public class Tor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;

	@OneToOne
	private Spieler torschuetze;
	
	private int spielminute;
	
	@ManyToOne
	private Spiel spiel;
	
	public Tor(Spieler torschuetze, int spielminute, Spiel spiel) {
		this.torschuetze = torschuetze;
		this.spielminute = spielminute;
		this.spiel = spiel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Spieler getTorschuetze() {
		return torschuetze;
	}

	public void setTorschuetze(Spieler torschuetze) {
		this.torschuetze = torschuetze;
	}

	public int getSpielminute() {
		return spielminute;
	}

	public void setSpielminute(int spielminute) {
		this.spielminute = spielminute;
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel spiel) {
		this.spiel = spiel;
	}
}
