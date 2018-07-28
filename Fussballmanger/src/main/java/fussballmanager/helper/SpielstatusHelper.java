package fussballmanager.helper;

import java.time.LocalTime;
import java.time.ZoneId;

import fussballmanager.service.spiel.SpieleTypen;

import static java.time.temporal.ChronoUnit.MINUTES;

public class SpielstatusHelper {
	
	public String getAktuellenSpielstatus() {
		
		LocalTime aktuelleUhrzeit  = LocalTime.now(ZoneId.of("Europe/Berlin"));
		LocalTime spielVorbereitungsBeginn = SpieleTypen.LIGASPIEL.getSpielBeginn();
		
		//Turnierspiel
		if(aktuelleUhrzeit.isAfter(SpieleTypen.TURNIERSPIEL.getSpielBeginn()) && 
				aktuelleUhrzeit.isBefore(SpieleTypen.TURNIERSPIEL.getSpielBeginn().plusHours(2))) {
			spielVorbereitungsBeginn = SpieleTypen.TURNIERSPIEL.getSpielBeginn();
		}
		
		//Freundschaftsspiel
		if(aktuelleUhrzeit.isAfter(SpieleTypen.FREUNDSCHAFTSSPIEL.getSpielBeginn()) && 
				aktuelleUhrzeit.isBefore(SpieleTypen.FREUNDSCHAFTSSPIEL.getSpielBeginn().plusHours(2))) {
			spielVorbereitungsBeginn = SpieleTypen.FREUNDSCHAFTSSPIEL.getSpielBeginn();
		}
		
		//Ligaspiel
		if(aktuelleUhrzeit.isAfter(SpieleTypen.LIGASPIEL.getSpielBeginn()) && 
				aktuelleUhrzeit.isBefore(SpieleTypen.LIGASPIEL.getSpielBeginn().plusHours(2))) {
			spielVorbereitungsBeginn = SpieleTypen.LIGASPIEL.getSpielBeginn();
		}
		
		//Pokalspiel
		if(aktuelleUhrzeit.isAfter(SpieleTypen.POKALSPIEL.getSpielBeginn()) && 
				aktuelleUhrzeit.isBefore(SpieleTypen.POKALSPIEL.getSpielBeginn().plusHours(2))) {
			spielVorbereitungsBeginn = SpieleTypen.POKALSPIEL.getSpielBeginn();
		}

		LocalTime spielBeginn = spielVorbereitungsBeginn.plusMinutes(15);
		LocalTime halbzeitBeginn = spielBeginn.plusMinutes(45);
		LocalTime halbzeitEnde = halbzeitBeginn.plusMinutes(15);
		LocalTime spielEnde = halbzeitEnde.plusMinutes(45);
		
		if(aktuelleUhrzeit.isAfter(spielVorbereitungsBeginn) && aktuelleUhrzeit.isBefore(spielBeginn)) {
			return "Spielvorbereitung | Spielbeginn: " + spielBeginn.toString();
		}
		
		if(aktuelleUhrzeit.isAfter(spielBeginn) && aktuelleUhrzeit.isBefore(halbzeitBeginn)) {
			long spielminute = spielBeginn.until(aktuelleUhrzeit, MINUTES);
			spielminute++;
			return "Spielminute: " + spielminute;
		}
		
		if(aktuelleUhrzeit.isAfter(halbzeitBeginn) && aktuelleUhrzeit.isBefore(halbzeitEnde)) {
			return "Halbzeit bis: " + halbzeitEnde.toString();
		}
		
		if(aktuelleUhrzeit.isAfter(halbzeitEnde) && aktuelleUhrzeit.isBefore(spielEnde)) {
			long spielminute = halbzeitEnde.until(aktuelleUhrzeit, MINUTES);
			spielminute = spielminute + 45 + 1;
			return "Spielminute: " + spielminute;
		}
		return "";
	}
}
