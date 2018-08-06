package fussballmanager.mvc.spiel;

import java.time.LocalTime;

import fussballmanager.service.team.Team;

public class SpielEintrag {
	
	private long id;
	
	private int spieltag;
	
	private LocalTime spielbeginn;
	
	private Team heimmannschaft;
	
	private Team gastmannschaft;
	
	private int toreHeimmannschaft;
	
	private int toreGastmannschaft;
	
	private int toreHeimmannschaftHalbzeit;
	
	private int toreGastmannschaftHalbzeit;
	
	private double staerkeHeimmannschaft;
	
	private double staerkeGastmannschaft;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(int spieltag) {
		this.spieltag = spieltag;
	}

	public LocalTime getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(LocalTime spielbeginn) {
		this.spielbeginn = spielbeginn;
	}

	public Team getHeimmannschaft() {
		return heimmannschaft;
	}

	public void setHeimmannschaft(Team heimmannschaft) {
		this.heimmannschaft = heimmannschaft;
	}

	public Team getGastmannschaft() {
		return gastmannschaft;
	}

	public void setGastmannschaft(Team gastmannschaft) {
		this.gastmannschaft = gastmannschaft;
	}

	public int getToreHeimmannschaft() {
		return toreHeimmannschaft;
	}

	public void setToreHeimmannschaft(int toreHeimmannschaft) {
		this.toreHeimmannschaft = toreHeimmannschaft;
	}

	public int getToreGastmannschaft() {
		return toreGastmannschaft;
	}

	public void setToreGastmannschaft(int toreGastmannschaft) {
		this.toreGastmannschaft = toreGastmannschaft;
	}

	public int getToreHeimmannschaftHalbzeit() {
		return toreHeimmannschaftHalbzeit;
	}

	public void setToreHeimmannschaftHalbzeit(int toreHeimmannschaftHalbzeit) {
		this.toreHeimmannschaftHalbzeit = toreHeimmannschaftHalbzeit;
	}

	public int getToreGastmannschaftHalbzeit() {
		return toreGastmannschaftHalbzeit;
	}

	public void setToreGastmannschaftHalbzeit(int toreGastmannschaftHalbzeit) {
		this.toreGastmannschaftHalbzeit = toreGastmannschaftHalbzeit;
	}

	public double getStaerkeHeimmannschaft() {
		return staerkeHeimmannschaft;
	}

	public void setStaerkeHeimmannschaft(double staerkeHeimmannschaft) {
		this.staerkeHeimmannschaft = staerkeHeimmannschaft;
	}

	public double getStaerkeGastmannschaft() {
		return staerkeGastmannschaft;
	}

	public void setStaerkeGastmannschaft(double staerkeGastmannschaft) {
		this.staerkeGastmannschaft = staerkeGastmannschaft;
	}
	
	public String spielbeginnToString() {
		String s = "";
		
		s = spielbeginn.toString();
		return s;
	}
	
	public String spielErgebnisToString() {
		String s = "";
		
		if(toreHeimmannschaft == -1) {
			s = "--:--";
		} else {
			s = toreHeimmannschaft + ":" + toreGastmannschaft;
		}
		
		return s;
	}
	
	public String spielErgebnisHalbzeitToString() {
		String s = "";
		
		if(toreHeimmannschaftHalbzeit == -1) {
			s = "(--:--)";
		} else {
			s = "("+ toreHeimmannschaftHalbzeit + ":" + toreGastmannschaftHalbzeit +")";
		}
		return s;
	}
	
	public String heimmannschaftUndStaerkeToString() {
		String s = "";
		
		s = heimmannschaft.getName() + " (" + staerkeHeimmannschaft + ")";
		return s;
	}
	
	public String gastmannschaftUndStaerkeToString() {
		String s = "";
		
		s = gastmannschaft.getName() + " (" + staerkeGastmannschaft + ")";
		return s;
	}
}
