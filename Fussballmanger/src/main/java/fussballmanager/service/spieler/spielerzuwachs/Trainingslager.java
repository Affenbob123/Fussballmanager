package fussballmanager.service.spieler.spielerzuwachs;

public enum Trainingslager {

	KEIN_TRAININGSLAGER(1.0, 1.0, ""),
	FERIENCAMP(2.0, 2.0, "Feriencamp"),
	HOEHENTRAINIGSLAGER(3.0, 3.0, "Höhen Trainingslager"),
	JUGENDINTERNAT(4.0, 4.0, "Jugendinternat");
	
	private final double internatFaktor;
	private final double preisFaktor;
	private final String bezeichnung;
	
	Trainingslager(double internatFaktor, double preisFaktor, String bezeichnung) {
		this.internatFaktor = internatFaktor;
		this.preisFaktor = preisFaktor;
		this.bezeichnung = bezeichnung;
	}

	public double getInternatFaktor() {
		return internatFaktor;
	}

	public double getPreisFaktor() {
		return preisFaktor;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
