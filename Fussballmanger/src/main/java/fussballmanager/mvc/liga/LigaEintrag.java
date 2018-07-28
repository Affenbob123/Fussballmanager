package fussballmanager.mvc.liga;

import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;

public class LigaEintrag implements Comparable<LigaEintrag> {
	
	private long id;
	
	private Land land;
	
	private Liga liga;
	
	private int platzierung;
	
	private String teamWappen;
	
	private String teamName;
	
	private int siege;
	
	private int unentschieden;
	
	private int niederlagen;
	
	private int spiele = siege + unentschieden + niederlagen;
	
	private int tore;
	
	private int gegentore;
	
	private int torDifferenz = tore - gegentore;
	
	private int punkte;
	
	private int gelbeKarten;
	
	private int gelbRoteKarten;
	
	private int roteKarten;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Land getLand() {
		return land;
	}

	public void setLand(Land land) {
		this.land = land;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}

	public int getPlatzierung() {
		return platzierung;
	}

	public void setPlatzierung(int platzierung) {
		this.platzierung = platzierung;
	}

	public int getTore() {
		return tore;
	}

	public void setTore(int tore) {
		this.tore = tore;
	}

	public int getGegentore() {
		return gegentore;
	}

	public void setGegentore(int gegentore) {
		this.gegentore = gegentore;
	}

	public int getTorDifferenz() {
		return torDifferenz;
	}

	public void setTorDifferenz(int torDifferenz) {
		this.torDifferenz = torDifferenz;
	}

	public int getSiege() {
		return siege;
	}

	public void setSiege(int siege) {
		this.siege = siege;
	}

	public int getUnentschieden() {
		return unentschieden;
	}

	public void setUnentschieden(int unentschieden) {
		this.unentschieden = unentschieden;
	}

	public int getNiederlagen() {
		return niederlagen;
	}

	public void setNiederlagen(int niederlagen) {
		this.niederlagen = niederlagen;
	}

	public int getSpiele() {
		return spiele;
	}

	public void setSpiele(int spiele) {
		this.spiele = spiele;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamWappen() {
		return teamWappen;
	}

	public void setTeamWappen(String teamWappen) {
		this.teamWappen = teamWappen;
	}

	public int getGelbeKarten() {
		return gelbeKarten;
	}

	public void setGelbeKarten(int gelbeKarten) {
		this.gelbeKarten = gelbeKarten;
	}

	public int getGelbRoteKarten() {
		return gelbRoteKarten;
	}

	public void setGelbRoteKarten(int gelbRoteKarten) {
		this.gelbRoteKarten = gelbRoteKarten;
	}

	public int getRoteKarten() {
		return roteKarten;
	}

	public void setRoteKarten(int roteKarten) {
		this.roteKarten = roteKarten;
	}

	@Override
	public int compareTo(LigaEintrag compareTo) {
		if(this.punkte - compareTo.getPunkte() == 0) {
			if(this.torDifferenz - compareTo.getTorDifferenz() == 0) {
				if(this.tore - compareTo.getTore() == 0) {
					if(this.roteKarten - compareTo.getRoteKarten() == 0) {
						if(this.gelbRoteKarten - compareTo.getGelbRoteKarten() == 0) {
							if(this.gelbeKarten - compareTo.getGelbeKarten() == 0) {
								return 1;
							} else {
								return compareTo.getGelbeKarten() - this.gelbeKarten;
							}
						} else {
							return compareTo.getGelbRoteKarten() - this.gelbRoteKarten;
						}
					} else {
						return compareTo.getRoteKarten() - this.roteKarten;
					}
				} else {
					return compareTo.getTore() - this.tore;
				}
			} else {
				return compareTo.getTorDifferenz() - this.torDifferenz;
			}
		} else {
			return compareTo.getPunkte() - this.punkte;
		}
	}
}
