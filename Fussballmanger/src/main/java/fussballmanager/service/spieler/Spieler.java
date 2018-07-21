package fussballmanager.service.spieler;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachs;
import fussballmanager.service.team.Team;

@Entity
public class Spieler {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private PositionenTypen position;
	
	private int alter;
	
	private double staerke;
	
	@OneToMany
	private List<SpielerZuwachs> spielerZuwachs;
	
	private int talentwert;
	
	private int motivation;
	
	@ManyToOne
	private Team team;
	
	private int trainingslagerTage;

	public Spieler(PositionenTypen position, int staerke, int motivation,
			Team team, int trainingslagerTage) {
		this.position = position;
		this.staerke = staerke;
		this.motivation = motivation;
		this.team = team;
		this.trainingslagerTage = trainingslagerTage;
	}

	public Spieler() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PositionenTypen getPosition() {
		return position;
	}

	public void setPosition(PositionenTypen position) {
		this.position = position;
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
}
