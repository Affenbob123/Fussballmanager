package fussballmanager.service.land;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Land {
	
	@Id
	private long id;
	
	private String landName;
	
	public Land(long id, String landName) {
		this.id = id;
		this.landName = landName;
	}

	public Land() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLandName() {
		return landName;
	}

	public void setLandName(String landName) {
		this.landName = landName;
	}
}
