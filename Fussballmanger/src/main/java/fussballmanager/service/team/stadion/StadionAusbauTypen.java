package fussballmanager.service.team.stadion;

public enum StadionAusbauTypen {

	IMBISS("Imbiss", 3, 0, 5),
	TRAININGSGELAENDE("Trainingsgelände", 3, 0, 10),
	VIPLOUNGE("VIP-Lounge", 3, 0, 6),
	ERSATZBANK("Ersatzbank", 4, 0, 3),
	WERBEBANDEN("Werbebanden", 3, 0, 3),
	JUGENDINTERNAT("Jugendinternat", 1, 0, 30);
	
	private final String bezeichnung;
	private final int maximaleStufe;
	private int aktuelleStufe;
	private final int ausbauDauer;
	
	StadionAusbauTypen(String bezeichnung, int maximaleStufe, int aktuelleStufe, int ausbauDauer) {
		this.bezeichnung = bezeichnung;
		this.maximaleStufe = maximaleStufe;
		this.aktuelleStufe = aktuelleStufe;
		this.ausbauDauer = ausbauDauer;
	}

	public int getAktuelleStufe() {
		return aktuelleStufe;
	}

	public void setAktuelleStufe(int aktuelleStufe) {
		this.aktuelleStufe = aktuelleStufe;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public int getMaximaleStufe() {
		return maximaleStufe;
	}

	public int getAusbauDauer() {
		return ausbauDauer;
	}
}
