package fussballmanager.helper;

import java.time.LocalTime;
import java.time.ZoneId;

import fussballmanager.service.spiel.SpieleTypen;

import static java.time.temporal.ChronoUnit.MINUTES;

public class SpielstatusHelper {
	
	public String getAktuellenSpielstatus(SpieleTypen spielTyp) {
		
		LocalTime spielVorbereitungsBeginn = spielTyp.getSpielBeginn();
		LocalTime spielBeginn = spielVorbereitungsBeginn.plusMinutes(15);
		LocalTime halbzeitBeginn = spielBeginn.plusMinutes(45);
		LocalTime halbzeitEnde = halbzeitBeginn.plusMinutes(15);
		LocalTime spielEnde = halbzeitEnde.plusMinutes(45);
		LocalTime aktuelleUhrzeit  = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		if(aktuelleUhrzeit.isAfter(spielVorbereitungsBeginn) && aktuelleUhrzeit.isBefore(spielBeginn)) {
			return "Spielvorbereitung | Spielbeginn: " + spielBeginn.toString();
		}
		
		if(aktuelleUhrzeit.isAfter(spielBeginn) && aktuelleUhrzeit.isBefore(halbzeitBeginn)) {
			long spielminute = spielBeginn.until(aktuelleUhrzeit, MINUTES);
			return "Spielminute: " + spielminute;
		}
		
		if(aktuelleUhrzeit.isAfter(spielVorbereitungsBeginn) && aktuelleUhrzeit.isBefore(spielBeginn)) {
			return "Halbzeit bis: " + halbzeitEnde.toString();
		}
		
		if(aktuelleUhrzeit.isAfter(halbzeitEnde) && aktuelleUhrzeit.isBefore(spielEnde)) {
			long spielminute = halbzeitEnde.until(aktuelleUhrzeit, MINUTES);
			return "Spielminute: " + spielminute;
		}
		
		return "";
	}
}
