package fussballmanager.service.spieler.spielerzuwachs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.spieler.Spieler;

@Entity
public class SpielerZuwachs {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@OneToOne
	Saison saison;
	
	@OneToOne
	Spieltag spieltag;
	
	private double zuwachs;
	
	Trainingslager trainingslager;
	
	ZuwachsFaktorAlter zuwachsFaktorAlter;
	
	public final double defaultZuwachs = 2.0;
	
	public final int maximaleErfahrung = 75;
	
	public SpielerZuwachs(Saison saison, Spieltag spieltag) {
		this.saison = saison;
		this.spieltag = spieltag;
	}
	
	public SpielerZuwachs() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}

	public Spieltag getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(Spieltag spieltag) {
		this.spieltag = spieltag;
	}

	public double getZuwachs() {
		return zuwachs;
	}

	public void setZuwachs(double zuwachs) {
		this.zuwachs = zuwachs;
	}

	public Trainingslager getTrainingslager() {
		return trainingslager;
	}

	public void setTrainingslager(Trainingslager trainingslager) {
		this.trainingslager = trainingslager;
	}

	public double getDefaultZuwachs() {
		return defaultZuwachs;
	}

	public int getMaximaleErfahrung() {
		return maximaleErfahrung;
	}
	
	public double berechneSpielerZuwachsFuerEinenSpieler(Spieler spieler) {
		int alter = spieler.getAlter();
		int talentwert = spieler.getTalentwert();
		int erfahrung = spieler.getErfahrung();
		int anzahlDerSaisonsDesSpielers = alter - 13;
		double zuwachsFaktorNachAlterDesSpielers = 1.0;
		for(ZuwachsFaktorAlter zFA : ZuwachsFaktorAlter.values()) {
			if(zFA.getAlter() == spieler.getAlter()) {
				zuwachsFaktorNachAlterDesSpielers = zFA.getZuwachsFaktor();
			}
		}
		
		double erfahrungsFaktorRechnungEins  = erfahrung * 1.0 / (maximaleErfahrung * anzahlDerSaisonsDesSpielers);
		double erfahrungsFaktor = (erfahrungsFaktorRechnungEins + 1) / 2;
		double zuwachsOhneErfahrung = defaultZuwachs * zuwachsFaktorNachAlterDesSpielers * (100 + (talentwert * 2)) / 100;
		double zuwachsMitErfahrung = zuwachsOhneErfahrung * erfahrungsFaktor;
		
		return zuwachsMitErfahrung;
	}
}
