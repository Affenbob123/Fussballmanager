package fussballmanager.mvc.freundschaftspiel;

import java.util.List;

import fussballmanager.service.benachrichtigung.FreunschaftsspieleAnfrageTypen;
import fussballmanager.service.team.Team;

public class FreundschaftsspieleWrapper {
	
	FreunschaftsspieleAnfrageTypen freundschaftsspielAnfrageTyp;
	
	Team absender;
	
	List<Team> empfaenger;

	public FreunschaftsspieleAnfrageTypen getFreundschaftsspielAnfrageTyp() {
		return freundschaftsspielAnfrageTyp;
	}

	public void setFreundschaftsspielAnfrageTyp(FreunschaftsspieleAnfrageTypen freundschaftsspielAnfrageTyp) {
		this.freundschaftsspielAnfrageTyp = freundschaftsspielAnfrageTyp;
	}

	public Team getAbsender() {
		return absender;
	}

	public void setAbsender(Team absender) {
		this.absender = absender;
	}

	public List<Team> getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(List<Team> empfaenger) {
		this.empfaenger = empfaenger;
	}
}
