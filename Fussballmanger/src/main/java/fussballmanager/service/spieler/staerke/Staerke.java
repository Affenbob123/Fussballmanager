package fussballmanager.service.spieler.staerke;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Staerke {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	Long id;
	
	private double geschwindigkeit;
	
	private double schiessen;
	
	private double passen;
	
	private double dribbeln;
	
	private double verteidigen;
	
	private double physis;
	
	private double durchschnittsStaerke;
	
	public Staerke(double geschwindigkeit, double schiessen, double passen, double dribbeln, double verteidigen,
			double physis) {
		this.geschwindigkeit = geschwindigkeit;
		this.schiessen = schiessen;
		this.passen = passen;
		this.dribbeln = dribbeln;
		this.verteidigen = verteidigen;
		this.physis = physis;
		this.durchschnittsStaerke = (this.geschwindigkeit + this.schiessen + this.passen 
				+ this.dribbeln + this.verteidigen + this.physis) / 6.0;
	}

	public Staerke() {
		
	}

	public double getGeschwindigkeit() {
		return geschwindigkeit;
	}

	public void setGeschwindigkeit(double geschwindigkeit) {
		this.geschwindigkeit = geschwindigkeit;
	}

	public double getSchiessen() {
		return schiessen;
	}

	public void setSchiessen(double schießen) {
		this.schiessen = schießen;
	}

	public double getPassen() {
		return passen;
	}

	public void setPassen(double passen) {
		this.passen = passen;
	}

	public double getDribbeln() {
		return dribbeln;
	}

	public void setDribbeln(double dribbeln) {
		this.dribbeln = dribbeln;
	}

	public double getVerteidigen() {
		return verteidigen;
	}

	public void setVerteidigen(double verteidigen) {
		this.verteidigen = verteidigen;
	}

	public double getPhysis() {
		return physis;
	}

	public void setPhysis(double physis) {
		this.physis = physis;
	}
	
	public double getDurchschnittsStaerke() {
		return this.durchschnittsStaerke;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDurchschnittsStaerke(double durchschnittsStaerke) {
		this.durchschnittsStaerke = durchschnittsStaerke;
	}
}
