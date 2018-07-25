package fussballmanager.service.team;

public enum AusrichtungsTypen {
	
	SEHRDEFENSIV("Sehr Defensiv", 0.5, 0.75),
	DEFENSIV("Defensiv", 0.75, 0.875),
	NORMAL("Normal", 1.0, 1.0),
	OFFENSIV("Offensiv", 1.125, 1.25),
	SEHROFFENSIV("Sehr Offensiv", 1.25, 1.5);
	
	private final String bezeichnung;
	private final double wahrscheinlichkeitTorZuErzielen;
	private final double wahrscheinlichkeitTorZuKassieren;
	
	AusrichtungsTypen(String bezeichnung, double wahrscheinlichkeitTorZuErzielen, double wahrscheinlichkeitTorZuKassieren) {
		this.bezeichnung = bezeichnung;
		this.wahrscheinlichkeitTorZuErzielen = wahrscheinlichkeitTorZuErzielen;
		this.wahrscheinlichkeitTorZuKassieren = wahrscheinlichkeitTorZuKassieren;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public double getWahrscheinlichkeitTorZuErzielen() {
		return wahrscheinlichkeitTorZuErzielen;
	}

	public double getWahrscheinlichkeitTorZuKassieren() {
		return wahrscheinlichkeitTorZuKassieren;
	}
}
