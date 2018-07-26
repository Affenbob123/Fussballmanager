package fussballmanager.service.spiel;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.team.Team;

@Entity
@Cacheable(false)
public class Spiel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private SpieleTypen spielTyp;
	
	@OneToOne
	private Team heimmannschaft;
	
	@OneToOne
	private Team gastmannschaft;
	
	@ManyToOne
	private Spieltag spieltag;
	
	@ManyToOne
	private Saison saison;
	
	private String spielort;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private List<SpielEreignis> spielEreignisse;
	
	private double heimVorteil = 1.3;

	public Spiel(SpieleTypen spielTyp, Team heimmannschaft, Team gastmannschaft, LocalTime spielbeginn, 
			Spieltag spieltag, Saison saison,String spielort) {
		this.heimmannschaft = heimmannschaft;
		this.gastmannschaft = gastmannschaft;
		this.spielTyp = spielTyp;
		this.spieltag = spieltag;
		this.saison = saison;
		this.spielort = spielort;
	}
	
	public Spiel() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public SpieleTypen getSpielTyp() {
		return spielTyp;
	}

	public void setSpielTyp(SpieleTypen spielTyp) {
		this.spielTyp = spielTyp;
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

	public Spieltag getSpieltag() {
		return spieltag;
	}

	public void setSpieltag(Spieltag spieltag) {
		this.spieltag = spieltag;
	}

	public Saison getSaison() {
		return saison;
	}

	public void setSaison(Saison saison) {
		this.saison = saison;
	}

	public String getSpielort() {
		return spielort;
	}

	public void setSpielort(String spielort) {
		this.spielort = spielort;
	}

	public List<SpielEreignis> getSpielEreignisse() {
		return spielEreignisse;
	}

	public void setSpielEreignisse(List<SpielEreignis> spielEreignisse) {
		this.spielEreignisse = spielEreignisse;
	}
	
	public void addSpielEreignis(SpielEreignis spielEreignis) {
		this.spielEreignisse.add(spielEreignis);
	}

	public double getHeimVorteil() {
		return heimVorteil;
	}

	public void setHeimVorteil(double heimVorteil) {
		this.heimVorteil = heimVorteil;
	}
}
