package fussballmanager.service.benachrichtigung;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.team.Team;

@Entity
public class Benachrichtigung implements Comparable<Benachrichtigung>{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	Long id;
	
	BenachrichtigungsTypen benachrichtungsTyp;
	
	@OneToOne
	Team absender;
	
	@OneToOne
	Team empfaenger;
	
	@Lob
	String benachrichtigungsText;
	
	@ManyToOne
	Spieltag spieltag;
	
	@OneToOne
	Spieler spieler;
	
	LocalTime uhrzeit;
	
	boolean gelesen = false;
	
	boolean geantwortet = false;
	
	AntwortTypen antwortTyp = AntwortTypen.KEINE;
	
	public Benachrichtigung() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Team getAbsender() {
		return absender;
	}

	public void setAbsender(Team absender) {
		this.absender = absender;
	}

	public Team getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(Team empfaenger) {
		this.empfaenger = empfaenger;
	}

	public String getBenachrichtigungsText() {
		return benachrichtigungsText;
	}

	public void setBenachrichtigungsText(String benachrichtigungsText) {
		this.benachrichtigungsText = benachrichtigungsText;
	}

	public Spieltag getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(Spieltag spieltag) {
		this.spieltag = spieltag;
	}

	public LocalTime getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(LocalTime uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public boolean isGelesen() {
		return gelesen;
	}

	public void setGelesen(boolean gelesen) {
		this.gelesen = gelesen;
	}

	public BenachrichtigungsTypen getBenachrichtungsTyp() {
		return benachrichtungsTyp;
	}

	public void setBenachrichtungsTyp(BenachrichtigungsTypen benachrichtungsTyp) {
		this.benachrichtungsTyp = benachrichtungsTyp;
	}

	public Spieler getSpieler() {
		return spieler;
	}

	public void setSpieler(Spieler spieler) {
		this.spieler = spieler;
	}

	public AntwortTypen getAntwortTyp() {
		return antwortTyp;
	}

	public void setAntwortTyp(AntwortTypen antwortTyp) {
		this.antwortTyp = antwortTyp;
	}

	public boolean isGeantwortet() {
		return geantwortet;
	}

	public void setGeantwortet(boolean geantwortet) {
		this.geantwortet = geantwortet;
	}

	@Override
	public int compareTo(Benachrichtigung compareTo) {
		Spieltag compareToSpieltag = compareTo.getSpieltag();
		LocalTime compareToUhrzeit = compareTo.getUhrzeit();
		
		if(compareToSpieltag.getSpieltagNummer() == this.spieltag.getSpieltagNummer()) {
			return compareToUhrzeit.compareTo(this.uhrzeit);
		}
		return compareToSpieltag.getSpieltagNummer() - this.spieltag.getSpieltagNummer();
	}
}
