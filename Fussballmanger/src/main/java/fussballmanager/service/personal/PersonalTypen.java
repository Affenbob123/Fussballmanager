package fussballmanager.service.personal;

public enum PersonalTypen {

	TRAINER("Trainer"),
	BAUARBEITER("Bauarbeiter"),
	ANWALT("Anwalt"),
	ARZT("Arzt");
	
	private final String bezeichnung;
	
	PersonalTypen(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
}
