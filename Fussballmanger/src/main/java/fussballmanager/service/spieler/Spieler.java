package fussballmanager.service.spieler;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fussballmanager.service.team.Team;

@Entity
public class Spieler {

	@Id
	private long id;
	
	private Position position;
	
	private int staerke;
	
	private int motivation;
	
	@ManyToOne
	private Team team;
	
	private int trainingslagerTage;

	public Spieler(long id, Position position, int staerke, int motivation,
			Team team, int trainingslagerTage) {
		this.id = id;
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getStaerke() {
		return staerke;
	}

	public void setStaerke(int staerke) {
		this.staerke = staerke;
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
