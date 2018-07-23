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
	
	private int saisonNummer;
	
	private final int  Spieltage = 35;
	
	private boolean aktuelleSaison = false;
		
	public Saison(int saisonNummer) {
		this.saisonNummer = saisonNummer;
	}
	
	public Saison() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSaisonNummer() {
		return saisonNummer;
	}

	public void setSaisonNummer(int saisonAnzahl) {
		this.saisonNummer = saisonAnzahl;
	}

	public int getSpieltage() {
		return Spieltage;
	}

	public boolean isAktuelleSaison() {
		return aktuelleSaison;
	}

	public void setAktuelleSaison(boolean aktuelleSaison) {
		this.aktuelleSaison = aktuelleSaison;
	}
}
