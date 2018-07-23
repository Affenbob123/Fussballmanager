package fussballmanager.service.spielereignisse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spieler.Spieler;

@Entity
public class SpielEreignis {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	int Spielminute;
	
	@OneToOne
	private Spieler spieler;
	
	@ManyToOne
	private Spiel spiel;
	
	SpielEreignisTypen spielereignisTyp;
	
	public SpielEreignis(int spielminute, Spieler spieler, Spiel spiel, SpielEreignisTypen spielereignisTyp) {
		Spielminute = spielminute;
		this.spieler = spieler;
		this.spiel = spiel;
		this.spielereignisTyp = spielereignisTyp;
	}

	public SpielEreignis() {

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}

	public Spiel getSpiel() {
		return spiel;
	}

	public void setSpiel(Spiel spiel) {
		this.spiel = spiel;
	}

	public SpielEreignisTypen getSpielereignisTyp() {
		return spielereignisTyp;
	}

	public void setSpielereignisTyp(SpielEreignisTypen spielereignisTyp) {
		this.spielereignisTyp = spielereignisTyp;
	}
}
