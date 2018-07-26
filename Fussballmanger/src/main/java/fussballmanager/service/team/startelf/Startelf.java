package fussballmanager.service.team.startelf;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.team.FormationsTypen;

@Entity
public class Startelf {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@OneToMany
	private List<Spieler> startelfSpieler;
	
	private FormationsTypen formationsTyp;
	
	private final int maximaleSpielerAnzahl = 11;
	
	public Startelf(List<Spieler> startelfSpieler, FormationsTypen formationsTyp) {
		this.startelfSpieler = startelfSpieler;
		this.formationsTyp = FormationsTypen.VIERVIERZWEI;
	}

	public Startelf() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Spieler> getStartelfSpieler() {
		return startelfSpieler;
	}

	public void setStartelfSpieler(List<Spieler> startelfSpieler) {
		this.startelfSpieler = startelfSpieler;
	}
	
	public void addStartelfSpieler(Spieler spieler) {
		this.startelfSpieler.add(spieler);
	}

	public int getMaximaleSpielerAnzahl() {
		return maximaleSpielerAnzahl;
	}

	public FormationsTypen getFormationsTyp() {
		return formationsTyp;
	}

	public void setFormationsTyp(FormationsTypen formationsTyp) {
		this.formationsTyp = formationsTyp;
	}
}
