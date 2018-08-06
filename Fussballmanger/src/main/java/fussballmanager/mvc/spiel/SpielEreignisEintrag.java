package fussballmanager.mvc.spiel;

import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.team.Team;

public class SpielEreignisEintrag implements Comparable<SpielEreignisEintrag> {

	private int spielminute;
	
	private String spielerName;
		
	private String spielEreignisName;
	
	private SpielEreignisTypen spielEreignisTyp;
	
	private Team team;

	public int getSpielminute() {
		return spielminute;
	}

	public void setSpielminute(int spielminute) {
		this.spielminute = spielminute;
	}

	public String getSpielerName() {
		return spielerName;
	}

	public void setSpielerName(String spielerName) {
		this.spielerName = spielerName;
	}

	public String getSpielEreignisName() {
		return spielEreignisName;
	}

	public void setSpielEreignisName(String spielEreignisName) {
		this.spielEreignisName = spielEreignisName;
	}
	
	public SpielEreignisTypen getSpielEreignisTyp() {
		return spielEreignisTyp;
	}

	public void setSpielEreignisTyp(SpielEreignisTypen spielEreignisTyp) {
		this.spielEreignisTyp = spielEreignisTyp;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String spielEreignisEintragToString() {
		String s = "";
		
		s = spielminute + "min. von " + spielerName + " vom Team: " + team.getName() + " hat " + spielEreignisName;
		return s;
	}
	
	@Override
	public int compareTo(SpielEreignisEintrag compareTo) {
		return this.spielminute - compareTo.getSpielminute();
	}
}
