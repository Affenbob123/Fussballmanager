package fussballmanager.mvc.transfermarkt;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.spieler.PositionenTypen;

public class SpielerSuche {
	
	private PositionenTypen position = null;
	
	private LaenderNamenTypen land = null;

	private int minimalesAlter = 13;
	
	private int maximalesAlter = 52;
	
	private double minimaleStaerke = 0.0;
		
	private double maximaleStaerke = 9999999999999999.9;
	
	private double minimalerPreis = 0.0;
	
	private double maximalerPreis = 9999999999999999.9;

	public PositionenTypen getPosition() {
		return position;
	}

	public void setPosition(PositionenTypen position) {
		this.position = position;
	}

	public LaenderNamenTypen getLand() {
		return land;
	}

	public void setLand(LaenderNamenTypen land) {
		this.land = land;
	}

	public int getMinimalesAlter() {
		return minimalesAlter;
	}

	public void setMinimalesAlter(int minimalesAlter) {
		this.minimalesAlter = minimalesAlter;
	}

	public int getMaximalesAlter() {
		return maximalesAlter;
	}

	public void setMaximalesAlter(int maximalesAlter) {
		this.maximalesAlter = maximalesAlter;
	}

	public double getMinimaleStaerke() {
		return minimaleStaerke;
	}

	public void setMinimaleStaerke(double minimaleStaerke) {
		this.minimaleStaerke = minimaleStaerke;
	}

	public double getMaximaleStaerke() {
		return maximaleStaerke;
	}

	public void setMaximaleStaerke(double maximaleStaerke) {
		this.maximaleStaerke = maximaleStaerke;
	}

	public double getMinimalerPreis() {
		return minimalerPreis;
	}

	public void setMinimalerPreis(double minimalerPreis) {
		this.minimalerPreis = minimalerPreis;
	}

	public double getMaximalerPreis() {
		return maximalerPreis;
	}

	public void setMaximalerPreis(double maximalerPreis) {
		this.maximalerPreis = maximalerPreis;
	}
	
	
}
