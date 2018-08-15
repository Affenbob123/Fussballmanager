package fussballmanager.service.spieler;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.land.Land;
import fussballmanager.service.spieler.spielerzuwachs.Trainingslager;
import fussballmanager.service.spieler.staerke.SpielerStaerke;
import fussballmanager.service.team.Team;

@Entity
public class Spieler implements Comparable<Spieler> {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	@OneToOne
	private Land nationalitaet;
	
	@ManyToOne
	private Team team;
	
	@Enumerated(EnumType.STRING)
	private PositionenTypen position;
	
	@Enumerated(EnumType.STRING)
	private AufstellungsPositionsTypen aufstellungsPositionsTyp;
	
	private String name = "Unbenannter Spieler";
	
	private int alter;
	
	@OneToOne
	private SpielerStaerke spielerStaerke;
	
	private double spielerZuwachs = 0.0;
	
	private int erfahrung;
	
	private int talentwert;
	
	private boolean talentwertErmittelt;
	
	private int motivation = 0;
	
	private int uebrigeTrainingslagerTage = 10;
	
	private int trainingslagerTage = 0;
	
	private Trainingslager trainingslager = Trainingslager.KEIN_TRAININGSLAGER;
	
	private int verletzungsTage = 0;
	
	private int gesperrteTage = 0;
	
	private long gehalt;
	
	private long preis;
	
	private boolean transfermarkt = false;
	
	private boolean gelbeKarte = false;
	
	private int tore = 0;
	
	private int gelbeKarten = 0;
	
	private int gelbRoteKarten = 0;
	
	private int roteKarten = 0;

	public Spieler(Land nationalitaet, PositionenTypen position, AufstellungsPositionsTypen aufstellungsPositionsTyp,
			int alter, SpielerStaerke spielerStaerke, int talentwert, Team team) {
		this.nationalitaet = nationalitaet;
		this.position = position;
		this.aufstellungsPositionsTyp = aufstellungsPositionsTyp;
		this.alter = alter;
		this.spielerStaerke = spielerStaerke;
		this.talentwert = talentwert;
		this.team = team;
		this.talentwertErmittelt =  false;
		this.erfahrung = 0;
		this.motivation = 0;
		this.verletzungsTage = 0;
		this.gehalt = (long) (spielerStaerke.getReinStaerke() * 100);
		this.preis = (long) (spielerStaerke.getReinStaerke() * 1000);
		this.transfermarkt = false;
	}

	public Spieler() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Land getNationalitaet() {
		return nationalitaet;
	}

	public void setNationalitaet(Land nationalitaet) {
		this.nationalitaet = nationalitaet;
	}

	public PositionenTypen getPosition() {
		return position;
	}

	public void setPosition(PositionenTypen position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAlter() {
		return alter;
	}

	public void setAlter(int alter) {
		this.alter = alter;
	}

	public int getErfahrung() {
		return erfahrung;
	}

	public void setErfahrung(int erfahrung) {
		this.erfahrung = erfahrung;
	}

	public int getTalentwert() {
		return talentwert;
	}

	public void setTalentwert(int talentwert) {
		this.talentwert = talentwert;
	}

	public int getMotivation() {
		return motivation;
	}

	public void setMotivation(int motivation) {
		this.motivation = motivation;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getTrainingslagerTage() {
		return trainingslagerTage;
	}

	public void setTrainingslagerTage(int trainingslagerTage) {
		this.trainingslagerTage = trainingslagerTage;
	}

	public Trainingslager getTrainingslager() {
		return trainingslager;
	}

	public void setTrainingsLager(Trainingslager trainingslager) {
		this.trainingslager = trainingslager;
	}

	public int getVerletzungsTage() {
		return verletzungsTage;
	}

	public void setVerletzungsTage(int verletzungsTage) {
		this.verletzungsTage = verletzungsTage;
	}

	public AufstellungsPositionsTypen getAufstellungsPositionsTyp() {
		return aufstellungsPositionsTyp;
	}

	public void setAufstellungsPositionsTyp(AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		this.aufstellungsPositionsTyp = aufstellungsPositionsTyp;
	}

	public long getGehalt() {
		return gehalt;
	}

	public void setGehalt(long gehalt) {
		this.gehalt = gehalt;
	}

	public SpielerStaerke getSpielerStaerke() {
		return spielerStaerke;
	}

	public void setSpielerStaerke(SpielerStaerke spielerStaerke) {
		this.spielerStaerke = spielerStaerke;
	}

	public double getSpielerZuwachs() {
		return spielerZuwachs;
	}

	public void setSpielerZuwachs(double spielerZuwachs) {
		this.spielerZuwachs = spielerZuwachs;
	}

	public long getPreis() {
		return preis;
	}

	public void setPreis(long preis) {
		this.preis = preis;
	}

	public boolean isTalentwertErmittelt() {
		return talentwertErmittelt;
	}

	public void setTalentwertErmittelt(boolean talentwertErmittelt) {
		this.talentwertErmittelt = talentwertErmittelt;
	}

	public boolean isTransfermarkt() {
		return transfermarkt;
	}

	public void setTransfermarkt(boolean istTransfermarkt) {
		this.transfermarkt = istTransfermarkt;
	}

	public int getTore() {
		return tore;
	}

	public void setTore(int tore) {
		this.tore = tore;
	}

	public int getGelbeKarten() {
		return gelbeKarten;
	}

	public void setGelbeKarten(int gelbeKarten) {
		this.gelbeKarten = gelbeKarten;
	}

	public int getGelbRoteKarten() {
		return gelbRoteKarten;
	}

	public void setGelbRoteKarten(int gelbRoteKarten) {
		this.gelbRoteKarten = gelbRoteKarten;
	}

	public int getRoteKarten() {
		return roteKarten;
	}

	public void setRoteKarten(int roteKarten) {
		this.roteKarten = roteKarten;
	}

	public boolean isGelbeKarte() {
		return gelbeKarte;
	}

	public void setGelbeKarte(boolean gelbeKarte) {
		this.gelbeKarte = gelbeKarte;
	}

	public int getGesperrteTage() {
		return gesperrteTage;
	}

	public void setGesperrteTage(int gesperrteTage) {
		this.gesperrteTage = gesperrteTage;
	}

	public int getUebrigeTrainingslagerTage() {
		return uebrigeTrainingslagerTage;
	}

	public void setUebrigeTrainingslagerTage(int uebrigeTrainingslagerTage) {
		this.uebrigeTrainingslagerTage = uebrigeTrainingslagerTage;
	}

	@Override
	public int compareTo(Spieler compareTo) {
		int compareAufstellungsPosition=((Spieler)compareTo).getAufstellungsPositionsTyp().getRangfolge();
		int comparePosition=((Spieler)compareTo).getPosition().getRangfolge();
		
		if(this.aufstellungsPositionsTyp.getRangfolge() - compareAufstellungsPosition == 0) {
			return this.position.getRangfolge() - comparePosition;
		}
		return this.aufstellungsPositionsTyp.getRangfolge() - compareAufstellungsPosition;
	}
}
