package fussballmanager.service.team;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.finanzen.Bilanz;
import fussballmanager.service.land.Land;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.team.stadion.Stadion;
import fussballmanager.service.user.User;


@Entity
public class Team implements Comparable<Team> {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Land land;
	
	private String name;
		
	@OneToOne
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Liga liga;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Bilanz bilanz;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Stadion stadion;
			
	private final int maximaleSpielerAnzahl = 43;
	
	private FormationsTypen formationsTyp = FormationsTypen.VIERVIERZWEI;
	
	private EinsatzTypen einsatzTyp = EinsatzTypen.NORMAL;
	
	private AusrichtungsTypen ausrichtungsTyp = AusrichtungsTypen.NORMAL;
	
	private int anzahlAuswechselungen = 3;
	
	private double staerke = 0.0;
	
	private boolean imLiveticker = true;
	
	public Team(Land land, String name, User user, Liga liga, Bilanz bilanz, Stadion stadion) {
		this.land = land;
		this.name = name;
		this.user = user;
		this.liga = liga;
		this.bilanz = bilanz;
		this.stadion = stadion;
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

	public Stadion getStadion() {
		return stadion;
	}

	public void setStadion(Stadion stadion) {
		this.stadion = stadion;
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

	public boolean isImLiveticker() {
		return imLiveticker;
	}

	public void setImLiveticker(boolean imLiveticker) {
		this.imLiveticker = imLiveticker;
	}

	public Bilanz getBilanz() {
		return bilanz;
	}

	public void setBilanz(Bilanz bilanz) {
		this.bilanz = bilanz;
	}

	@Override
	public int compareTo(Team arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
