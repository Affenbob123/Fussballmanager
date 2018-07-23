package fussballmanager.service.saison.spieltag;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.saison.Saison;

@Entity
public class Spieltag {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;

	private int spieltagNummer;
	
	@ManyToOne
	private Saison saison;
	
	private boolean AktuellerSpieltag = false;
	
	public Spieltag(int spieltagNummer, Saison saison) {
		this.spieltagNummer = spieltagNummer;
		this.saison = saison;
	}
	
	public Spieltag() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSpieltagNummer() {
		return spieltagNummer;
	}

	public void setSpieltagNummer(int spieltagNummer) {
		this.spieltagNummer = spieltagNummer;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}

	public boolean isAktuellerSpieltag() {
		return AktuellerSpieltag;
	}

	public void setAktuellerSpieltag(boolean istAktuellerSpieltag) {
		this.AktuellerSpieltag = istAktuellerSpieltag;
	}
}
