package fussballmanager.mvc.spiel;

import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.team.Team;

public class SpielEreignisEintrag implements Comparable<SpielEreignisEintrag> {
	
	private Spieler spieler;
		
	private SpielEreignis spielEreignis;
	
	private SpielEreignisTypen spielEreignisTyp;
	
	private Team team;
	
	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}

	public SpielEreignis getSpielEreignis() {
		return spielEreignis;
	}

	public void setSpielEreignis(SpielEreignis spielEreignis) {
		this.spielEreignis = spielEreignis;
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
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGETROFFEN)) {
			s = spielEreignis.getSpielminute() + "min.: " + spieler.getName() + "(" + spieler.getAufstellungsPositionsTyp().getPositionsName()
					+ ")" + " vom Team: " + team.getName() + " hat ein Tor geschossen.";
		}
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGEHALTEN)) {
			s = spielEreignis.getSpielminute() + "min.: " + spieler.getName() + "(" + spieler.getAufstellungsPositionsTyp().getPositionsName()
					+ ")" + " vom Team: " + team.getName() + " hat den Torversuch gehalten.";
		}
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.GELBEKARTE)) {
			s = spielEreignis.getSpielminute() + "min.: " + spieler.getName() + "(" + spieler.getPosition().getPositionsName()
					+ ")" + " vom Team: " + team.getName() + " hat eine gelbe Karte erhalten.";
		}
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.GELBROTEKARTE)) {
			s = spielEreignis.getSpielminute() + "min.: " + spieler.getName() + "(" + spieler.getPosition().getPositionsName()
					+ ")" + " vom Team: " + team.getName() + " hat eine gelbrote Karte erhalten.";
		}
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.ROTEKARTE)) {
			s = spielEreignis.getSpielminute() + "min.: " + spieler.getName() + "(" + spieler.getPosition().getPositionsName()
					+ ")" + " vom Team: " + team.getName() + " hat eine rote Karte erhalten.";
		}
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.VERLETZUNG)) {
			s = spielEreignis.getSpielminute() + "min.: " + spieler.getName() + "(" + spieler.getPosition().getPositionsName()
					+ ")" + " vom Team: " + team.getName() + " hat sich verletzt.";
		}
		return s;
	}
	
	@Override
	public int compareTo(SpielEreignisEintrag compareTo) {
		return this.spielEreignis.getSpielminute() - compareTo.spielEreignis.getSpielminute();
	}
}
