package fussballmanager.service.liga;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.land.Land;

@Entity
public class Liga {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private LigenNamenTypen ligaName;
	
	private final int groeße = 18;
	
	@ManyToOne
	private Land land;
	
	public Liga(LigenNamenTypen ligaName, Land land) {
		this.ligaName = ligaName;
		this.land = land;
	}
	
	public Liga() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LigenNamenTypen getLigaName() {
		return ligaName;
	}

	public void setLigaName(LigenNamenTypen ligaName) {
		this.ligaName = ligaName;
	}

	public Land getLand() {
		return land;
	}

	public void setLand(Land land) {
		this.land = land;
	}

	public int getGroeße() {
		return groeße;
	}
}
