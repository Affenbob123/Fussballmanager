package fussballmanager.mvc.sekretariat.statistik;

public enum SortierTypen {
	STAERKE("Stärke"),
	ERFAHRUNG("Erfahrung"),
	TORE("Tore"),
	GELBEKARTEN("Gelbe Karten"),
	GELBROTEKARTEN("Gelbrote Karten"),
	ROTEKARTEN("Rote Karten");
	
	private final String bezeichnung;
	
	SortierTypen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
