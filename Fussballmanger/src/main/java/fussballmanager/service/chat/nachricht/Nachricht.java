package fussballmanager.service.chat.nachricht;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.chat.Chat;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.user.User;

@Entity
public class Nachricht implements Comparable<Nachricht> {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	@OneToOne
	private User absender;
	
	@OneToOne
	private Spieltag spieltag;
	
	private LocalTime uhrzeit;
		
	@Lob
	private String nachricht;
	
	private boolean gelesen = false;
	
	public Nachricht(User absender, Spieltag spieltag, String nachricht, LocalTime uhrzeit) {
		this.nachricht = nachricht;
		this.absender = absender;
		this.spieltag = spieltag;
		this.uhrzeit = uhrzeit;
	}
	
	public Nachricht() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getAbsender() {
		return absender;
	}

	public void setAbsender(User absender) {
		this.absender = absender;
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

	public String getNachricht() {
		return nachricht;
	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

	public boolean isGelesen() {
		return gelesen;
	}

	public void setGelesen(boolean gelesen) {
		this.gelesen = gelesen;
	}

	@Override
	public int compareTo(Nachricht compareTo) {
		int compareToSpieltag = compareTo.getSpieltag().getSpieltagNummer();
		LocalTime compareToUhrzeit = compareTo.getUhrzeit();
		
		if(compareToSpieltag == this.spieltag.getSpieltagNummer()) {
			return uhrzeit.compareTo(compareToUhrzeit);
		}
		return compareToSpieltag - this.spieltag.getSpieltagNummer();
	}
}
