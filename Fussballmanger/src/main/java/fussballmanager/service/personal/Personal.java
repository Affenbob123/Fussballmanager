package fussballmanager.service.personal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.land.Land;
import fussballmanager.service.team.Team;

@Entity
public class Personal implements Comparable<Personal>{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	@OneToOne
	private Team team;
	
	private String name = "Unnamed Staff";
	
	@OneToOne
	private Land nationalitaet;
	
	private int alter;
	
	private int talentwert;
	
	private int erfahrung = 0;
	
	private PersonalTypen personalTyp;
	
	private double staerke;
	
	private double staerkeZuwachs;
	
	@Formula("staerke * 100")
	private long gehalt;
	
	private long preis;
	
	private boolean talentwertErmittelt = false;
	
	private boolean transfermarkt = false;
	
	public Personal(Team team, Land nationalitaet, int alter, int talentwert, int erfahrung, 
			PersonalTypen personalTyp, double staerke, long preis) {
		this.team = team;
		this.nationalitaet = nationalitaet;
		this.alter = alter;
		this.talentwert = talentwert;
		this.erfahrung = erfahrung;
		this.personalTyp = personalTyp;
		this.staerke = staerke;
		this.preis = preis;
	}
	
	public Personal() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Land getNationalitaet() {
		return nationalitaet;
	}

	public void setNationalitaet(Land nationalitaet) {
		this.nationalitaet = nationalitaet;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public int getTalentwert() {
		return talentwert;
	}

	public void setTalentwert(int talentwert) {
		this.talentwert = talentwert;
	}

	public int getErfahrung() {
		return erfahrung;
	}

	public void setErfahrung(int erfahrung) {
		this.erfahrung = erfahrung;
	}

	public PersonalTypen getPersonalTyp() {
		return personalTyp;
	}

	public void setPersonalTyp(PersonalTypen personalTyp) {
		this.personalTyp = personalTyp;
	}

	public double getStaerke() {
		return staerke;
	}

	public void setStaerke(double staerke) {
		this.staerke = staerke;
	}

	public double getStaerkeZuwachs() {
		return staerkeZuwachs;
	}

	public void setStaerkeZuwachs(double staerkeZuwachs) {
		this.staerkeZuwachs = staerkeZuwachs;
	}

	public long getGehalt() {
		return gehalt;
	}

	public void setGehalt(long gehalt) {
		this.gehalt = gehalt;
	}

	public long getPreis() {
		return preis;
	}

	public void setPreis(long preis) {
		this.preis = preis;
	}

	public boolean isTalentwertErmittelt() {
		return talentwertErmittelt;
	}

	public void setTalentwertErmittelt(boolean talentwertErmittelt) {
		this.talentwertErmittelt = talentwertErmittelt;
	}

	public boolean isTransfermarkt() {
		return transfermarkt;
	}

	public void setTransfermarkt(boolean transfermarkt) {
		this.transfermarkt = transfermarkt;
	}

	@Override
	public int compareTo(Personal compareTo) {
		double compareToStaerke = compareTo.getStaerke();
		return (int) (compareToStaerke - this.staerke);
	}
}
