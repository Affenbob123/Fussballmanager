package fussballmanager.service.finanzen;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Bilanz {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	private long stadionEinnahmen = 0;
	
	private long sponsorenEinnahmen = 0;
	
	private long praemienEinnahmen = 0;
	
	private long spielerVerkaufEinnahmen = 0;
	
	private long sonstigeEinnahmen = 10000000;
	
	private long gehaelterAusgaben = 0;
	
	private long trainingsAusgaben = 0;
	
	private long spielerEinkaufAusgaben = 0;
	
	private long zinsAufwendungen = 0;
	
	private long sonstigeAusgaben = 0;
	
	@Formula("stadion_Einnahmen + sponsoren_Einnahmen + praemien_Einnahmen + spieler_Verkauf_Einnahmen + sonstige_Einnahmen - "
			+ "gehaelter_Ausgaben - trainings_Ausgaben - spieler_Einkauf_Ausgaben - zins_Aufwendungen - sonstige_Ausgaben")
	private long saldo;
	
	public Bilanz() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStadionEinnahmen() {
		return stadionEinnahmen;
	}

	public void setStadionEinnahmen(long stadionEinnahmen) {
		this.stadionEinnahmen = stadionEinnahmen;
	}

	public long getSponsorenEinnahmen() {
		return sponsorenEinnahmen;
	}

	public void setSponsorenEinnahmen(long sponsorenEinnahmen) {
		this.sponsorenEinnahmen = sponsorenEinnahmen;
	}

	public long getPraemienEinnahmen() {
		return praemienEinnahmen;
	}

	public void setPraemienEinnahmen(long praemienEinnahmen) {
		this.praemienEinnahmen = praemienEinnahmen;
	}

	public long getSpielerVerkaufEinnahmen() {
		return spielerVerkaufEinnahmen;
	}

	public void setSpielerVerkaufEinnahmen(long spielerVerkaufEinnahmen) {
		this.spielerVerkaufEinnahmen = spielerVerkaufEinnahmen;
	}

	public long getSonstigeEinnahmen() {
		return sonstigeEinnahmen;
	}

	public void setSonstigeEinnahmen(long sonstigeEinnahmen) {
		this.sonstigeEinnahmen = sonstigeEinnahmen;
	}

	public long getGehaelterAusgaben() {
		return gehaelterAusgaben;
	}

	public void setGehaelterAusgaben(long gehaelterAusgaben) {
		this.gehaelterAusgaben = gehaelterAusgaben;
	}

	public long getTrainingsAusgaben() {
		return trainingsAusgaben;
	}

	public void setTrainingsAusgaben(long trainingsAusgaben) {
		this.trainingsAusgaben = trainingsAusgaben;
	}

	public long getSpielerEinkaufAusgaben() {
		return spielerEinkaufAusgaben;
	}

	public void setSpielerEinkaufAusgaben(long spielerEinkaufAusgaben) {
		this.spielerEinkaufAusgaben = spielerEinkaufAusgaben;
	}

	public long getZinsAufwendungen() {
		return zinsAufwendungen;
	}

	public void setZinsAufwendungen(long zinsAufwendungen) {
		this.zinsAufwendungen = zinsAufwendungen;
	}

	public long getSonstigeAusgaben() {
		return sonstigeAusgaben;
	}

	public void setSonstigeAusgaben(long sonstigeAusgaben) {
		this.sonstigeAusgaben = sonstigeAusgaben;
	}

	public long getSaldo() {
		return saldo;
	}

	public void setSaldo(long saldo) {
		this.saldo = saldo;
	}
}
