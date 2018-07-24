package fussballmanager.service.spiel;

import java.time.LocalTime;

public enum SpieleTypen {
	
	TURNIERSPIEL("Ligaspiel", LocalTime.of(14, 00)),
	FREUNDSCHAFTSSPIEL("Ligaspiel", LocalTime.of(16, 00)),
	LIGASPIEL("Ligaspiel", LocalTime.of(18, 00)),
	POKALSPIEL("Ligaspiel", LocalTime.of(20, 00));
	
	
	private final String name;
	
	private final LocalTime spielBeginn;
	
	SpieleTypen(String name, LocalTime spielBeginn) {
		this.name = name;
		this.spielBeginn = spielBeginn;
	}

	public String getName() {
		return name;
	}

	public LocalTime getSpielBeginn() {
		return spielBeginn;
	}
}
