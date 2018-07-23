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

	private int derSpieltag;
	
	@ManyToOne
	private Saison saison;
	
	public Spieltag(int derSpieltag, Saison saison) {
		this.derSpieltag = derSpieltag;
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

	public int getDerSpieltag() {
		return derSpieltag;
	}

	public void setDerSpieltag(int derSpieltag) {
		this.derSpieltag = derSpieltag;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}
}
