package fussballmanager.mvc.spiel;

public class SpielEreignisEintrag implements Comparable<SpielEreignisEintrag>{

	private int spielminute;
	
	private String spielerName;
	
	private String teamName;
	
	private String spielEreignisName;

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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getSpielEreignisName() {
		return spielEreignisName;
	}

	public void setSpielEreignisName(String spielEreignisName) {
		this.spielEreignisName = spielEreignisName;
	}
	
	public String spielEreignisEintragToString() {
		String s = "";
		
		s = spielminute + "min. von " + spielerName + " vom Team: " + teamName + " hat " + spielEreignisName;
		return s;
	}
	@Override
	public int compareTo(SpielEreignisEintrag compareTo) {
		return this.spielminute - compareTo.getSpielminute();
	}
}
