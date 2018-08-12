package fussballmanager.mvc.sekretariat.statistik;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.spieler.PositionenTypen;

public class StatistikFormular {

	private PositionenTypen position = null;
	
	private LaenderNamenTypen landNameTyp = null;
	
	private int alter = - 1;
	
	private SortierTypen sortierTyp = SortierTypen.STAERKE;
	
	private int seitenNummer = 0;

	public PositionenTypen getPosition() {
		return position;
	}

	public void setPosition(PositionenTypen position) {
		this.position = position;
	}

	public LaenderNamenTypen getLandNameTyp() {
		return landNameTyp;
	}

	public void setLandNameTyp(LaenderNamenTypen landNameTyp) {
		this.landNameTyp = landNameTyp;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public SortierTypen getSortierTyp() {
		return sortierTyp;
	}

	public void setSortierTyp(SortierTypen sortierTyp) {
		this.sortierTyp = sortierTyp;
	}

	public int getSeitenNummer() {
		return seitenNummer;
	}

	public void setSeitenNummer(int seitenNummer) {
		this.seitenNummer = seitenNummer;
	}
}
