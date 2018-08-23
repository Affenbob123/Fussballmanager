package fussballmanager.service.auktionshaus;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import fussballmanager.service.team.Team;
import fussballmanager.service.user.User;

@Entity
public class AuktionshausEintrag{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@OneToOne
	private Team team;
	
	@OneToOne
	private User hoechstBieter;

	@Lob
	private String beschreibung;
	
	@NumberFormat(style = Style.NUMBER)
	@Min(0)
	@Max(9223372036854775807L)
	private long startGebotPreis;
	
	@NumberFormat(style = Style.NUMBER)
	@Min(1)
	@Max(9223372036854775807L)
	private long sofortKaufPreis;
	
	@NumberFormat(style = Style.NUMBER)
	@Min(0)
	@Max(9223372036854775807L)
	private long aktuellesGebot;
	
	private LocalDateTime ablaufDatum;
	
	private boolean fuerProtage = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getHoechstBieter() {
		return hoechstBieter;
	}

	public void setHoechstBieter(User hoechstBieter) {
		this.hoechstBieter = hoechstBieter;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public long getStartGebotPreis() {
		return startGebotPreis;
	}

	public void setStartGebotPreis(long startGebotPreis) {
		this.startGebotPreis = startGebotPreis;
	}

	public long getSofortKaufPreis() {
		return sofortKaufPreis;
	}

	public void setSofortKaufPreis(long sofortKaufPreis) {
		this.sofortKaufPreis = sofortKaufPreis;
	}

	public boolean isFuerProtage() {
		return fuerProtage;
	}

	public LocalDateTime getAblaufDatum() {
		return ablaufDatum;
	}

	public void setAblaufDatum(LocalDateTime ablaufDatum) {
		this.ablaufDatum = ablaufDatum;
	}

	public void setFuerProtage(boolean fuerProtage) {
		this.fuerProtage = fuerProtage;
	}

	public long getAktuellesGebot() {
		return aktuellesGebot;
	}

	public void setAktuellesGebot(long aktuellesGebot) {
		this.aktuellesGebot = aktuellesGebot;
	}
}
