package fussballmanager.service.spieler.staerke;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Staerke {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	Long id;
	
	private double geschwindigkeit;
	
	private double schießen;
	
	private double passen;
	
	private double dribbeln;
	
	private double verteidigung;
	
	private double physis;
	
	private double durchschnittsStaerke;
	
	public Staerke(double geschwindigkeit, double schießen, double passen, double dribbeln, double verteidigung,
			double physis) {
		this.geschwindigkeit = geschwindigkeit;
		this.schießen = schießen;
		this.passen = passen;
		this.dribbeln = dribbeln;
		this.verteidigung = verteidigung;
		this.physis = physis;
		this.durchschnittsStaerke = (geschwindigkeit + schießen + passen 
				+ dribbeln + verteidigung + physis) / 6.0;
	}

	public Staerke() {
		
	}

	public double getGeschwindigkeit() {
		return geschwindigkeit;
	}

	public void setGeschwindigkeit(double geschwindigkeit) {
		this.geschwindigkeit = geschwindigkeit;
	}

	public double getSchießen() {
		return schießen;
	}

	public void setSchießen(double schießen) {
		this.schießen = schießen;
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

	public double getVerteidigung() {
		return verteidigung;
	}

	public void setVerteidigung(double verteidigung) {
		this.verteidigung = verteidigung;
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
}
