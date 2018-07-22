package fussballmanager.service.land;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Land {
	
	@Id
	private LaenderNamenTypen landNameTyp;
	
	public Land(LaenderNamenTypen landName) {
		this.landNameTyp = landName;
	}

	public Land() {
		
	}

	public LaenderNamenTypen getLandNameTyp() {
		return landNameTyp;
	}

	public void setLandNameTyp(LaenderNamenTypen landName) {
		this.landNameTyp = landName;
	}
}
