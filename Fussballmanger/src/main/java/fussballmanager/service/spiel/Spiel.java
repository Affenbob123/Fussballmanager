package fussballmanager.service.spiel;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.team.Team;
import fussballmanager.service.tor.Tor;

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
	private List<Tor> toreHeimMannschaft;
	
	@OneToMany
	private List<Tor> toreGastMannschaft;
	
	private LocalDateTime spielbeginn;
	
	private String spielort;
	
	@ManyToOne
	private Saison saison;

	public Spiel(Team heimmannschaft, Team gastmannschaft, LocalDateTime spielbeginn, 
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

	public List<Tor> getToreHeimMannschaft() {
		return toreHeimMannschaft;
	}

	public void setToreHeimMannschaft(List<Tor> toreHeimMannschaft) {
		this.toreHeimMannschaft = toreHeimMannschaft;
	}
	
	public void addToreHeimMannschaft(Tor tor) {
		toreHeimMannschaft.add(tor);
	}

	public List<Tor> getToreGastMannschaft() {
		return toreGastMannschaft;
	}

	public void setToreGastMannschaft(List<Tor> toreGastMannschaft) {
		this.toreGastMannschaft = toreGastMannschaft;
	}
	
	public void addToreGastMannschaft(Tor tor) {
		toreGastMannschaft.add(tor);
	}

	public LocalDateTime getSpielbeginn() {
		return spielbeginn;
	}

	public void setSpielbeginn(LocalDateTime spielbeginn) {
		this.spielbeginn = spielbeginn;
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
