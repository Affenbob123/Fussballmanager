package fussballmanager.service.spieler.spielerzuwachs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.spieler.Spieler;

@Entity
public class SpielerZuwachs {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@OneToOne
	Saison saison;
	
	@OneToOne
	Spieltag spieltag;
	
	private double zuwachs;
	
	@ManyToOne
	Spieler spieler;
	
	public SpielerZuwachs(Saison saison, Spieltag spieltag, Spieler spieler) {
		this.saison = saison;
		this.spieltag = spieltag;
		this.spieler = spieler;
	}
	
	public SpielerZuwachs() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}

	public Spieltag getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(Spieltag spieltag) {
		this.spieltag = spieltag;
	}

	public double getZuwachs() {
		return zuwachs;
	}

	public void setZuwachs(double zuwachs) {
		this.zuwachs = zuwachs;
	}

	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}
}
