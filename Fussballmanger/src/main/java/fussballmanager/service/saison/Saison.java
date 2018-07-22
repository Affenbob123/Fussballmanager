package fussballmanager.service.saison;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Saison {
	
	@Id
	private long id;
	
	private final int Spieltage = 35;
	
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
}
