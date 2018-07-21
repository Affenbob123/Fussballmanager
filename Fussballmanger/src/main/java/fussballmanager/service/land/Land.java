package fussballmanager.service.land;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Land {
	
	@Id
	private LaenderNamenTypen landName;
	
	public Land(LaenderNamenTypen landName) {
		this.landName = landName;
	}

	public Land() {
		
	}

	public LaenderNamenTypen getLandName() {
		return landName;
	}

	public void setLandName(LaenderNamenTypen landName) {
		this.landName = landName;
	}
}
