package fussballmanager.service.team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.user.User;


@Entity
public class Team implements Comparable<Team> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@ManyToOne
	private Land land;
	
	private String name;
		
	private long geld;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Liga liga;
	
	private String spielort;
			
	private final int maximaleSpielerAnzahl = 43;
	
	private FormationsTypen formationsTyp;
	
	private EinsatzTypen einsatzTyp;
	
	private AusrichtungsTypen ausrichtungsTyp;
	
	private int anzahlAuswechselungen = 3;
	
	private double staerke = 0.0;
	
	public Team(Land land, String name, User user, Liga liga) {
		this.land = land;
		this.name = name;
		this.geld = 500000;
		this.user = user;
		this.liga = liga;
		this.spielort = "Unbenanntes Stadion";
		this.formationsTyp = FormationsTypen.VIERVIERZWEI;
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

	public long getGeld() {
		return geld;
	}

	public void setGeld(long geld) {
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

	public FormationsTypen getFormationsTyp() {
		return formationsTyp;
	}

	public void setFormationsTyp(FormationsTypen formationsTyp) {
		this.formationsTyp = formationsTyp;
	}
	
	public int getAnzahlAuswechselungen() {
		return anzahlAuswechselungen;
	}

	public void setAnzahlAuswechselungen(int anzahlAuswechselungen) {
		this.anzahlAuswechselungen = anzahlAuswechselungen;
	}

	public double getStaerke() {
		return staerke;
	}

	public void setStaerke(double staerke) {
		this.staerke = staerke;
	}

	@Override
	public int compareTo(Team arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
