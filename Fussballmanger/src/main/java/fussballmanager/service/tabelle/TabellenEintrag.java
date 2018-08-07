package fussballmanager.service.tabelle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.mvc.liga.LigaEintrag;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.team.Team;

@Entity
public class TabellenEintrag implements Comparable<TabellenEintrag>{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@ManyToOne
	private Liga liga;
	
	@ManyToOne
	private Saison saison;
	
	private int platzierung;
	
	@ManyToOne
	private Team team;
	
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
	
	public TabellenEintrag(Liga liga, Saison saison, Team team) {
		this.liga = liga;
		this.saison = saison;
		this.team = team;
		this.siege = 0;
		this.unentschieden = 0;
		this.niederlagen = 0;
		this.spiele = 0;
		this.tore = 0;
		this.gegentore = 0;
		this.torDifferenz = 0;
		this.punkte = 0;
		this.gelbeKarten = 0;
		this.gelbRoteKarten = 0;
		this.roteKarten = 0;
	}
	
	public TabellenEintrag() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}

	public int getPlatzierung() {
		return platzierung;
	}

	public void setPlatzierung(int platzierung) {
		this.platzierung = platzierung;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
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

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int punkte) {
		this.punkte = punkte;
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
	public int compareTo(TabellenEintrag compareTo) {
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
