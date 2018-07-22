package fussballmanager.service.saison;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Saison {
	
	@Id
	private long id;
	
	private final int Spieltage = 35;
	
	private int aktuellerSpieltag;
	
	public Saison() {
		this.aktuellerSpieltag = 1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSpieltage() {
		return Spieltage;
	}

	public int getAktuellerSpieltag() {
		return aktuellerSpieltag;
	}

	public void setAktuellerSpieltag(int aktuellerSpieltag) {
		this.aktuellerSpieltag = aktuellerSpieltag;
	}
}
