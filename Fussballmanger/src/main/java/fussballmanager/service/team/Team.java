package fussballmanager.service.team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.team.startelf.Startelf;
import fussballmanager.service.user.User;


@Entity
public class Team implements Comparable<Team> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@ManyToOne
	private Land land;
	
	private String name;
		
	private double geld;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Liga liga;
	
	private String spielort;
			
	private final int maximaleSpielerAnzahl = 43;
	
	@OneToOne
	private Startelf startelf;
	
	private EinsatzTypen einsatzTyp;
	
	private AusrichtungsTypen ausrichtungsTyp;
	
	public Team(Land land, String name, User user, Liga liga) {
		this.land = land;
		this.name = name;
		this.geld = 500000.0;
		this.user = user;
		this.liga = liga;
		this.spielort = "Unbennantes Stadion";
		this.einsatzTyp = EinsatzTypen.NORMAL;
		this.ausrichtungsTyp = AusrichtungsTypen.NORMAL;
	}
	
	public Team() {
		
	}

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

	public String getSpielort() {
		return spielort;
	}

	public void setSpielort(String spielort) {
		this.spielort = spielort;
	}

	public int getMaximaleSpielerAnzahl() {
		return maximaleSpielerAnzahl;
	}

	public Startelf getStartelf() {
		return startelf;
	}

	public void setStartelf(Startelf startelf) {
		this.startelf = startelf;
	}

	public EinsatzTypen getEinsatzTyp() {
		return einsatzTyp;
	}

	public void setEinsatzTyp(EinsatzTypen einsatzTyp) {
		this.einsatzTyp = einsatzTyp;
	}

	public AusrichtungsTypen getAusrichtungsTyp() {
		return ausrichtungsTyp;
	}

	public void setAusrichtungsTyp(AusrichtungsTypen ausrichtungsTyp) {
		this.ausrichtungsTyp = ausrichtungsTyp;
	}

	@Override
	public int compareTo(Team arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
