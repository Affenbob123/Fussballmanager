package fussballmanager.service.spiel.turnier;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.mvc.spiel.SpielEintrag;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.spiel.KOSpielTypen;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.team.Team;
import fussballmanager.service.user.User;

@Entity
public class Turnier implements Comparable<Turnier>{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@OneToOne
	private User user;
	
	@ManyToMany
	private List<Team> teams;
	
	@OneToMany
	private List<Spiel> turnierSpiele;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Spieltag spieltag;
	
	private long praemien = 0;
	
	private String name;
	
	@Lob
	private String beschreibung;
	
	private boolean geschlossen = false;
	
	private boolean gestartet = false;
	
	private KOSpielTypen kOSpielTyp;
	
	public Turnier(User user, Spieltag spieltag, String name) {
		this.user = user;
		this.spieltag = spieltag;
		this.name = name;
	}
	
	public Turnier() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public Spieltag getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(Spieltag spieltag) {
		this.spieltag = spieltag;
	}

	public long getPraemien() {
		return praemien;
	}

	public void setPraemien(long praemien) {
		this.praemien = praemien;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public boolean isGeschlossen() {
		return geschlossen;
	}

	public void setGeschlossen(boolean geschlossen) {
		this.geschlossen = geschlossen;
	}

	public boolean isGestartet() {
		return gestartet;
	}

	public void setGestartet(boolean gestartet) {
		this.gestartet = gestartet;
	}

	public List<Spiel> getTurnierSpiele() {
		return turnierSpiele;
	}

	public void setTurnierSpiele(List<Spiel> turnierSpiele) {
		this.turnierSpiele = turnierSpiele;
	}

	public KOSpielTypen getkOSpielTyp() {
		return kOSpielTyp;
	}

	public void setkOSpielTyp(KOSpielTypen kOSpielTyp) {
		this.kOSpielTyp = kOSpielTyp;
	}

	@Override
	public int compareTo(Turnier compareTo) {
		int compareSpieltag=((Turnier)compareTo).getSpieltag().getSpieltagNummer();
		
		return this.spieltag.getSpieltagNummer() - compareSpieltag;
	}
}
