package fussballmanager.service.spiel;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.team.Team;
import fussballmanager.service.torversuch.Torversuch;

@Entity
public class Spiel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@ManyToOne
	private Team heimmannschaft;
	
	@ManyToOne
	private Team gastmannschaft;
	
	@OneToMany
	private List<Torversuch> toreHeimMannschaft;
	
	@OneToMany
	private List<Torversuch> toreGastMannschaft;
	
	private int spieltag;
	
	private LocalTime spielbeginn;
	
	private String spielort;
	
	@ManyToOne
	private Saison saison;

	public Spiel(Team heimmannschaft, Team gastmannschaft, LocalTime spielbeginn, 
			String spielort, Saison saison) {
		this.heimmannschaft = heimmannschaft;
		this.gastmannschaft = gastmannschaft;
		this.spielbeginn = spielbeginn;
		this.spielort = spielort;
		this.saison = saison;
	}
	
	public Spiel() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Team getHeimmannschaft() {
		return heimmannschaft;
	}

	public void setHeimmannschaft(Team heimmannschaft) {
		this.heimmannschaft = heimmannschaft;
	}

	public Team getGastmannschaft() {
		return gastmannschaft;
	}

	public void setGastmannschaft(Team gastmannschaft) {
		this.gastmannschaft = gastmannschaft;
	}

	public List<Torversuch> getToreHeimMannschaft() {
		return toreHeimMannschaft;
	}

	public void setToreHeimMannschaft(List<Torversuch> toreHeimMannschaft) {
		this.toreHeimMannschaft = toreHeimMannschaft;
	}
	
	public void addToreHeimMannschaft(Torversuch tor) {
		toreHeimMannschaft.add(tor);
	}

	public List<Torversuch> getToreGastMannschaft() {
		return toreGastMannschaft;
	}

	public void setToreGastMannschaft(List<Torversuch> toreGastMannschaft) {
		this.toreGastMannschaft = toreGastMannschaft;
	}
	
	public void addToreGastMannschaft(Torversuch tor) {
		toreGastMannschaft.add(tor);
	}

	public LocalTime getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(LocalTime spielbeginn) {
		this.spielbeginn = spielbeginn;
	}

	public int getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(int spieltag) {
		this.spieltag = spieltag;
	}

	public String getSpielort() {
		return spielort;
	}

	public void setSpielort(String spielort) {
		this.spielort = spielort;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}
}
