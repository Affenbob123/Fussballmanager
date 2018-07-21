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
	
	private int Spieltage;

	public Saison(int spieltage) {
		Spieltage = spieltage;
	}
	
	public Saison() {
		
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

	public void setSpieltage(int spieltage) {
		Spieltage = spieltage;
	}
}
