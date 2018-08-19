package fussballmanager.mvc.kader;

import java.util.List;

import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.spielerzuwachs.Trainingslager;

public class TrainingslagerWrapper {

	Trainingslager trainingslager = Trainingslager.FERIENCAMP;
	
	List<Spieler> spieler;

	public Trainingslager getTrainingslager() {
		return trainingslager;
	}

	public void setTrainingslager(Trainingslager trainingslager) {
		this.trainingslager = trainingslager;
	}

	public List<Spieler> getSpieler() {
		return spieler;
	}

	public void setSpieler(List<Spieler> spieler) {
		this.spieler = spieler;
	}
}
