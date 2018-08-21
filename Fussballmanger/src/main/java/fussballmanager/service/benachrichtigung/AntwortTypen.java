package fussballmanager.service.benachrichtigung;

public enum AntwortTypen {

	ANNEHMEN("Annehmen"),
	ZUWENIG("Zu wenig"),
	KEINE("");
	
	private final String bezeichnung;
	
	AntwortTypen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
