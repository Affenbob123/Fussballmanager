package fussballmanager.service.finanzen;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Bilanz {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	private long saldo = 10000000;
	
	private long stadionEinnahmen = 0;
	
	private long sponsorenEinnahmen = 0;
	
	private long praemienEinnahmen = 0;
	
	private long spielerVerkaufEinnahmen = 0;
	
	private long gehaelterAusgaben = 0;
	
	private long trainingsAusgaben = 0;
	
	private long spielerEinkaufAusgaben = 0;
	
	private long sonstigeAusgaben = 0;
	
	public Bilanz() {
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSaldo() {
		return saldo;
	}

	public void setSaldo(long saldo) {
		this.saldo = saldo;
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

	public long getSonstigeAusgaben() {
		return sonstigeAusgaben;
	}

	public void setSonstigeAusgaben(long sonstigeAusgaben) {
		this.sonstigeAusgaben = sonstigeAusgaben;
	}
}
