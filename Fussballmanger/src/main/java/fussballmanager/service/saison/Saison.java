package fussballmanager.service.saison;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Saison {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private int saisonAnzahl;
	
	private final int  Spieltage = 35;
		
	public Saison(int saisonAnzahl) {
		this.saisonAnzahl = saisonAnzahl;
	}
	
	public Saison() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSaisonAnzahl() {
		return saisonAnzahl;
	}

	public void setSaisonAnzahl(int saisonAnzahl) {
		this.saisonAnzahl = saisonAnzahl;
	}

	public int getSpieltage() {
		return Spieltage;
	}
}
