package fussballmanager.service.spielereignisse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import fussballmanager.service.spieler.Spieler;

@Entity
public class SpielEreignis {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private int Spielminute;
	
	@OneToOne
	private Spieler spieler;
	
	SpielEreignisTypen spielereignisTyp;
	
	public SpielEreignis(int spielminute, Spieler spieler, SpielEreignisTypen spielereignisTyp) {
		Spielminute = spielminute;
		this.spieler = spieler;
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

	public SpielEreignisTypen getSpielereignisTyp() {
		return spielereignisTyp;
	}

	public void setSpielereignisTyp(SpielEreignisTypen spielereignisTyp) {
		this.spielereignisTyp = spielereignisTyp;
	}
}
