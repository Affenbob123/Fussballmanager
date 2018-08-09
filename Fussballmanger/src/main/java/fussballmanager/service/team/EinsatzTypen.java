package fussballmanager.service.team;

public enum EinsatzTypen {

	GEMUETLICH("Gemütlich", 0.70, 0.5),
	LOCKER("Locker", 0.85, 0.75),
	NORMAL("Normal", 1.0, 1.0),
	AGGRESSIV("Aggressiv", 1.15, 1.75),
	BRUTAL("Brutal", 1.3, 3);
	
	private final String bezeichnung;
	private final double staerkenFaktor;
	private final double wahrscheinlichkeitKarteUndVerletzung;
	
	EinsatzTypen(String bezeichnung, double staerkenFaktor, double wahrscheinlichkeitKarteUndVerletzung) {
		this.bezeichnung = bezeichnung;
		this.staerkenFaktor = staerkenFaktor;
		this.wahrscheinlichkeitKarteUndVerletzung = wahrscheinlichkeitKarteUndVerletzung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public double getStaerkenFaktor() {
		return staerkenFaktor;
	}

	public double getWahrscheinlichkeitKarteUndVerletzung() {
		return wahrscheinlichkeitKarteUndVerletzung;
	}
}
