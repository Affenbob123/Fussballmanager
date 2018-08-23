package fussballmanager.mvc.auktionshaus;

import fussballmanager.service.team.Team;

public class AuktionshausEintragHelper {

	private Team team;

	private String beschreibung;
	
	private long startGebotPreis;
		
	private long sofortKaufPreis;
		
	private String ablaufDatum;
	
	private boolean fuerProtage = true;

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public long getStartGebotPreis() {
		return startGebotPreis;
	}

	public void setStartGebotPreis(long startGebotPreis) {
		this.startGebotPreis = startGebotPreis;
	}

	public long getSofortKaufPreis() {
		return sofortKaufPreis;
	}

	public void setSofortKaufPreis(long sofortKaufPreis) {
		this.sofortKaufPreis = sofortKaufPreis;
	}

	public String getAblaufDatum() {
		return ablaufDatum;
	}

	public void setAblaufDatum(String ablaufDatum) {
		this.ablaufDatum = ablaufDatum;
	}

	public boolean isFuerProtage() {
		return fuerProtage;
	}

	public void setFuerProtage(boolean fuerProtage) {
		this.fuerProtage = fuerProtage;
	}
}
