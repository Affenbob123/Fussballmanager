package fussballmanager.service.benachrichtigung;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.team.Team;

@Entity
public class Benachrichtigung {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	Long id;
	
	@OneToOne
	Team absender;
	
	@OneToOne
	Team empfaenger;
	
	String benachrichtigungsText;
	
	@ManyToOne
	Spieltag spieltag;
	
	LocalTime uhrzeit;
	
	boolean gelesen = false;
	
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
}
