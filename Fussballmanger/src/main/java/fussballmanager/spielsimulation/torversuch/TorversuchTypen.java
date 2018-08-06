package fussballmanager.spielsimulation.torversuch;

public enum TorversuchTypen {

	LINKS("Links"),
	MITTE("Mitte"),
	RECHTS("Rechts");
	
	private final String bezeichnung;
	
	TorversuchTypen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
