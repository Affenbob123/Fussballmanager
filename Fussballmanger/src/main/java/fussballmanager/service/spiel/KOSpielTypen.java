package fussballmanager.service.spiel;

public enum KOSpielTypen {
	GRUPPENPHASE("Gruppenphase"),
	ERSTERUNDE("1. Runde"),
	ZWEITERUNDE("2. Runde"),
	DRITTERUNDE("3. Runde"),
	ACHTELFINALE("Achtelfinale"),
	VIERTELFINALE("Viertelfinale"),
	HALBFINALE("Halbfinale"),
	FINALE("Finale");
	
	private final String bezeichnung;
	
	KOSpielTypen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
