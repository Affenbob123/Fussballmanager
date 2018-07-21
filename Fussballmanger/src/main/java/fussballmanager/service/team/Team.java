package fussballmanager.service.team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.user.User;


@Entity
public class Team implements Comparable<Team>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private String name;
		
	private double geld;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Liga liga;
		
	private int punkte;
	
	private int maximaleSpielerAnzahl;
	
	private int aktuelleSpielerAnzahl;
	
	public Team(String name, User user, Liga liga) {
		this.name = name;
		this.geld = 500000.0;
		this.user = user;
		this.liga = liga;
		this.punkte = 0;
		this.maximaleSpielerAnzahl = 43;
		this.maximaleSpielerAnzahl = 0;
	}
	
	public Team() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGeld() {
		return geld;
	}

	public void setGeld(double geld) {
		this.geld = geld;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public int getMaximaleSpielerAnzahl() {
		return maximaleSpielerAnzahl;
	}

	public void setMaximaleSpielerAnzahl(int maximaleSpielerAnzahl) {
		this.maximaleSpielerAnzahl = maximaleSpielerAnzahl;
	}

	public int getAktuelleSpielerAnzahl() {
		return aktuelleSpielerAnzahl;
	}

	public void setAktuelleSpielerAnzahl(int aktuelleSpielerAnzahl) {
		this.aktuelleSpielerAnzahl = aktuelleSpielerAnzahl;
	}

	@Override
	public int compareTo(Team arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
