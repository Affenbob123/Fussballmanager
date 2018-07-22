package fussballmanager.service.spieler;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import fussballmanager.service.land.Land;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachs;
import fussballmanager.service.team.Team;

@Entity
public class Spieler implements Comparable<Spieler>{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@OneToOne
	private Land nationalitaet;
	
	private PositionenTypen position;
	
	private String name;
	
	private int alter;
	
	private double staerke;
	
	private int erfahrung;
	
	@OneToMany
	private List<SpielerZuwachs> spielerZuwachs;
	
	private int talentwert;
	
	private int motivation;
	
	@ManyToOne
	private Team team;
	
	private int trainingslagerTage;
	
	private int verletzungsTage;

	public Spieler(Land nationalitaet, PositionenTypen position, int alter, double staerke, int talentwert, Team team) {
		this.nationalitaet = nationalitaet;
		this.position = position;
		this.name = "Unbennanter Spieler";
		this.alter = alter;
		this.staerke = staerke;
		this.talentwert = talentwert;
		this.erfahrung = 0;
		this.motivation = 0;
		this.team = team;
		this.trainingslagerTage = 10;
		this.verletzungsTage = 0;
	}

	public Spieler() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Land getNationalitaet() {
		return nationalitaet;
	}

	public void setNationalitaet(Land nationalitaet) {
		this.nationalitaet = nationalitaet;
	}

	public PositionenTypen getPosition() {
		return position;
	}

	public void setPosition(PositionenTypen position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public double getStaerke() {
		return staerke;
	}

	public void setStaerke(double staerke) {
		this.staerke = staerke;
	}

	public int getErfahrung() {
		return erfahrung;
	}

	public void setErfahrung(int erfahrung) {
		this.erfahrung = erfahrung;
	}

	public List<SpielerZuwachs> getSpielerZuwachs() {
		return spielerZuwachs;
	}

	public void setSpielerZuwachs(List<SpielerZuwachs> spielerZuwachs) {
		this.spielerZuwachs = spielerZuwachs;
	}

	public int getTalentwert() {
		return talentwert;
	}

	public void setTalentwert(int talentwert) {
		this.talentwert = talentwert;
	}

	public int getMotivation() {
		return motivation;
	}

	public void setMotivation(int motivation) {
		this.motivation = motivation;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getTrainingslagerTage() {
		return trainingslagerTage;
	}

	public void setTrainingslagerTage(int trainingslagerTage) {
		this.trainingslagerTage = trainingslagerTage;
	}

	public int getVerletzungsTage() {
		return verletzungsTage;
	}

	public void setVerletzungsTage(int verletzungsTage) {
		this.verletzungsTage = verletzungsTage;
	}

	@Override
	public int compareTo(Spieler compareTo) {
		int comparePosition=((Spieler)compareTo).getPosition().getRangfolge();
		return this.position.getRangfolge() - comparePosition;
	}
}
