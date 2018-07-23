package fussballmanager.service.torversuch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spieler.Spieler;

@Entity
public class Torversuch {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;

	@OneToOne
	private Spieler spieler;
	
	private int spielminute;
	
	@ManyToOne
	private Spiel spiel;
	
	private TorVersuchTypen torVersuchTyp;
	
	public Torversuch(Spieler spieler, int spielminute, Spiel spiel, TorVersuchTypen torVersuchTyp) {
		this.spieler = spieler;
		this.spielminute = spielminute;
		this.spiel = spiel;
		this.torVersuchTyp = torVersuchTyp;
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

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel spiel) {
		this.spiel = spiel;
	}

	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}

	public TorVersuchTypen getTorVersuchTyp() {
		return torVersuchTyp;
	}

	public void setTorVersuchTyp(TorVersuchTypen torVersuchTyp) {
		this.torVersuchTyp = torVersuchTyp;
	}
}
