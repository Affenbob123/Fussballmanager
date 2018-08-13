package fussballmanager.mvc.transfermarkt;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.spieler.PositionenTypen;

public class SpielerSuche {
	
	private PositionenTypen position = null;
	
	private LaenderNamenTypen land = null;

	private int minimalesAlter = 14;
	
	private int maximalesAlter = 50;
	
	private double minimaleStaerke = 0.0;
		
	private double maximaleStaerke = 9999999999999999.9;
	
	private long minimalerPreis = 0;
	
	private long maximalerPreis = 99999999999999999L;

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

	public long getMinimalerPreis() {
		return minimalerPreis;
	}

	public void setMinimalerPreis(long minimalerPreis) {
		this.minimalerPreis = minimalerPreis;
	}

	public long getMaximalerPreis() {
		return maximalerPreis;
	}

	public void setMaximalerPreis(long maximalerPreis) {
		this.maximalerPreis = maximalerPreis;
	}
	
	
}
