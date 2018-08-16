package fussballmanager.service.team.stadion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Stadion {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	private String name = "Unbenanntes Stadion";
	
	private int sitzplaetze = 5000;
	
	private int sitzplatzAusbauTage = 0;
	
	private final int maximaleSitzplaetze = 100000;
	
	private long ticketPreis = 5;
	
	private StadionAusbauTypen aktuellAusgebauterTyp;
	
	private int uebrigeAusbauTage = 0;
	
	private StadionAusbauTypen imbiss = StadionAusbauTypen.IMBISS;
	
	private StadionAusbauTypen trainingsGelaende = StadionAusbauTypen.TRAININGSGELAENDE;
	
	private StadionAusbauTypen vipLounge = StadionAusbauTypen.VIPLOUNGE;
	
	private StadionAusbauTypen ersatzbank = StadionAusbauTypen.ERSATZBANK;
	
	private StadionAusbauTypen werbebanden = StadionAusbauTypen.WERBEBANDEN;
	
	private StadionAusbauTypen jugendInternat = StadionAusbauTypen.JUGENDINTERNAT;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSitzplaetze() {
		return sitzplaetze;
	}

	public void setSitzplaetze(int sitzplaetze) {
		this.sitzplaetze = sitzplaetze;
	}

	public long getTicketPreis() {
		return ticketPreis;
	}

	public void setTicketPreis(long ticketPreis) {
		this.ticketPreis = ticketPreis;
	}

	public StadionAusbauTypen getAktuellAusgebauterTyp() {
		return aktuellAusgebauterTyp;
	}

	public void setAktuellAusgebauterTyp(StadionAusbauTypen aktuellAusgebauterTyp) {
		this.aktuellAusgebauterTyp = aktuellAusgebauterTyp;
	}

	public int getUebrigeAusbauTage() {
		return uebrigeAusbauTage;
	}

	public void setUebrigeAusbauTage(int uebrigeAusbauTage) {
		this.uebrigeAusbauTage = uebrigeAusbauTage;
	}

	public StadionAusbauTypen getImbiss() {
		return imbiss;
	}

	public void setImbiss(StadionAusbauTypen imbiss) {
		this.imbiss = imbiss;
	}

	public StadionAusbauTypen getTrainingsGelaende() {
		return trainingsGelaende;
	}

	public void setTrainingsGelaende(StadionAusbauTypen trainingsGelaende) {
		this.trainingsGelaende = trainingsGelaende;
	}

	public StadionAusbauTypen getVipLounge() {
		return vipLounge;
	}

	public void setVipLounge(StadionAusbauTypen vipLounge) {
		this.vipLounge = vipLounge;
	}

	public StadionAusbauTypen getErsatzbank() {
		return ersatzbank;
	}

	public void setErsatzbank(StadionAusbauTypen ersatzbank) {
		this.ersatzbank = ersatzbank;
	}

	public StadionAusbauTypen getWerbebanden() {
		return werbebanden;
	}

	public void setWerbebanden(StadionAusbauTypen werbebanden) {
		this.werbebanden = werbebanden;
	}

	public StadionAusbauTypen getJugendInternat() {
		return jugendInternat;
	}

	public void setJugendInternat(StadionAusbauTypen jugendInternat) {
		this.jugendInternat = jugendInternat;
	}

	public int getMaximaleSitzplaetze() {
		return maximaleSitzplaetze;
	}

	public int getSitzplatzAusbauTage() {
		return sitzplatzAusbauTage;
	}

	public void setSitzplatzAusbauTage(int sitzplatzAusbauTage) {
		this.sitzplatzAusbauTage = sitzplatzAusbauTage;
	}
}
